package com.mz.example.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class MappingFuture<SOURCE, TARGET> implements Future<TARGET>{

    private Future<SOURCE> future;
    private Function<SOURCE, TARGET> mapper;

    public static <S, T> MappingFuture<S, T> create(Future<S> future, Function<S, T> mapper) {
        return new MappingFuture<>(future, mapper);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public TARGET get() throws InterruptedException, ExecutionException {
        SOURCE source = future.get();
        return mapper.apply(source);
    }

    @Override
    public TARGET get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        SOURCE source = future.get(timeout, unit);
        return mapper.apply(source);
    }
}
