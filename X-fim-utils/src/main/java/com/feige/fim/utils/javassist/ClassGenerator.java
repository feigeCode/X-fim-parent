package com.feige.fim.utils.javassist;

import com.feige.fim.utils.ArrayUtils;
import com.feige.fim.utils.ClassUtils;
import com.feige.fim.utils.ReflectionUtils;
import com.feige.fim.utils.StringUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClassGenerator implements AutoCloseable {
    public static final String CLASS_NAME_PREFIX = "com.feige.fim.msg.dc.";
    private static final String SIMPLE_NAME_TAG = "<init>";
    private static final AtomicInteger CLASS_NAME_IDX = new AtomicInteger();
    private static String debugDump = null;
    private CtClass ctClass;
    private String className;
    private String superClass;
    private Set<String> interfaces;
    private List<MethodDefine> constructors;
    private List<FieldDefine> fields;
    private List<MethodDefine> methods;
    private List<AnnotationDefine> classAnnotations;
    
    
    public static String getDebugDump() {
        return debugDump;
    }

    public static void setDebugDump(String debugDump) {
        ClassGenerator.debugDump = debugDump;
    }

    private static String modifier(int mod) {
        StringBuilder modifier = new StringBuilder();
        if (Modifier.isPublic(mod)) {
            modifier.append("public");
        } else if (Modifier.isProtected(mod)) {
            modifier.append("protected");
        } else if (Modifier.isPrivate(mod)) {
            modifier.append("private");
        }

        if (Modifier.isStatic(mod)) {
            modifier.append(" static");
        }
        if (Modifier.isVolatile(mod)) {
            modifier.append(" volatile");
        }

        return modifier.toString();
    }

    
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<AnnotationDefine> getClassAnnotations() {
        if (classAnnotations != null){
            classAnnotations = new ArrayList<>();
        }
        return classAnnotations;
    }
    
    public ClassGenerator addClassAnnotation(AnnotationDefine annotationDefine){
        getClassAnnotations().add(annotationDefine);
        return this;
    }

    public void setClassAnnotations(List<AnnotationDefine> classAnnotations) {
        this.classAnnotations = classAnnotations;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }
    
    public ClassGenerator addInterface(String interfaceName){
        getInterfaces().add(interfaceName);
        return this;
    }

    public Set<String> getInterfaces() {
        if (interfaces == null){
            interfaces = new HashSet<>();
        }
        return interfaces;
    }

    public void setInterfaces(Set<String> interfaces) {
        this.interfaces = interfaces;
    }

    public MethodDefine addConstructor(String constructor){
        MethodDefine methodDefine = new MethodDefine(constructor);
        getConstructors().add(methodDefine);
        return methodDefine;
    }
    
    public List<MethodDefine> getConstructors() {
        if (constructors == null){
            constructors = new ArrayList<>();
        }
        return constructors;
    }

    public void setConstructors(List<MethodDefine> constructors) {
        this.constructors = constructors;
    }
    
    public FieldDefine addField(String field){
        FieldDefine fieldDefine = new FieldDefine(field);
        getFields().add(fieldDefine);
        return fieldDefine;
    }

    public ClassGenerator addField(String name, int mod, Class<?> type, boolean isGenGetterAndSetter) {
        return addField(name, mod, type, null, isGenGetterAndSetter, void.class, null);
    }
    
    public ClassGenerator addField(String name, int mod, Class<?> type, boolean isGenGetterAndSetter, Class<?> setterReturnType) {
        return addField(name, mod, type, null, isGenGetterAndSetter, setterReturnType, null);
    }
    public ClassGenerator addField(String name, int mod, Class<?> type, List<AnnotationDefine> annotationDefines) {
        return addField(name, mod, type, annotationDefines, false, void.class, null);
    }

    public ClassGenerator addField(String name, int mod, Class<?> type, List<AnnotationDefine> annotationDefines, boolean isGenGetterAndSetter, Class<?> setterReturnType, String def) {
        StringBuilder sb = new StringBuilder();
        sb.append(modifier(mod)).append(' ').append(ReflectionUtils.getName(type)).append(' ');
        sb.append(name);
        if (StringUtils.isNotBlank(def)) {
            sb.append('=');
            sb.append(def);
        }
        sb.append(';');
        FieldDefine fieldDefine = addField(sb.toString());
        if (annotationDefines != null){
            fieldDefine.getAnnotationDefines().addAll(annotationDefines);
        }
        if (isGenGetterAndSetter){
            String capitalizeName = StringUtils.capitalize(name);
            String getterMethodName = name;
            if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)){
                if (!name.startsWith("is")){
                    getterMethodName = "is" + capitalizeName;
                }
            }else {
                getterMethodName = "get" + capitalizeName;
            }
            addMethod(getterMethodName, Modifier.PUBLIC, type, null, "\nreturn " + name + ";\n");
            String setterMethodBody = "\nthis." + name + " = arg0;\n";
            if (!void.class.equals(setterReturnType)){
                setterMethodBody += "return this;\n";
            }
            addMethod("set" + capitalizeName, Modifier.PUBLIC, setterReturnType, new Class[]{type}, setterMethodBody);
        }
        return this;
    }
    public List<FieldDefine> getFields() {
        if (fields == null){
            fields = new ArrayList<>();
        }
        return fields;
    }

    public void setFields(List<FieldDefine> fields) {
        this.fields = fields;
    }
    public ClassGenerator addMethod(String name, int mod, Class<?> rt, Class<?>[] pts, String body) {
        return addMethod(name, mod, rt, pts, null, body, null, null);
    }

    public ClassGenerator addMethod(String name, int mod, Class<?> rt, Class<?>[] pts, Class<?>[] ets,
                                    String body, Map<Integer, List<AnnotationDefine>> parametersAnnotations, List<AnnotationDefine> annotationDefines) {
        StringBuilder sb = new StringBuilder();
        sb.append(modifier(mod)).append(' ').append(ReflectionUtils.getName(rt)).append(' ').append(name);
        sb.append('(');
        if (ArrayUtils.isNotEmpty(pts)) {
            for (int i = 0; i < pts.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(ReflectionUtils.getName(pts[i]));
                sb.append(" arg").append(i);
            }
        }
        sb.append(')');
        if (ArrayUtils.isNotEmpty(ets)) {
            sb.append(" throws ");
            for (int i = 0; i < ets.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(ReflectionUtils.getName(ets[i]));
            }
        }
        sb.append('{').append(body).append('}');
        MethodDefine methodDefine = addMethod(sb.toString());
        if (annotationDefines != null) {
            methodDefine.getAnnotationDefines().addAll(annotationDefines);
        }
        
        if (parametersAnnotations != null){
            methodDefine.getParametersAnnotations().putAll(parametersAnnotations);
        }
        return this;
    }
    
    public MethodDefine addMethod(String method){
        MethodDefine methodDefine = new MethodDefine(method);
        getMethods().add(methodDefine);
        return methodDefine;
    }
    public List<MethodDefine> getMethods() {
        if (methods == null){
            this.methods = new ArrayList<>();
        }
        return methods;
    }

    public void setMethods(List<MethodDefine> methods) {
        this.methods = methods;
    }

    public CtClass getCtClass(){
        return this.ctClass;
    }

    public void addMethodParameterAnnotations(Map<Integer, List<AnnotationDefine>> annotationDefinesMap, CtConstructor constructor, ConstPool constPool){
        ParameterAnnotationsAttribute parameterAnnotationsAttribute = getParameterAnnotationsAttribute(annotationDefinesMap, constPool);
        if (parameterAnnotationsAttribute == null){
            return;
        }
        constructor.getMethodInfo().addAttribute(parameterAnnotationsAttribute);
    }

    public void addMethodParameterAnnotations(Map<Integer, List<AnnotationDefine>> annotationDefinesMap, CtMethod method, ConstPool constPool){
        ParameterAnnotationsAttribute parameterAnnotationsAttribute = getParameterAnnotationsAttribute(annotationDefinesMap, constPool);
        if (parameterAnnotationsAttribute == null){
            return;
        }
        method.getMethodInfo().addAttribute(parameterAnnotationsAttribute);
    }

    public ParameterAnnotationsAttribute getParameterAnnotationsAttribute(Map<Integer, List<AnnotationDefine>> annotationDefinesMap, ConstPool constPool){
        if (annotationDefinesMap == null || annotationDefinesMap.isEmpty()){
            return null;
        }
        ParameterAnnotationsAttribute parameterAnnotationsAttribute = new ParameterAnnotationsAttribute(constPool, ParameterAnnotationsAttribute.visibleTag);
        Integer idxMax = Collections.max(annotationDefinesMap.keySet());
        Integer annotationMax = Collections.max(annotationDefinesMap.values().stream().map(List::size).collect(Collectors.toList()));
        Annotation[][] annotations = new Annotation[idxMax + 1][annotationMax];
        annotationDefinesMap.forEach((paramIdx, annotationDefineList) -> {
            for (int i = 0; i < annotationDefineList.size(); i++) {
                annotations[paramIdx][i] = parse(annotationDefineList.get(i), constPool);
            }
        });
        parameterAnnotationsAttribute.setAnnotations(annotations);
        return parameterAnnotationsAttribute;
    }

    public void addAnnotations(List<AnnotationDefine> annotationDefineList, CtClass ctClass, ConstPool constPool) throws NotFoundException {
        AnnotationsAttribute annotationsAttribute = getAnnotationsAttribute(annotationDefineList, constPool);
        if (annotationsAttribute == null){
            return;
        }
        ctClass.getClassFile().addAttribute(annotationsAttribute);
    }

    public void addAnnotations(List<AnnotationDefine> annotationDefineList, CtConstructor constructor, ConstPool constPool) throws NotFoundException {
        AnnotationsAttribute annotationsAttribute = getAnnotationsAttribute(annotationDefineList, constPool);
        if (annotationsAttribute == null){
            return;
        }
        constructor.getMethodInfo().addAttribute(annotationsAttribute);
    }

    public void addAnnotations(List<AnnotationDefine> annotationDefineList, CtMethod method, ConstPool constPool) throws NotFoundException {
        AnnotationsAttribute annotationsAttribute = getAnnotationsAttribute(annotationDefineList, constPool);
        if (annotationsAttribute == null){
            return;
        }
        method.getMethodInfo().addAttribute(annotationsAttribute);
    }

    public void addAnnotations(List<AnnotationDefine> annotationDefineList, CtField field, ConstPool constPool){
        AnnotationsAttribute annotationsAttribute = getAnnotationsAttribute(annotationDefineList, constPool);
        if (annotationsAttribute == null){
            return;
        }
        field.getFieldInfo().addAttribute(annotationsAttribute);
    }

    public AnnotationsAttribute getAnnotationsAttribute(List<AnnotationDefine> annotationDefineList, ConstPool constPool){
        if (CollectionUtils.isEmpty(annotationDefineList)){
            return null;
        }
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        for (AnnotationDefine annotationDefine : annotationDefineList) {
            Annotation annotation = parse(annotationDefine, constPool);
            annotationsAttribute.addAnnotation(annotation);
        }
        return annotationsAttribute;
    }

    public Annotation parse(AnnotationDefine annotationDefine, ConstPool constPool){
        Annotation annotation = new Annotation(annotationDefine.getAnnotationName(), constPool);
        Map<String, Function<ConstPool, MemberValue>> memberValues = annotationDefine.getMemberValues();
        memberValues.forEach((name, mvFun) -> annotation.addMemberValue(name, mvFun.apply(constPool)));
        return annotation;
    }

    public Class<?> generate(Class<?> neighbor){
        if (ctClass != null){
            ctClass.detach();
        }
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        ClassPool classPool = ClassPoolUtils.getClassPool(defaultClassLoader, neighbor);
        int id = CLASS_NAME_IDX.incrementAndGet();
        try {
            CtClass superCtClass = superClass == null ? null : classPool.get(superClass);
            if (className == null) {
                className = (superClass == null || javassist.Modifier.isPublic(superCtClass.getModifiers())
                        ? ClassGenerator.class.getName() : superClass + "$sc") + id;
            }else {
                if (className.contains(".")){
                    className = className + id;
                }else {
                    className = CLASS_NAME_PREFIX + className + id;
                }
            }
            ctClass = classPool.makeClass(className);
            ConstPool constPool = ctClass.getClassFile().getConstPool();
            if (superCtClass != null){
                ctClass.setSuperclass(superCtClass);
            }
            if (CollectionUtils.isNotEmpty(interfaces)){
                for (String interfaceName : interfaces) {
                    ctClass.addInterface(classPool.get(interfaceName));
                }
            }

            if (CollectionUtils.isNotEmpty(fields)){
                for (FieldDefine fieldDefine : fields) {
                    String field = fieldDefine.getField();
                    CtField ctField = CtField.make(field, ctClass);
                    addAnnotations(fieldDefine.getAnnotationDefines(), ctField, constPool);
                    ctClass.addField(ctField);
                }
            }


            if (CollectionUtils.isNotEmpty(constructors)){
                for (MethodDefine constructor : constructors) {
                    String method = constructor.getMethod();
                    // inner class name include $.
                    String[] sn = ctClass.getSimpleName().split("\\$+");
                    CtConstructor ctConstructor = CtNewConstructor.make(method.replaceFirst(SIMPLE_NAME_TAG, sn[sn.length - 1]), ctClass);
                    addAnnotations(constructor.getAnnotationDefines(), ctConstructor, constPool);
                    addMethodParameterAnnotations(constructor.getParametersAnnotations(), ctConstructor, constPool);
                    ctClass.addConstructor(ctConstructor);
                }
            }else {
                ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));
            }
            
            
            if (CollectionUtils.isNotEmpty(methods)){
                for (MethodDefine methodDefine : methods) {
                    String method = methodDefine.getMethod();
                    CtMethod ctMethod = CtNewMethod.make(method, ctClass);
                    addAnnotations(methodDefine.getAnnotationDefines(), ctMethod, constPool);
                    addMethodParameterAnnotations(methodDefine.getParametersAnnotations(), ctMethod, constPool);
                    ctClass.addMethod(ctMethod);
                }
            }
            if (StringUtils.isNotBlank(debugDump)){
                CtClass.debugDump = debugDump;
            }
            return classPool.toClass(ctClass, neighbor, defaultClassLoader, getClass().getProtectionDomain());
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }

    public void release() {
        if (ctClass != null){
            ctClass.detach();
        }
        if (interfaces != null) {
            interfaces.clear();
        }
        if (fields != null) {
            fields.clear();
        }
        if (methods != null) {
            methods.clear();
        }
        if (constructors != null) {
            constructors.clear();
        }
    }

    @Override
    public void close() throws Exception {
        this.release();
    }
}
