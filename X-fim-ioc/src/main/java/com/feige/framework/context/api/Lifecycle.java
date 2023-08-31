package com.feige.framework.context.api;

public interface Lifecycle extends Disposable {

    /**
     * Initialize the component before {@link #start() start}
     *
     * @return current {@link Lifecycle}
     * @throws IllegalStateException
     */
    void initialize() throws IllegalStateException;

    /**
     * Start the component
     *
     * @return current {@link Lifecycle}
     * @throws IllegalStateException
     */
    void start() throws IllegalStateException;

    /**
     * Destroy the component
     *
     * @throws IllegalStateException
     */
    @Override
    void destroy() throws IllegalStateException;
}
