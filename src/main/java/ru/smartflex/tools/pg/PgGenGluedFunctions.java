package ru.smartflex.tools.pg;

public class PgGenGluedFunctions {

    private PgParsingResultBag pgParsingResultBag;
    private PgTreeNode root;

    PgGenGluedFunctions(PgParsingResultBag pgParsingResultBag, PgTreeNode root) {
        this.pgParsingResultBag = pgParsingResultBag;
        this.root = root;
    }

    private void gen() {


    }

    private ITreeHandler getITreeHandler() {
        //создание разрывной структуры на первом проходе для последующей вставки тел ф-ций на втором проходе
        ITreeHandler<PgTreeNodeWalker> ith = (a) -> {
            if (((PgTreeNode.PrintTreeBag) a.getInfo()).indexNested == 0) {
                System.out.println(String.format("%" + ((PgTreeNode.PrintTreeBag) a.getInfo()).depth + "s", a.getNode().getPgFuncName()));
            } else {
                System.out.println(String.format("%" + ((PgTreeNode.PrintTreeBag) a.getInfo()).depth + "s",
                        ((PgTreeNode.PrintTreeBag) a.getInfo()).indexNested) + ": " + a.getNode().getPgFuncName());
            }
            return;
        };

        return ith;
    }
}
