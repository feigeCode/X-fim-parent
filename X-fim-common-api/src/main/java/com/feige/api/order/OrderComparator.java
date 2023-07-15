package com.feige.api.order;

import com.feige.api.annotation.Order;

import java.util.Comparator;

public class OrderComparator<T> implements Comparator<T> {
    
    private static final OrderComparator<?> instance = new OrderComparator<>();
    
    public static <T> OrderComparator<T> getInstance(){
        return (OrderComparator<T>) instance;
    }
    
    @Override
    public int compare(T o1, T o2) {
        Order order1 = o1.getClass().getAnnotation(Order.class);
        Order order2 = o2.getClass().getAnnotation(Order.class);
        int val1 = order1 == null ? 0 : order1.value();
        int val2 = order2 == null ? 0 : order2.value();
        return Integer.compare(val1, val2);
    }
}
