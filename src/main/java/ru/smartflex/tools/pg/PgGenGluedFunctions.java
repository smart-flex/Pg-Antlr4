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
        //        если у ноды есть PgDefined То это 1 уовень, берем тело и пишем в старт
        //если у ноды нет PgDefined но есть дети, то ищем а баге его PgDefined
        //если у ноды нет детей а есть парент-кусок-body - то определяем хвот и пишем его (для следующих  детей)
        //и СОХРАЯНЕМ всю попутную информацию для последующей генерации
        ITreeHandler<PgTreeNodeWalker> ith = (w) -> {
            PgFuncInvoked funcInvoked = w.getNode().getFuncInvoked();
            if (funcInvoked != null) {
                // это chield, формируем хвост
                //надо дописать 9проработать) первначальное наполнение bodypart
                //                        и дальнейше наполнение шаг за шагом (уменьшение хвоста)
                //
                PgFuncBodyPartBag.FuncBodyPart lastBodyPart = ParserHelper.getLastFuncBodyPart(w.getNode());

            }


            return;
        };

        return ith;
    }
}
