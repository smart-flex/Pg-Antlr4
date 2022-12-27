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
                pgParsingResult.addFuncParameter(param);
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
            //фиксируем типы параметров через enum
            PgPlSqlElEnum elType = null;
            if (par.identifier() != null) {
                elType = PgPlSqlElEnum.DECL_IDENT;
            } else if (par.constantExpression() != null) {
                elType = PgPlSqlElEnum.DECL_CONST;
            } else if (par.anonymousParameter() != null) {
                elType = PgPlSqlElEnum.DECL_ANON_PAR;
            } else if (par.refExpression() != null) {
                elType = PgPlSqlElEnum.DECL_REF_EXPR;
            } else if (par.functionInvocation() != null) {
                elType = PgPlSqlElEnum.DECL_FUNC;
            }

            funcInvoked.addParameter(elType, par.getText(), par.start.getStartIndex(), par.stop.getStopIndex());
        }

        // todo идем вверх до declare или до параметров
        // не идем вверх если в параметрах константа
        // todo оберунть один из вызовов ХП в DECLARE begin end;

        goBack(ctx, funcInvoked);

    }

    private void goBack(ParserRuleContext ctx, PgFuncInvoked funcInvoked) {
        boolean toBack = true;
        for (ParseTree child : ctx.children) {
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext) {
                goBackFunctionBlockStatementContext((ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext) child,
                        funcInvoked);
            }
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext) {
                ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext vCtx =
                        ((ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext)child).variableDefinitions();
                if (vCtx != null) {
                    goBackVariableDefinition(vCtx, funcInvoked);
                }

            }
        }

        if (toBack) {
            if (ctx.getParent() == null) {
                return;
            }
            goBack(ctx.getParent(), funcInvoked);
        }
    }

    private void goBackFunctionBlockStatementContext(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext ctx,
                                                     PgFuncInvoked funcInvoked) {
        ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext vCtx = ctx.variableDefinitions();
        if (vCtx == null) {
            return;
        }

        goBackVariableDefinition(vCtx, funcInvoked);
    }

    private void goBackVariableDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext vCtx,
                                          PgFuncInvoked funcInvoked) {
        List<ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext> list = vCtx.variableDefinition();

        for (ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext vd : list) {
            PgPlSqlElEnum declareType = ParserHelper.defineDataType(vd.dataType());
            String declareName = vd.identifier().getText();
            String declareNameLower = vd.identifier().getText().toLowerCase();
            List<PgFuncReplacementPart> lp = funcInvoked.getListSubPart();
            int startIndex = vd.start.getStartIndex();
            int stopindex = vd.stop.getStopIndex();
            for (PgFuncReplacementPart invokedPart : lp) {
                if (invokedPart.getElementType() == PgPlSqlElEnum.PERFORM) {
                    if (invokedPart.getElementSubType() == PgPlSqlElEnum.DECL_IDENT) {
                        String parNameLower = invokedPart.getValue().toLowerCase();
                        if (parNameLower.equals(declareNameLower)) {
                            PgFuncReplacementPart abovePart = pgParsingResult.getPart(startIndex);
                            if (abovePart == null) {
                                abovePart = new PgFuncReplacementPart(PgPlSqlElEnum.VAR_DECLARE_BLOCK, declareName, startIndex, stopindex);
                                abovePart.addSubPart(PgPlSqlElEnum.DECL_IDENT, declareName,
                                        vd.identifier().start.getStartIndex(), vd.identifier().stop.getStopIndex());
                                abovePart.addSubPart(declareType, null,
                                        vd.dataType().start.getStartIndex(), vd.dataType().stop.getStopIndex());
                                pgParsingResult.addPart(abovePart);
                            }
                            invokedPart.setAbovePart(abovePart);
                        }
                    }

                } else {
                    // TODO дописать обработку для anonymousParameter и refExpression
                    throw new PgSQLQjuException("There is no parameter handler for: " + invokedPart.getElementType());
                }
            }
        }

    }

    public void enterFunctionBlockStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext ctx) {
        int indexStart = ctx.start.getStartIndex();
        int indexEnd = ctx.stop.getStopIndex();
        pgParsingResult.setFunctionBlockStatementIndexes(indexStart, indexEnd);
    }

    public void enterAnonymousParameter(ru.smartflex.tools.pg.PgSQLIncludeParser.AnonymousParameterContext ctx) {
        pgParsingResult.getFuncDefined().addPart(new PgAnonymousParameter(ctx));
    }

}
