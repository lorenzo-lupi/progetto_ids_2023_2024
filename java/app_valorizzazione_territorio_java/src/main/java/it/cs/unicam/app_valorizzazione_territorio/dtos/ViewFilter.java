package it.cs.unicam.app_valorizzazione_territorio.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class ViewFilter extends SimpleBeanPropertyFilter {

    private final Class<?> activeView;

    public ViewFilter(Class<?> activeView) {
        this.activeView = activeView;
    }

    @Override
    protected boolean include(BeanPropertyWriter writer) {
        JsonView annotation = writer.getAnnotation(JsonView.class);
        return annotation == null || isViewAllowed(annotation.value());
    }

    @Override
    protected boolean include(PropertyWriter writer) {
        JsonView annotation = writer.getAnnotation(JsonView.class);
        return annotation == null || isViewAllowed(annotation.value());
    }

    private boolean isViewAllowed(Class<?>[] views) {
        if (views == null || views.length == 0) {
            return true; // No view specified, include by default
        }
        for (Class<?> view : views) {
            if (activeView == view) {
                return true; // Current view matches, include
            }
        }
        return false; // Current view does not match
    }
}
