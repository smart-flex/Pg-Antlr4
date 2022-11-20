package ru.smartflex.tools.pg;

@FunctionalInterface
public interface ITreeHandler<PgTreeNodeWalker> {
    void apply(ru.smartflex.tools.pg.PgTreeNodeWalker a);

}
