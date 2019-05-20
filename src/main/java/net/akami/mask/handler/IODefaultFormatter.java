package net.akami.mask.handler;

public interface IODefaultFormatter<T> {

    T inFormat(T input);
    T outFormat(T input);
}
