package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;

public class TransferParameters {
    List<PgVarDefinition> listPars = new ArrayList<>();

    void add(PgVarDefinition var) {
        listPars.add(var);
    }

    PgVarDefinition getVarByOrder(int order) {
        order--;
        return listPars.get(order);
    }

}
