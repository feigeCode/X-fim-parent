package com.feige.utils.order;


import java.util.Comparator;

public class OrderClassComparator implements Comparator<Class<?>> {
    
    private static final OrderClassComparator instance = new OrderClassComparator();
    
    public static OrderClassComparator getInstance(){
        return  instance;
    }
    
    @Override
    public int compare(Class<?> o1, Class<?> o2) {
        Order order1 = o1.getAnnotation(Order.class);
        Order order2 = o2.getAnnotation(Order.class);
        int val1 = order1 == null ? 0 : order1.value();
        int val2 = order2 == null ? 0 : order2.value();
        return Integer.compare(val1, val2);
    }
}
