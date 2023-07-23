package com.feige.fim.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassGenerator implements AutoCloseable {
    public static final String CLASS_NAME_PREFIX = "com.feige.fim.msg.dc.";
    private static final String SIMPLE_NAME_TAG = "<init>";
    private static final AtomicInteger CLASS_NAME_IDX = new AtomicInteger();
    private static String debugDump = null;
    private CtClass ctClass;
    private String className;
    private String superClass;
    private Set<String> interfaces;
    private List<String> constructors;
    private List<String> fields;
    private List<String> methods;

    
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

    public ClassGenerator addConstructor(String constructor){
        getConstructors().add(constructor);
        return this;
    }
    
    public List<String> getConstructors() {
        if (constructors == null){
            constructors = new ArrayList<>();
        }
        return constructors;
    }

    public void setConstructors(List<String> constructors) {
        this.constructors = constructors;
    }
    
    public ClassGenerator addField(String field){
        getFields().add(field);
        return this;
    }
    
    public ClassGenerator addField(String name, int mod, Class<?> type, boolean isGenGetterAndSetter) {
        return addField(name, mod, type, isGenGetterAndSetter, null);
    }

    public ClassGenerator addField(String name, int mod, Class<?> type,  boolean isGenGetterAndSetter, String def) {
        StringBuilder sb = new StringBuilder();
        sb.append(modifier(mod)).append(' ').append(ReflectionUtils.getName(type)).append(' ');
        sb.append(name);
        if (StringUtils.isNotBlank(def)) {
            sb.append('=');
            sb.append(def);
        }
        sb.append(';');
        addField(sb.toString());
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
            addMethod("set" + capitalizeName, Modifier.PUBLIC, void.class, new Class[]{type}, "\nthis." + name + " = arg0;\n");
        }
        return this;
    }
    public List<String> getFields() {
        if (fields == null){
            fields = new ArrayList<>();
        }
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
    public ClassGenerator addMethod(String name, int mod, Class<?> rt, Class<?>[] pts, String body) {
        return addMethod(name, mod, rt, pts, null, body);
    }

    public ClassGenerator addMethod(String name, int mod, Class<?> rt, Class<?>[] pts, Class<?>[] ets,
                                    String body) {
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
        return addMethod(sb.toString());
    }
    
    public ClassGenerator addMethod(String method){
        getMethods().add(method);
        return this;
    }
    public List<String> getMethods() {
        if (methods == null){
            this.methods = new ArrayList<>();
        }
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
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
            
            if (superCtClass != null){
                ctClass.setSuperclass(superCtClass);
            }
            if (CollectionUtils.isNotEmpty(interfaces)){
                for (String interfaceName : interfaces) {
                    ctClass.addInterface(classPool.get(interfaceName));
                }
            }
            
            if (CollectionUtils.isNotEmpty(constructors)){
                for (String constructor : constructors) {
                    // inner class name include $.
                    String[] sn = ctClass.getSimpleName().split("\\$+"); 
                    ctClass.addConstructor(
                            CtNewConstructor.make(constructor.replaceFirst(SIMPLE_NAME_TAG, sn[sn.length - 1]), ctClass));
                }
            }else {
                ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));
            }
            
            if (CollectionUtils.isNotEmpty(fields)){
                for (String field : fields) {
                    ctClass.addField(CtField.make(field, ctClass));
                }
            }
            
            if (CollectionUtils.isNotEmpty(methods)){
                for (String method : methods) {
                    ctClass.addMethod(CtNewMethod.make(method, ctClass));
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
