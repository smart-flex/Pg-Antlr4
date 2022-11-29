package ru.smartflex.tools.pg;

@FunctionalInterface
public interface ITreeHandler<T> {
    void apply(T t);

}
