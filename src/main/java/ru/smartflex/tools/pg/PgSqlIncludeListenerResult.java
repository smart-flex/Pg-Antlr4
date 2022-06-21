package ru.smartflex.tools.pg;

public class PgSqlIncludeListenerResult extends PgSqlIncludeListener {

    private PgParsingResult pgParsingResult;

    public PgSqlIncludeListenerResult(PgParsingResult pgParsingResult) {
        this.pgParsingResult = pgParsingResult;
    }

    public void enterFunctionTitle(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionTitleContext ctx) {

        pgParsingResult.setFunctionName(ctx.identifier().getText());

        ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionListContext cntx = ctx.functionParamsDef().functionParamDefinitionList();
        if (cntx != null) {
            for (ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionContext param : cntx.functionParamDefinition()) {
                ru.smartflex.tools.pg.PgSQLIncludeParser.IdentifierContext ident = param.identifier();
                ru.smartflex.tools.pg.PgSQLIncludeParser.PgTypeEnumContext pgType = param.pgTypeEnum();

            }

        }

    }

    public void enterFunctionInvocation(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext ctx) {
        System.out.println("+++ " + ctx.identifier().getText());
        pgParsingResult.addElementToFunctionInvocationsList(ctx.identifier().getText());
    }

    public void enterFunctionParamList(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamListContext ctx) {
        System.out.println("+++ " + ctx);

    }


}
