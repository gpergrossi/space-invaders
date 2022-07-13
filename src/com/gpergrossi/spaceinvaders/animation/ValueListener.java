package com.gpergrossi.spaceinvaders.animation;

@FunctionalInterface
public interface ValueListener<T> {

    void onChange(T newValue);

}
