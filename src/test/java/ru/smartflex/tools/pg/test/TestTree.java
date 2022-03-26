package ru.smartflex.tools.pg.test;

import org.junit.Test;
import ru.smartflex.tools.pg.PgTreeNode;

public class TestTree {

    @Test
    public void testMakeTree() {
        PgTreeNode root = PgTreeNode.createRoot();

        PgTreeNode func1_level1 = new PgTreeNode("func1_level1");
        PgTreeNode func2_level2 = new PgTreeNode("func2_level2");
        PgTreeNode func3_level2 = new PgTreeNode("func3_level2");
        func1_level1.addChild((func2_level2));
        func1_level1.addChild((func3_level2));

        root.putInPlaceNode(func1_level1);

        root.drawTree();

        // TODO make tree
    }

}
