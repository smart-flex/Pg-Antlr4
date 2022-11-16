package ru.smartflex.tools.pg;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ParserHelper {

    public static PgTreeNode makeTree(PgParsingResultBag pgParsingResultBag) {
        PgTreeNode root = PgTreeNode.createRoot();
        for (PgParsingResult res : pgParsingResultBag.getResultList()) {
            PgFuncDefined funcDefined = res.getFuncDefined();
            PgTreeNode node = new PgTreeNode(funcDefined);

            for (PgFuncInvoked inv : res.getFunctionInvocationsList()) {
                PgTreeNode child = new PgTreeNode(node, inv);
                node.addChild(child);
            }

            root.putInPlaceNode(node);
        }
        root.packTree();

        return root;
    }

    static PgFuncBodyPartBag.FuncBodyPart getLastFuncBodyPart(PgTreeNode node) {
        PgFuncDefined funcDefined = node.getParentNode().getFuncDefined();

        if (funcDefined == null) {
            // это второй и более уровни вложенности.
            // TODO Надо найти и сопоставить определение ХП с учетом полиморфизма
            return null; // so far
        }

        return funcDefined.getFuncBodyPartBag().getLastFuncBodyPart();
    }

    static void makeNewBodyPart(PgFuncInvoked funcInvoked,  PgTreeNode node) {
        PgFuncDefined funcDefined = node.getParentNode().getFuncDefined();
        PgFuncBodyPartBag.FuncBodyPart lastBodyPart = ParserHelper.getLastFuncBodyPart(node);

        int rowOffsetBody = lastBodyPart.getRowOffsetBody();
        int lineStart = funcInvoked.getLineStart() - rowOffsetBody;
        int lineEnd = funcInvoked.getLineEnd() - rowOffsetBody;
//todo насытить p02_void_perform комментами-ловушками // -- и пр.
        Reader reader = new StringReader(lastBodyPart.getFuncPart());
        Stream<String> lines = new BufferedReader(reader).lines();
        AtomicInteger idLine = new AtomicInteger(0);
        lines.forEach(line -> {

            idLine.incrementAndGet();
        });




    }
}
