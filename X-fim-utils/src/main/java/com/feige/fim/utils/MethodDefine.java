package com.feige.fim.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodDefine {
    
    private final String method;
    
    private Map<Integer, List<AnnotationDefine>> parametersAnnotations;
    
    private List<AnnotationDefine> annotationDefines;

    public MethodDefine(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public Map<Integer, List<AnnotationDefine>> getParametersAnnotations() {
        if (parametersAnnotations == null){
            this.parametersAnnotations = new HashMap<>();
        }
        return parametersAnnotations;
    }

    public void setParametersAnnotations(Map<Integer, List<AnnotationDefine>> parametersAnnotations) {
        this.parametersAnnotations = parametersAnnotations;
    }

    public List<AnnotationDefine> getAnnotationDefines() {
        if (annotationDefines == null) {
            this.annotationDefines = new ArrayList<>();
        }
        return annotationDefines;
    }

    public void setAnnotationDefines(List<AnnotationDefine> annotationDefines) {
        this.annotationDefines = annotationDefines;
    }
    
    public void addAnnotationDefine(AnnotationDefine... annotationDefine){
        getAnnotationDefines().addAll(Arrays.asList(annotationDefine));
    }
    
    public void addAnnotationDefine(int parameterIndex, AnnotationDefine... annotationDefines){
        this.getParametersAnnotations().computeIfAbsent(parameterIndex, k -> new ArrayList<>()).addAll(Arrays.asList(annotationDefines));
    }
}
