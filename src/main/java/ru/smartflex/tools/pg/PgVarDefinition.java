package ru.smartflex.tools.pg;

public class PgVarDefinition extends PgFuncReplacementPart {

    int order = 0;
    PgPlSqlElEnum type = null;
    String identifier = null;
    boolean anonymous = false;

    PgVarDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.FunctionParamDefinitionContext parCtx, int order) {
        super(PgPlSqlElEnum.FUNCTION_PARAMETER_DECLARE, null, parCtx);
        this.order = order;

        //ru.smartflex.tools.pg.PgSQLIncludeParser.ArgModeContext argMode = parCtx.argMode();
        ru.smartflex.tools.pg.PgSQLIncludeParser.IdentifierContext ident = parCtx.identifier();
        if (ident != null) {
            identifier = ident.getText();
            setValue(identifier);
        } else {
            anonymous = true;
            setValue(parCtx.pgTypeFull().getText());
        }
        type = ParserHelper.defineDataType(parCtx.pgTypeFull());
    }

    PgVarDefinition(ru.smartflex.tools.pg.PgSQLIncludeParser.VariableDefinitionContext ctx) {
        super(PgPlSqlElEnum.VARIABLE_DECLARE, ctx.identifier().getText(), ctx);
        identifier = ctx.identifier().getText();
        type = ParserHelper.defineDataType(ctx.dataType());
    }

    int getOrder() {
        return order;
    }
}
