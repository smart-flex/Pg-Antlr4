package ru.smartflex.tools.pg;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public abstract class PgSqlIncludeListener implements ru.smartflex.tools.pg.PgSQLIncludeParserListener {

    @Override
    public void enterFunctionDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionDefinitionContext ctx) {

    }

    @Override
    public void exitFunctionDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionDefinitionContext ctx) {

    }

    @Override
    public abstract void enterFunctionTitle(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionTitleContext ctx);

    @Override
    public void exitFunctionTitle(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionTitleContext ctx) {

    }

    @Override
    public void enterIdentifier(ru.smartflex.tools.pg.PgSQLIncludeParser.IdentifierContext ctx) {

    }

    @Override
    public void exitIdentifier(ru.smartflex.tools.pg.PgSQLIncludeParser.IdentifierContext ctx) {

    }

    @Override
    public void enterSeqOfStatements(ru.smartflex.tools.pg.PgSQLIncludeParser.SeqOfStatementsContext ctx) {

    }

    @Override
    public void exitSeqOfStatements(ru.smartflex.tools.pg.PgSQLIncludeParser.SeqOfStatementsContext ctx) {

    }

    @Override
    public void enterStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.StatementContext ctx) {

    }

    @Override
    public void exitStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.StatementContext ctx) {

    }

    @Override
    public void enterNullStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.NullStatementContext ctx) {

    }

    @Override
    public void exitNullStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.NullStatementContext ctx) {

    }

    @Override
    public void enterPerformStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.PerformStatementContext ctx) {

    }

    @Override
    public void exitPerformStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.PerformStatementContext ctx) {

    }

    @Override
    public void enterExecuteStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.ExecuteStatementContext ctx) {

    }

    @Override
    public void exitExecuteStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.ExecuteStatementContext ctx) {

    }

    @Override
    public void enterReturnStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.ReturnStatementContext ctx) {

    }

    @Override
    public void exitReturnStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.ReturnStatementContext ctx) {

    }

    @Override
    public abstract void enterFunctionInvocation(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext ctx);

    @Override
    public void exitFunctionInvocation(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionInvocationContext ctx) {

    }

    @Override
    public abstract void enterFunctionParamList(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamListContext ctx);

    @Override
    public void exitFunctionParamList(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamListContext ctx) {

    }

    @Override
    public void enterFunctionCreateDef(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionCreateDefContext ctx) {

    }

    @Override
    public void exitFunctionCreateDef(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionCreateDefContext ctx) {

    }

    @Override
    public void enterFunctionReturns(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionReturnsContext ctx) {

    }

    @Override
    public void exitFunctionReturns(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionReturnsContext ctx) {

    }

    @Override
    public void enterTableParams(ru.smartflex.tools.pg.PgSQLIncludeParser.TableParamsContext ctx) {

    }

    @Override
    public void exitTableParams(ru.smartflex.tools.pg.PgSQLIncludeParser.TableParamsContext ctx) {

    }

    @Override
    public void enterTableParamDefinitionList(ru.smartflex.tools.pg.PgSQLIncludeParser.TableParamDefinitionListContext ctx) {

    }

    @Override
    public void exitTableParamDefinitionList(ru.smartflex.tools.pg.PgSQLIncludeParser.TableParamDefinitionListContext ctx) {

    }

    @Override
    public void enterTableParamDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.TableParamDefinitionContext ctx) {

    }

    @Override
    public void exitTableParamDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.TableParamDefinitionContext ctx) {

    }

    @Override
    public void enterFunctionParamsDef(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamsDefContext ctx) {

    }

    @Override
    public void exitFunctionParamsDef(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamsDefContext ctx) {

    }

    @Override
    public void enterFunctionParamDefinitionList(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionListContext ctx) {

    }

    @Override
    public void exitFunctionParamDefinitionList(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionListContext ctx) {

    }

    @Override
    public void enterFunctionParamDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionContext ctx) {

    }

    @Override
    public void exitFunctionParamDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionContext ctx) {

    }

    @Override
    public void enterSeqOfFunctionAttributes(ru.smartflex.tools.pg.PgSQLIncludeParser.SeqOfFunctionAttributesContext ctx) {

    }

    @Override
    public void exitSeqOfFunctionAttributes(ru.smartflex.tools.pg.PgSQLIncludeParser.SeqOfFunctionAttributesContext ctx) {

    }

    @Override
    public void enterFunctionAttribute(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionAttributeContext ctx) {

    }

    @Override
    public void exitFunctionAttribute(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionAttributeContext ctx) {

    }

    @Override
    public void enterIfDef(ru.smartflex.tools.pg.PgSQLIncludeParser.IfDefContext ctx) {

    }

    @Override
    public void exitIfDef(ru.smartflex.tools.pg.PgSQLIncludeParser.IfDefContext ctx) {

    }

    @Override
    public void enterCaseDef(ru.smartflex.tools.pg.PgSQLIncludeParser.CaseDefContext ctx) {

    }

    @Override
    public void exitCaseDef(ru.smartflex.tools.pg.PgSQLIncludeParser.CaseDefContext ctx) {

    }

    @Override
    public void enterExitDef(ru.smartflex.tools.pg.PgSQLIncludeParser.ExitDefContext ctx) {

    }

    @Override
    public void exitExitDef(ru.smartflex.tools.pg.PgSQLIncludeParser.ExitDefContext ctx) {

    }

    @Override
    public void enterLoopDef(ru.smartflex.tools.pg.PgSQLIncludeParser.LoopDefContext ctx) {

    }

    @Override
    public void exitLoopDef(ru.smartflex.tools.pg.PgSQLIncludeParser.LoopDefContext ctx) {

    }

    @Override
    public void enterLoopDefQuery(ru.smartflex.tools.pg.PgSQLIncludeParser.LoopDefQueryContext ctx) {

    }

    @Override
    public void exitLoopDefQuery(ru.smartflex.tools.pg.PgSQLIncludeParser.LoopDefQueryContext ctx) {

    }

    @Override
    public void enterLoopDefExecute(ru.smartflex.tools.pg.PgSQLIncludeParser.LoopDefExecuteContext ctx) {

    }

    @Override
    public void exitLoopDefExecute(ru.smartflex.tools.pg.PgSQLIncludeParser.LoopDefExecuteContext ctx) {

    }

    @Override
    public void enterBlockStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext ctx) {

    }

    @Override
    public void exitBlockStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.BlockStatementContext ctx) {

    }

    @Override
    public void enterTrappingErrorList(ru.smartflex.tools.pg.PgSQLIncludeParser.TrappingErrorListContext ctx) {

    }

    @Override
    public void exitTrappingErrorList(ru.smartflex.tools.pg.PgSQLIncludeParser.TrappingErrorListContext ctx) {

    }

    @Override
    public void enterTrappingError(ru.smartflex.tools.pg.PgSQLIncludeParser.TrappingErrorContext ctx) {

    }

    @Override
    public void exitTrappingError(ru.smartflex.tools.pg.PgSQLIncludeParser.TrappingErrorContext ctx) {

    }

    @Override
    public void enterComplexExpressionList(ru.smartflex.tools.pg.PgSQLIncludeParser.ComplexExpressionListContext ctx) {

    }

    @Override
    public void exitComplexExpressionList(ru.smartflex.tools.pg.PgSQLIncludeParser.ComplexExpressionListContext ctx) {

    }

    @Override
    public void enterAssignedStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext ctx) {

    }

    @Override
    public void exitAssignedStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.AssignedStatementContext ctx) {

    }

    @Override
    public void enterComplexExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.ComplexExpressionContext ctx) {

    }

    @Override
    public void exitComplexExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.ComplexExpressionContext ctx) {

    }

    @Override
    public void enterRefExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.RefExpressionContext ctx) {

    }

    @Override
    public void exitRefExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.RefExpressionContext ctx) {

    }

    @Override
    public void enterSeqOfRightPartExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.SeqOfRightPartExpressionContext ctx) {

    }

    @Override
    public void exitSeqOfRightPartExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.SeqOfRightPartExpressionContext ctx) {

    }

    @Override
    public void enterRightPartExpressionList(ru.smartflex.tools.pg.PgSQLIncludeParser.RightPartExpressionListContext ctx) {

    }

    @Override
    public void exitRightPartExpressionList(ru.smartflex.tools.pg.PgSQLIncludeParser.RightPartExpressionListContext ctx) {

    }

    @Override
    public void enterRightPartExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.RightPartExpressionContext ctx) {

    }

    @Override
    public void exitRightPartExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.RightPartExpressionContext ctx) {

    }

    @Override
    public void enterVariableDefinitions(ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext ctx) {

    }

    @Override
    public void exitVariableDefinitions(ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionsContext ctx) {

    }

    @Override
    public void enterVariableDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext ctx) {

    }

    @Override
    public void exitVariableDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext ctx) {

    }

    @Override
    public void enterDataType(ru.smartflex.tools.pg.PgSQLIncludeParser.DataTypeContext ctx) {

    }

    @Override
    public void exitDataType(ru.smartflex.tools.pg.PgSQLIncludeParser.DataTypeContext ctx) {

    }

    @Override
    public void enterBooleanType(ru.smartflex.tools.pg.PgSQLIncludeParser.BooleanTypeContext ctx) {

    }

    @Override
    public void exitBooleanType(ru.smartflex.tools.pg.PgSQLIncludeParser.BooleanTypeContext ctx) {

    }

    @Override
    public void enterUsualType(ru.smartflex.tools.pg.PgSQLIncludeParser.UsualTypeContext ctx) {

    }

    @Override
    public void exitUsualType(ru.smartflex.tools.pg.PgSQLIncludeParser.UsualTypeContext ctx) {

    }

    @Override
    public void enterPrecisionClause(ru.smartflex.tools.pg.PgSQLIncludeParser.PrecisionClauseContext ctx) {

    }

    @Override
    public void exitPrecisionClause(ru.smartflex.tools.pg.PgSQLIncludeParser.PrecisionClauseContext ctx) {

    }

    @Override
    public void enterCursorType(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorTypeContext ctx) {

    }

    @Override
    public void exitCursorType(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorTypeContext ctx) {

    }

    @Override
    public void enterCursorOpenStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorOpenStatementContext ctx) {

    }

    @Override
    public void exitCursorOpenStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorOpenStatementContext ctx) {

    }

    @Override
    public void enterCursorCloseStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorCloseStatementContext ctx) {

    }

    @Override
    public void exitCursorCloseStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorCloseStatementContext ctx) {

    }

    @Override
    public void enterFetchStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.FetchStatementContext ctx) {

    }

    @Override
    public void exitFetchStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.FetchStatementContext ctx) {

    }

    @Override
    public void enterMoveStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.MoveStatementContext ctx) {

    }

    @Override
    public void exitMoveStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.MoveStatementContext ctx) {

    }

    @Override
    public void enterSelectStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.SelectStatementContext ctx) {

    }

    @Override
    public void exitSelectStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.SelectStatementContext ctx) {

    }

    @Override
    public void enterUpdateStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.UpdateStatementContext ctx) {

    }

    @Override
    public void exitUpdateStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.UpdateStatementContext ctx) {

    }

    @Override
    public void enterInlineQuery(ru.smartflex.tools.pg.PgSQLIncludeParser.InlineQueryContext ctx) {

    }

    @Override
    public void exitInlineQuery(ru.smartflex.tools.pg.PgSQLIncludeParser.InlineQueryContext ctx) {

    }

    @Override
    public void enterQueryColClauseList(ru.smartflex.tools.pg.PgSQLIncludeParser.QueryColClauseListContext ctx) {

    }

    @Override
    public void exitQueryColClauseList(ru.smartflex.tools.pg.PgSQLIncludeParser.QueryColClauseListContext ctx) {

    }

    @Override
    public void enterQueryColClause(ru.smartflex.tools.pg.PgSQLIncludeParser.QueryColClauseContext ctx) {

    }

    @Override
    public void exitQueryColClause(ru.smartflex.tools.pg.PgSQLIncludeParser.QueryColClauseContext ctx) {

    }

    @Override
    public void enterQueryColumnAs(ru.smartflex.tools.pg.PgSQLIncludeParser.QueryColumnAsContext ctx) {

    }

    @Override
    public void exitQueryColumnAs(ru.smartflex.tools.pg.PgSQLIncludeParser.QueryColumnAsContext ctx) {

    }

    @Override
    public void enterCastClause(ru.smartflex.tools.pg.PgSQLIncludeParser.CastClauseContext ctx) {

    }

    @Override
    public void exitCastClause(ru.smartflex.tools.pg.PgSQLIncludeParser.CastClauseContext ctx) {

    }

    @Override
    public void enterIntoList(ru.smartflex.tools.pg.PgSQLIncludeParser.IntoListContext ctx) {

    }

    @Override
    public void exitIntoList(ru.smartflex.tools.pg.PgSQLIncludeParser.IntoListContext ctx) {

    }

    @Override
    public void enterCursorParamList(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamListContext ctx) {

    }

    @Override
    public void exitCursorParamList(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamListContext ctx) {

    }

    @Override
    public void enterCursorParam(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamContext ctx) {

    }

    @Override
    public void exitCursorParam(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamContext ctx) {

    }

    @Override
    public void enterCursorParamsDef(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamsDefContext ctx) {

    }

    @Override
    public void exitCursorParamsDef(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamsDefContext ctx) {

    }

    @Override
    public void enterCursorParamDefinitionList(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamDefinitionListContext ctx) {

    }

    @Override
    public void exitCursorParamDefinitionList(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamDefinitionListContext ctx) {

    }

    @Override
    public void enterCursorParamDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamDefinitionContext ctx) {

    }

    @Override
    public void exitCursorParamDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.CursorParamDefinitionContext ctx) {

    }

    @Override
    public void enterPgTypeEnum(ru.smartflex.tools.pg.PgSQLIncludeParser.PgTypeEnumContext ctx) {

    }

    @Override
    public void exitPgTypeEnum(ru.smartflex.tools.pg.PgSQLIncludeParser.PgTypeEnumContext ctx) {

    }

    @Override
    public void enterConstantExpressionList(ru.smartflex.tools.pg.PgSQLIncludeParser.ConstantExpressionListContext ctx) {

    }

    @Override
    public void exitConstantExpressionList(ru.smartflex.tools.pg.PgSQLIncludeParser.ConstantExpressionListContext ctx) {

    }

    @Override
    public void enterConstantExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.ConstantExpressionContext ctx) {

    }

    @Override
    public void exitConstantExpression(ru.smartflex.tools.pg.PgSQLIncludeParser.ConstantExpressionContext ctx) {

    }

    @Override
    public void enterBitString(ru.smartflex.tools.pg.PgSQLIncludeParser.BitStringContext ctx) {

    }

    @Override
    public void exitBitString(ru.smartflex.tools.pg.PgSQLIncludeParser.BitStringContext ctx) {

    }

    @Override
    public void enterEscapeString(ru.smartflex.tools.pg.PgSQLIncludeParser.EscapeStringContext ctx) {

    }

    @Override
    public void exitEscapeString(ru.smartflex.tools.pg.PgSQLIncludeParser.EscapeStringContext ctx) {

    }

    @Override
    public void enterString(ru.smartflex.tools.pg.PgSQLIncludeParser.StringContext ctx) {

    }

    @Override
    public void exitString(ru.smartflex.tools.pg.PgSQLIncludeParser.StringContext ctx) {

    }

    @Override
    public void enterRealValue(ru.smartflex.tools.pg.PgSQLIncludeParser.RealValueContext ctx) {

    }

    @Override
    public void exitRealValue(ru.smartflex.tools.pg.PgSQLIncludeParser.RealValueContext ctx) {

    }

    @Override
    public void enterIntValue(ru.smartflex.tools.pg.PgSQLIncludeParser.IntValueContext ctx) {

    }

    @Override
    public void exitIntValue(ru.smartflex.tools.pg.PgSQLIncludeParser.IntValueContext ctx) {

    }

    @Override
    public void enterSign(ru.smartflex.tools.pg.PgSQLIncludeParser.SignContext ctx) {

    }

    @Override
    public void exitSign(ru.smartflex.tools.pg.PgSQLIncludeParser.SignContext ctx) {

    }

    @Override
    public void enterAnonymousParameter(ru.smartflex.tools.pg.PgSQLIncludeParser.AnonymousParameterContext ctx) {

    }

    @Override
    public void exitAnonymousParameter(ru.smartflex.tools.pg.PgSQLIncludeParser.AnonymousParameterContext ctx) {

    }

    @Override
    public void enterOperators(ru.smartflex.tools.pg.PgSQLIncludeParser.OperatorsContext ctx) {

    }

    @Override
    public void exitOperators(ru.smartflex.tools.pg.PgSQLIncludeParser.OperatorsContext ctx) {

    }

    @Override
    public void enterRaiseStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.RaiseStatementContext ctx) {

    }

    @Override
    public void exitRaiseStatement(ru.smartflex.tools.pg.PgSQLIncludeParser.RaiseStatementContext ctx) {

    }

    @Override
    public void enterLabelClause(ru.smartflex.tools.pg.PgSQLIncludeParser.LabelClauseContext ctx) {

    }

    @Override
    public void exitLabelClause(ru.smartflex.tools.pg.PgSQLIncludeParser.LabelClauseContext ctx) {

    }

    @Override
    public void enterFetchDirection(ru.smartflex.tools.pg.PgSQLIncludeParser.FetchDirectionContext ctx) {

    }

    @Override
    public void exitFetchDirection(ru.smartflex.tools.pg.PgSQLIncludeParser.FetchDirectionContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void enterArgMode(ru.smartflex.tools.pg.PgSQLIncludeParser.ArgModeContext ctx) {

    }

    @Override
    public void exitArgMode(ru.smartflex.tools.pg.PgSQLIncludeParser.ArgModeContext ctx) {

    }

}
