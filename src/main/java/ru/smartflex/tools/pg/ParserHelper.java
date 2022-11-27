package ru.smartflex.tools.pg;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ParserHelper {

    public static PgTreeNode makeTree(PgParsingResultBag pgParsingResultBag) {
        PgTreeNode root = PgTreeNode.createRoot();

        List<PgTreeNode> nodeList = new ArrayList();
        for (PgParsingResult res : pgParsingResultBag.getResultList()) {
            PgFuncDefined funcDefined = res.getFuncDefined();
            PgTreeNode node = new PgTreeNode(funcDefined);

            for (PgFuncInvoked inv : res.getFunctionInvocationsList()) {
                PgTreeNode child = new PgTreeNode(node, inv);
                node.addChild(child);
            }

            nodeList.add(node);
        }
        // sub node м.б. использован дважды , в Null его нельзя переводить
        for (int i=0; i<nodeList.size(); i++) {
            PgTreeNode node1 = nodeList.get(i);

                for (int k=0; k<nodeList.size(); k++) {
                    PgTreeNode node2 = nodeList.get(k);
                    for (PgTreeNode nd : node2.getChildList()) {
                        if (node1.equals(nd)) {
                            node1.setWasUsedAsChild(true);
                            nd.replaceNodeWithBody(node1);
                        }
                    }
                }
        }

        for (PgTreeNode nd : nodeList) {
            if (!nd.isWasUsedAsChild()) {
                root.addChild(nd);
            }
        }

        return root;
    }

/*
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
    */
}
