package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

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
                ru.smartflex.tools.pg.PgSQLIncludeParser.ArgModeContext argMode = param.argMode();
                ru.smartflex.tools.pg.PgSQLIncludeParser.IdentifierContext ident = param.identifier();
                ru.smartflex.tools.pg.PgSQLIncludeParser.PgTypeEnumContext pgType = param.pgTypeEnum();

                pgParsingResult.addFuncParameter(
                        (argMode != null ? argMode.getText() : null),
                        (ident != null ? ident.getText() : null),
                        (pgType != null ? pgType.getText() : null)
                );
            }
        }

        if (ctx.functionReturns() != null) {

            // USUAL
            if (ctx.functionReturns().functionReturnsUsualType() != null) {
                String dataTypeName = null;
                if (ctx.functionReturns().functionReturnsUsualType().pgTypeEnum() != null) {
                    dataTypeName = ctx.functionReturns().functionReturnsUsualType().pgTypeEnum().getText();
                } else if (ctx.functionReturns().functionReturnsUsualType().tableRefColumnType() != null) {
                    dataTypeName = ctx.functionReturns().functionReturnsUsualType().tableRefColumnType().getText();
                }
                pgParsingResult.setFuncReturnUsual(dataTypeName);
            }

            // TABLE
            if (ctx.functionReturns().functionReturnsTable() != null) {
                List<ru.smartflex.tools.pg.PgSQLIncludeParser.TableParamDefinitionContext> list = ctx.functionReturns().functionReturnsTable().tableParams().tableParamDefinitionList().tableParamDefinition();
                for (ru.smartflex.tools.pg.PgSQLIncludeParser.TableParamDefinitionContext par : list) {
                    String parName = par.identifier().getText();
                    String parType = par.pgTypeEnum().getText();
                    pgParsingResult.addFuncReturnTableParameter(parName, parType);
                }
            }

            // REFCURSOR
            if (ctx.functionReturns().functionReturnsRefcursor() != null) {
                pgParsingResult.setFuncReturnRefcursor();
            }

            // SETOF
            if (ctx.functionReturns().functionReturnsSetOf() != null) {
                String dataTypeName = ctx.functionReturns().functionReturnsSetOf().identifier().getText();
                pgParsingResult.setFuncReturnSetOf(dataTypeName);
            }
        }

    }

    @Override
    public void enterFunctionInvocation(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext ctx) {

    }

    @Override
    public void enterPerformStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.PerformStatementContext ctx) {

        int indexStart = ctx.start.getStartIndex();
        int indexEnd = ctx.stop.getStopIndex();
        PgFuncInvoked funcInvoked = pgParsingResult.addFunctionInvocationsName(ctx.functionInvocation().identifier().getText(),
                indexStart, indexEnd);

        if (ctx.functionInvocation().functionInvocationParamList() == null) {
            return;
        }

        for (ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationParameterContext par : ctx.functionInvocation().functionInvocationParamList().functionInvocationParameter()) {
            funcInvoked.addParameter(par.getText());
        }

    }

}
