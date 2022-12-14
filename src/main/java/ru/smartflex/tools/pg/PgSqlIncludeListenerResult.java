package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.ParserRuleContext;
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


    public void enterPerformStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.PerformStatementContext ctx) {

        int indexStart = ctx.start.getStartIndex();
        int indexEnd = ctx.stop.getStopIndex();
        PgFuncInvoked funcInvoked = pgParsingResult.addFunctionInvocationsName(ctx.functionInvocation().identifier().getText(),
                indexStart, indexEnd);

        if (ctx.functionInvocation().functionInvocationParamList() == null) {
            return;
        }

        for (ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationParameterContext par : ctx.functionInvocation().functionInvocationParamList().functionInvocationParameter()) {
            funcInvoked.addParameter(par.getText(), par.start.getStartIndex(), par.stop.getStopIndex());
        }

        // todo идем вверх до declare или до параметров
        // не идем вверх если в параметрах константа
        // todo оберунть один из вызовов ХП в DECLARE begin end;

        goBack(ctx);

    }

    private void goBack(ParserRuleContext ctx) {
        boolean toBack = true;
        for (ParseTree child : ctx.children) {
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext) {
                goBackFunctionBlockStatementContext((ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext) child);
            }
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext) {

            }
        }

        if (toBack) {
            if (ctx.getParent() == null) {
                return;
            }
            goBack(ctx.getParent());
        }
    }

    private void goBackFunctionBlockStatementContext(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext ctx) {
        ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext vCtx = ctx.variableDefinitions();
        if (vCtx == null) {
            return;
        }

        List<ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext> list = vCtx.variableDefinition();
        for (ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext vd : list) {
            System.out.println("+++++++++++++ "+vd.identifier().getText());
        }
    }

    public void enterFunctionBlockStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext ctx) {
        int indexStart = ctx.start.getStartIndex();
        int indexEnd = ctx.stop.getStopIndex();
        pgParsingResult.setFunctionBlockStatementIndexes(indexStart, indexEnd);
    }

}
