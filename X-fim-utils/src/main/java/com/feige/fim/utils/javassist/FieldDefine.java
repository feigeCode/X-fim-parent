package com.feige.fim.utils.javassist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldDefine {
    private final String field;
    
    private List<AnnotationDefine> annotationDefines;

    public FieldDefine(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
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
    
}
