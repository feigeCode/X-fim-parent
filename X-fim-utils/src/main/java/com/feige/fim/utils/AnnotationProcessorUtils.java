package com.feige.fim.utils;

import javax.lang.model.element.Element;;
import javax.lang.model.element.TypeElement;

import javax.lang.model.util.SimpleElementVisitor8;

public class AnnotationProcessorUtils {


    
    
    
    private static final class TypeElementVisitor extends CastingElementVisitor<TypeElement> {
        private static final TypeElementVisitor INSTANCE = new TypeElementVisitor();

        TypeElementVisitor() {
            super("type element");
        }

        /**
         * 处理class和接口
         * @param e
         * @param ignore
         * @return
         */
        @Override
        public TypeElement visitType(TypeElement e, Void ignore) {
            return e;
        }
    }

    /**
     * visitType: 用于处理class和接口（TypeElement类型）
     * visitExecutable: 用于处理方法（ExecutableElement 类型）
     * visitVariable: 用于处理字段（VariableElement 类型）。
     * visitAnnotation: 用于处理注解（AnnotationMirror 类型）
     * visitPackage: 用于处理包元素（PackageElement 类型）
     * visitTypeParameter: 用于处理类型参数元素（TypeParameterElement类型）
     * @param <T>
     */
    private abstract static class CastingElementVisitor<T> extends SimpleElementVisitor8<T, Void> {
        private final String label;

        CastingElementVisitor(String label) {
            this.label = label;
        }

        /**
         * 为未覆盖的元素提供一个默认的处理方式
         * @param e
         * @param ignore
         * @return
         */
        @Override
        protected final T defaultAction(Element e, Void ignore) {
            throw new IllegalArgumentException(e + " does not represent a " + label);
        }
    }

 
}
