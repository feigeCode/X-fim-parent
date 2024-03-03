package com.feige.utils.spi.apt;

import com.feige.utils.common.Pair;
import com.feige.utils.common.StringUtils;
import com.feige.utils.spi.ComponentsLoader;
import com.feige.utils.spi.annotation.SPI;
import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;


import static com.google.auto.common.AnnotationMirrors.getAnnotationValue;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static javax.lang.model.SourceVersion.RELEASE_8;

@SupportedOptions({"debug", "verify"})
@SupportedSourceVersion(RELEASE_8)
@AutoService(Processor.class)
public class SpiProcessor extends AbstractProcessor {

    @VisibleForTesting
    static final String MISSING_SERVICES_ERROR = "No service interfaces provided for element!";

    public static final String ANNOTATION_NAME = SPI.class.getName();

    private final List<String> exceptionStacks = Collections.synchronizedList(new ArrayList<>());

    private final Multimap<String, Pair<String, String>> providers = HashMultimap.create();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ANNOTATION_NAME);
    }

    ImmutableList<String> exceptionStacks() {
        return ImmutableList.copyOf(exceptionStacks);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            processSPI(annotations, roundEnv);
        }catch (RuntimeException e){
            String trace = getStackTraceAsString(e);
            exceptionStacks.add(trace);
            fatalError(trace);
        }
        return false;
    }


    private void processSPI(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
        if (roundEnv.processingOver()){
            generateSource();
        }else {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SPI.class);
            log(annotations.toString());
            log(elements.toString());
            for (Element element : elements) {
                TypeElement providerImplementer = MoreElements.asType(element);
                AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(element, ANNOTATION_NAME).get();
                // DeclaredType表示已声明的类型，即类、接口或枚举类型
                ImmutableSet<DeclaredType> providerInterfaces = getValueFieldOfClasses(annotationMirror);
                String value = getStringValueFieldOfClasses(annotationMirror);
                if (providerInterfaces.isEmpty()) {
                    error(MISSING_SERVICES_ERROR, element, annotationMirror);
                    continue;
                }
                for (DeclaredType providerInterface : providerInterfaces) {
                    TypeElement providerType = MoreTypes.asTypeElement(providerInterface);
                    String binaryName = getBinaryName(providerType);
                    if (Object.class.getName().equals(getBinaryName(providerType))){
                        continue;
                    }
                    log("provider interface: " + providerType.getQualifiedName());
                    log("provider implementer: " + providerImplementer.getQualifiedName());
                    if (checkImplementer(providerImplementer, providerType, annotationMirror)) {
                        if (StringUtils.isBlank(value)){
                            value = StringUtils.uncapitalize(providerImplementer.getSimpleName().toString());
                        }
                        String providerImplName = getBinaryName(providerImplementer);
                        providers.put(binaryName, Pair.of(value, providerImplName));
                    } else {
                        String message =
                                "ServiceProviders must implement their service provider interface. "
                                        + providerImplementer.getQualifiedName()
                                        + " does not implement "
                                        + providerType.getQualifiedName();
                        error(message, element, annotationMirror);
                    }
                }
            }
        }



    }

    private String getBinaryName(TypeElement element) {
        return getBinaryNameImpl(element, element.getSimpleName().toString());
    }

    private String getBinaryNameImpl(TypeElement element, String className) {
        Element enclosingElement = element.getEnclosingElement();

        if (enclosingElement instanceof PackageElement) {
            PackageElement pkg = MoreElements.asPackage(enclosingElement);
            if (pkg.isUnnamed()) {
                return className;
            }
            return pkg.getQualifiedName() + "." + className;
        }

        TypeElement typeElement = MoreElements.asType(enclosingElement);
        return getBinaryNameImpl(typeElement, typeElement.getSimpleName() + "$" + className);
    }

    private String getStringValueFieldOfClasses(AnnotationMirror annotationMirror) {
        return getAnnotationValue(annotationMirror, "value")
                .accept(
                        new SimpleAnnotationValueVisitor8<String, Void>() {
                            @Override
                            public String visitString(String s, Void unused) {
                                return s;
                            }
                        },
                        null);
    }

    private ImmutableSet<DeclaredType> getValueFieldOfClasses(AnnotationMirror annotationMirror) {
        return getAnnotationValue(annotationMirror, "interfaces")
                .accept(
                        new SimpleAnnotationValueVisitor8<ImmutableSet<DeclaredType>, Void>(ImmutableSet.of()) {
                            @Override
                            public ImmutableSet<DeclaredType> visitType(TypeMirror typeMirror, Void v) {
                                return ImmutableSet.of(MoreTypes.asDeclared(typeMirror));
                            }

                            @Override
                            public ImmutableSet<DeclaredType> visitArray(
                                    List<? extends AnnotationValue> values, Void v) {
                                return values.stream()
                                        .flatMap(value -> value.accept(this, null).stream())
                                        .collect(collectingAndThen(toList(), ImmutableSet::copyOf));
                            }
                        },
                        null);
    }




    private boolean checkImplementer(
            TypeElement providerImplementer,
            TypeElement providerType,
            AnnotationMirror annotationMirror) {

        String verify = processingEnv.getOptions().get("verify");
        if (verify == null || !Boolean.parseBoolean(verify)) {
            return true;
        }

        // TODO: We're currently only enforcing the subtype relationship
        // constraint. It would be nice to enforce them all.

        Types types = processingEnv.getTypeUtils();

        if (types.isSubtype(providerImplementer.asType(), providerType.asType())) {
            return true;
        }

        // Maybe the provider has generic type, but the argument to @AutoService can't be generic.
        // So we allow that with a warning, which can be suppressed with @SuppressWarnings("rawtypes").
        // See https://github.com/google/auto/issues/870.
        if (types.isSubtype(providerImplementer.asType(), types.erasure(providerType.asType()))) {
            if (!rawTypesSuppressed(providerImplementer)) {
                warning(
                        "Service provider "
                                + providerType
                                + " is generic, so it can't be named exactly by @AutoService."
                                + " If this is OK, add @SuppressWarnings(\"rawtypes\").",
                        providerImplementer,
                        annotationMirror);
            }
            return true;
        }

        return false;
    }

    private static boolean rawTypesSuppressed(Element element) {
        for (; element != null; element = element.getEnclosingElement()) {
            SuppressWarnings suppress = element.getAnnotation(SuppressWarnings.class);
            if (suppress != null && Arrays.asList(suppress.value()).contains("rawtypes")) {
                return true;
            }
        }
        return false;
    }

    private void generateSource(){
        generateServices();
    }


    private void generateServices(){
        Filer filer = processingEnv.getFiler();
        for (String providerInterface : providers.keySet()) {
            String resourceFile = ComponentsLoader.getPath(providerInterface);
            log("Working on resource file: " + resourceFile);
            try {
                SortedSet<String> allServices = Sets.newTreeSet();
                try {
                    // would like to be able to print the full path
                    // before we attempt to get the resource in case the behavior
                    // of filer.getResource does change to match the spec, but there's
                    // no good way to resolve CLASS_OUTPUT without first getting a resource.
                    FileObject existingFile =
                            filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                    log("Looking for existing resource file at " + existingFile.toUri());
                    Set<String> oldServices = ComponentsLoader.readServiceFile(existingFile.openInputStream());
                    log("Existing service entries: " + oldServices);
                    allServices.addAll(oldServices);
                } catch (IOException e) {
                    // According to the javadoc, Filer.getResource throws an exception
                    // if the file doesn't already exist.  In practice this doesn't
                    // appear to be the case.  Filer.getResource will happily return a
                    // FileObject that refers to a non-existent file but will throw
                    // IOException if you try to open an input stream for it.
                    log("Resource file did not already exist.");
                }
                Collection<Pair<String, String>> pairs = providers.get(providerInterface);
                Set<String> newServices = new HashSet<>();
                for (Pair<String, String> pair : pairs) {
                    newServices.add(pair.getK() + "=" + pair.getV());
                }
                if (!allServices.addAll(newServices)) {
                    log("No new service entries being added.");
                    continue;
                }

                log("New service file contents: " + allServices);
                FileObject fileObject =
                        filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
                try (OutputStream out = fileObject.openOutputStream()) {
                    ComponentsLoader.writeServiceFile(allServices, out);
                }
                log("Wrote to: " + fileObject.toUri());
            } catch (IOException e) {
                fatalError("Unable to create " + resourceFile + ", " + e);
                return;
            }
        }
    }

    private void log(String msg) {
        if (processingEnv.getOptions().containsKey("debug")) {
            processingEnv.getMessager().printMessage(Kind.NOTE, msg);
        }
    }

    private void warning(String msg, Element element, AnnotationMirror annotation) {
        processingEnv.getMessager().printMessage(Kind.WARNING, msg, element, annotation);
    }

    private void error(String msg, Element element, AnnotationMirror annotation) {
        processingEnv.getMessager().printMessage(Kind.ERROR, msg, element, annotation);
    }

    private void fatalError(String msg) {
        processingEnv.getMessager().printMessage(Kind.ERROR, "FATAL ERROR: " + msg);
    }
}
