package com.feige.utils.javassist;

import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.MemberValue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AnnotationDefine {
    private String annotationName;
    private Map<String, Function<ConstPool, MemberValue>> memberValues = new HashMap<>();
    
    public void addMemberValue(String memberName, Function<ConstPool, MemberValue> mvFun){
        memberValues.put(memberName, mvFun);
    }

    public String getAnnotationName() {
        return annotationName;
    }

    public void setAnnotationName(String annotationName) {
        this.annotationName = annotationName;
    }

    public Map<String, Function<ConstPool, MemberValue>> getMemberValues() {
        return memberValues;
    }

    public void setMemberValues(Map<String, Function<ConstPool, MemberValue>> memberValues) {
        this.memberValues = memberValues;
    }
}
