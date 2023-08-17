package com.feige.fim;

import com.feige.fim.utils.javassist.ClassGenerator;
import com.feige.fim.utils.ReflectionUtils;
import com.feige.fim.utils.StringUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassGenTest {
    
    public interface TestMsg {
        int getId();
        
        String getName();
        
        String getTag();
        
        int getAge();
        
        boolean isTest();
    }
    
    @Test
    public void generateClass() throws InstantiationException, IllegalAccessException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(TestMsg.class);
        ClassGenerator classGenerator = new ClassGenerator();
        classGenerator.setClassName("com.feige.fim.test.TestMsgDC");
        classGenerator.addInterface(TestMsg.class.getName());
        for (Method method : methods) {
            String name = method.getName();
            Class<?> returnType = method.getReturnType();
            String fieldName;
            if (Boolean.class.equals(returnType) || Boolean.TYPE.equals(returnType)){
                if (name.startsWith("is")){
                    fieldName = name.replaceFirst("is", "");
                }else {
                    fieldName = name.replaceFirst("get", "");
                }
            }else {
                fieldName = name.replaceFirst("get", "");
            }
            classGenerator.addField(StringUtils.uncapitalize(fieldName), Modifier.PRIVATE, returnType, true);
        }
        Class<TestMsg> generate = (Class<TestMsg>) classGenerator.generate(TestMsg.class);
        TestMsg instance = generate.newInstance();
        System.out.println(instance);
    }
}
