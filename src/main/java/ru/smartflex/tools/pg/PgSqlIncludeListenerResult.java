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
        PgFuncInvoked funcInvoked = createFunctionInvoked(ctx.functionInvocation(), PgPlSqlElEnum.PERFORM_STATEMENT,
                indexStart, indexEnd);

        goBack(ctx, funcInvoked);
    }

    private PgFuncInvoked createFunctionInvoked(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext ctx,
                                       PgPlSqlElEnum elementType, int indexStart, int indexEnd) {

        PgFuncInvoked funcInvoked = pgParsingResult.addFunctionInvocation(elementType, ctx.identifier().getText(),
                indexStart, indexEnd);

        if (ctx.functionInvocationParamList() == null) {
            return funcInvoked;
        }

        for (ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationParameterContext par : ctx.functionInvocationParamList().functionInvocationParameter()) {
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

        return funcInvoked;
    }


    public void enterFunctionInvocation(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext ctx) {
        goBackToAssignStatement(ctx, ctx);
    }

    private void goBackToAssignStatement(ParserRuleContext ctx,
                                         ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext fiCtx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext) {
                int startIndex = ((ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext) child).start.getStartIndex();

                PgFuncReplacementPart assignPart = pgParsingResult.getPart(startIndex, PgPlSqlElEnum.ASSIGN_STATEMENT);
                // т.к. в выражении типа assign ХП м.б. вызвана неколько раз, то assign Блок может уже быть зарегистрирован
                if (assignPart == null) {
                    int stopindex = ((ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext) child).stop.getStopIndex();
                    assignPart = new PgFuncReplacementPart(PgPlSqlElEnum.ASSIGN_STATEMENT, ctx.getText(), startIndex, stopindex);
                    pgParsingResult.addPart(assignPart);
                }
                int indexStart = fiCtx.start.getStartIndex();
                int indexEnd = fiCtx.stop.getStopIndex();
//                PgFuncInvoked funcInvoked = createFunctionInvoked(fiCtx, PgPlSqlElEnum.PERFORM_STATEMENT, indexStart, indexEnd);
                PgFuncInvoked funcInvoked = createFunctionInvoked(fiCtx, PgPlSqlElEnum.FUNC_INVOKE_STATEMENT, indexStart, indexEnd);
                assignPart.addSubPart(funcInvoked);

                goBack(ctx, funcInvoked);

            }
        }

        if (ctx.getParent() == null) {
            return;
        }
        goBackToAssignStatement(ctx.getParent(), fiCtx);
    }

    private void goBack(ParserRuleContext ctx, PgFuncInvoked funcInvoked) {
        for (ParseTree child : ctx.children) {
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext) {
                goBackFunctionBlockStatementContext((ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext) child,
                        funcInvoked);
            }
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext) {
                ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext vCtx =
                        ((ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext) child).variableDefinitions();
                if (vCtx != null) {
                    goBackVariableDefinition(vCtx, funcInvoked);
                }

            }
        }

        if (ctx.getParent() == null) {
            return;
        }
        goBack(ctx.getParent(), funcInvoked);
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
                if (invokedPart.getElementType() == PgPlSqlElEnum.DECL_IDENT) {
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
                } else if (invokedPart.getElementType() == PgPlSqlElEnum.DECL_CONST) {
                    // todo дописать обработку на передачу константы
                } else if (invokedPart.getElementType() == PgPlSqlElEnum.DECL_FUNC) {
                    // todo дописать обработку на передачу ф-ции

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

    public void enterAssignedStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext ctx) {
        addVariablePartFromAssignedStatement(ctx.identifier());

        if (ctx.complexExpression() != null) {
            addVariablePartFromAssignedStatement(ctx.complexExpression().identifier());

            if (ctx.complexExpression().seqOfRightPartExpression() != null) {
                List<ru.smartflex.tools.pg.PgSQLIncludeParser.RightPartExpressionContext> list =
                        ctx.complexExpression().seqOfRightPartExpression().rightPartExpression();
                for (ru.smartflex.tools.pg.PgSQLIncludeParser.RightPartExpressionContext rp : list) {
                    addVariablePartFromAssignedStatement(rp.identifier());
                }
            }
        }
    }

    private void addVariablePartFromAssignedStatement(ParserRuleContext ctx) {
        if (ctx == null) {
            return;
        }
        PgFuncReplacementPart part = new PgFuncReplacementPart(PgPlSqlElEnum.VARIABLE_USAGE, ctx.getText(),
                ctx.start.getStartIndex(), ctx.stop.getStopIndex());
        pgParsingResult.addPart(part);
    }

    public void enterReturnStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.ReturnStatementContext ctx) {
        PgFuncReplacementPart part = new PgFuncReplacementPart(PgPlSqlElEnum.RETURN_STATEMENT, ctx.getText(),
                ctx.start.getStartIndex(), ctx.stop.getStopIndex());
        pgParsingResult.addPart(part);
    }

}
