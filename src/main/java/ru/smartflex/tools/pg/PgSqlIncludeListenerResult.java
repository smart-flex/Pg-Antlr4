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
System.out.println("===================== func name "+ctx.identifier().getText());
        ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionListContext cntx = ctx.functionParamsDef().functionParamDefinitionList();
        if (cntx != null) {
            int order = 0;
            for (ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionContext param : cntx.functionParamDefinition()) {
                order++;
                pgParsingResult.addFuncParameter(param);

                pgParsingResult.addPart(new PgVarDefinition(param, order));
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

    public void enterVariableDefinitions(ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext ctx) {

        List<ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext> list = ctx.variableDefinition();
        for (ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext v : list) {
            pgParsingResult.addPart(new PgVarDefinition(v));
        }
    }

    public void enterPerformStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.PerformStatementContext ctx) {

        PgFuncInvoked funcInvoked = createFunctionInvoked(ctx.functionInvocation(), PgPlSqlElEnum.PERFORM_STATEMENT, ctx);
System.out.println("  > ===================== enterPerform "+ctx.functionInvocation().identifier().getText()+ " <> "+ctx);
for (PgFuncReplacementPart rp :funcInvoked.getListSubPart()) {
System.out.println("     > ===================== param "+rp.getValue());
        }
        goBack(ctx, funcInvoked);
    }

    private PgFuncInvoked createFunctionInvoked(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext ctx,
                                       PgPlSqlElEnum elementType, ParserRuleContext prc) {

        PgFuncInvoked funcInvoked = pgParsingResult.addFunctionInvocation(elementType, ctx.identifier().getText(), prc);

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

            funcInvoked.addParameter(elType, par.getText(), par);
        }

        return funcInvoked;
    }


    public void enterFunctionInvocation(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext ctx) {
System.out.println("  > ===================== enterFunctionInvocation "+ctx.identifier().getText());
        goBackToAssignStatement(ctx, ctx);
    }

    private void goBackToAssignStatement(ParserRuleContext ctx,
                                         ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext fiCtx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext) {
                int startIndex = ((ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext) child).start.getStartIndex();

System.out.println("    > ===================== asignStatement "+ctx.getText());
                PgFuncReplacementPart assignPart = pgParsingResult.getPart(startIndex, PgPlSqlElEnum.ASSIGN_STATEMENT);
                // т.к. в выражении типа assign ХП м.б. вызвана неколько раз, то assign Блок может уже быть зарегистрирован
                if (assignPart == null) {
                    assignPart = new PgFuncReplacementPart(PgPlSqlElEnum.ASSIGN_STATEMENT, ctx.getText(),
                            (ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext)child);
                    pgParsingResult.addPart(assignPart);
                }
                PgFuncInvoked funcInvoked = createFunctionInvoked(fiCtx, PgPlSqlElEnum.FUNC_INVOKE_STATEMENT, fiCtx);
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
System.out.println("     ************************ goBacks *********** ");
        for (ParseTree child : ctx.children) {
System.out.println("      > ===================== goBack child "+child.getText()+" <> "+child);
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext) {
System.out.println("   > ===================== FunctionBlockStatement ");
                goBackFunctionBlockStatementContext((ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext) child,
                        funcInvoked);
            }
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext) {
                ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext vCtx =
                        ((ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext) child).variableDefinitions();
System.out.println("   > ===================== BlockStatement "+vCtx);
                if (vCtx != null) {
                    goBackVariableDefinition(vCtx, funcInvoked);
                }

            }
        }

        for (ParseTree child : ctx.children) {
            // сначала отрабатываем BlockStatements, затем FunctionTitle - в виду приоритета обработки
            if (child instanceof ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionTitleContext) {
System.out.println("   > ===================== FunctionTitleContext ");
                goBackFunctionTitleContext((ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionTitleContext) child, funcInvoked);
            }
        }

        if (ctx.getParent() == null) {
            return;
        }
        goBack(ctx.getParent(), funcInvoked);
    }

    private void goBackFunctionTitleContext(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionTitleContext ctx,
                                            PgFuncInvoked funcInvoked) {
        ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamsDefContext ctxPd = ctx.functionParamsDef();
System.out.println("     > ===================== step1 ");
        if (ctxPd == null) {
            return;
        }
        ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionListContext ctxPdl = ctxPd.functionParamDefinitionList();
System.out.println("     > ===================== step2 ");
        if (ctxPdl == null) {
            return;
        }
        List<ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionContext> list = ctxPdl.functionParamDefinition();
System.out.println("     > ===================== step3 ");

        if (list == null || list.size() == 0) {
            return;
        }
        int funcParameterOrder = 0;
        for (ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionContext fpd : list) {
            funcParameterOrder++;
            PgPlSqlElEnum declareType = ParserHelper.defineDataType(fpd.pgTypeFull());
            String declareName = null;
            String declareNameLower = null;
            if (fpd.identifier() != null) {
                declareName = fpd.identifier().getText();
                declareNameLower = fpd.identifier().getText().toLowerCase();
            }
            handleParametersReplacement(funcInvoked, fpd, declareName, declareNameLower,
                    fpd.identifier(), fpd, declareType, PgPlSqlElEnum.FUNCTION_PARAM_DECLARE_BLOCK, funcParameterOrder);
        }
    }

    private void handleParametersReplacement(PgFuncInvoked funcInvoked, ParserRuleContext prc,
                                             String declareName, String declareNameLower,
                                             ParserRuleContext identifier, ParserRuleContext dataType,
                                             PgPlSqlElEnum declareType, PgPlSqlElEnum defPlace,
                                             int funcParameterOrder) {

        List<PgFuncReplacementPart> lp = funcInvoked.getListSubPart();
        int startIndex = prc.start.getStartIndex();
        for (PgFuncReplacementPart invokedPart : lp) {
            PgVarDefinition linkedVariable = null;
            PgPlSqlElEnum typeParam = invokedPart.getElementType();
            switch (typeParam) {
                case DECL_IDENT:
                    String parNameLower = invokedPart.getValue().toLowerCase();
                    if (parNameLower.equals(declareNameLower)) {
                        linkedVariable = (PgVarDefinition) pgParsingResult.getPart(startIndex);
                        invokedPart.setLinkedVariable(linkedVariable);
/*
                        if (abovePart == null) {
                            abovePart = new PgFuncReplacementPart(defPlace, declareName, prc);
                            abovePart.addSubPart(PgPlSqlElEnum.DECL_IDENT, declareName, identifier);
                            abovePart.addSubPart(declareType, null, dataType);
                            pgParsingResult.addPart(abovePart);
                        }
                        invokedPart.setAbovePart(abovePart);
 */
                    }
                    break;
                case DECL_ANON_PAR:
                    int order = Integer.parseInt(invokedPart.getValue().substring(1));
                    if (order == funcParameterOrder) {
                        linkedVariable = (PgVarDefinition) pgParsingResult.getPart(startIndex);
                        invokedPart.setLinkedVariable(linkedVariable);
/*
                        if (abovePart == null) {
                            abovePart = new PgFuncReplacementPart(defPlace, invokedPart.getValue(), prc);
//                            abovePart.addSubPart(PgPlSqlElEnum.DECL_ANON_PAR, declareName, identifier);
//                            abovePart.addSubPart(declareType, null, dataType);
                            pgParsingResult.addPart(abovePart);
                        }
                        invokedPart.setAbovePart(abovePart);
*/
                    }
                    break;
                default:
                    System.err.println("No handler for type: "+typeParam+" ");
                    break;
            }
//System.err.println("********* abovePart: "+abovePart);

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
            String declareName = null;
            String declareNameLower = null;
            if (vd.identifier() != null) {
                declareName = vd.identifier().getText();
                declareNameLower = vd.identifier().getText().toLowerCase();
            }
            handleParametersReplacement(funcInvoked, vd, declareName, declareNameLower,
                    vd.identifier(), vd.dataType(), declareType, PgPlSqlElEnum.VAR_DECLARE_BLOCK, 0);
        }

    }

    public void enterFunctionBlockStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionBlockStatementContext ctx) {
System.out.println("  > ===================== enterFunctionBlockStatement ");
//        int indexStart = ctx.start.getStartIndex();
//        int indexEnd = ctx.stop.getStopIndex();
        pgParsingResult.setFunctionBlockStatementIndexes(ctx);

    }

    public void enterAnonymousParameter(ru.smartflex.tools.pg.PgSQLIncludeParser.AnonymousParameterContext ctx) {
System.out.println("  > ===================== enterAnonymousParameter ");
        pgParsingResult.getFuncDefined().addPart(new PgAnonymousParameter(ctx));
    }

    public void enterAssignedStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext ctx) {
System.out.println("  > ===================== enterAssignedStatement");
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
        PgFuncReplacementPart part = new PgFuncReplacementPart(PgPlSqlElEnum.VARIABLE_USAGE, ctx.getText(), ctx);
        pgParsingResult.addPart(part);
    }

    public void enterReturnStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.ReturnStatementContext ctx) {
System.out.println("  > ===================== enterReturnStatement");
        PgFuncReplacementPart part = new PgFuncReplacementPart(PgPlSqlElEnum.RETURN_STATEMENT, ctx.getText(), ctx);
        pgParsingResult.addPart(part);
    }

}
