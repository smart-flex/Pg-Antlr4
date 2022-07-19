package ru.smartflex.tools.pg;

@FunctionalInterface
public interface ITreeHandler<A> {
    void apply(A a);
}
