package ru.smartflex.tools.pg;

class PgAnonymousParameter extends PgFuncReplacementPart {

    private Integer order = null;

    PgAnonymousParameter(ru.smartflex.tools.pg.PgSQLIncludeParser.AnonymousParameterContext ctx) {
        super(PgPlSqlElEnum.ANONYMOUS_PARAMETER, ctx.ANONYMOUS_PAR().getText(), ctx);

        init(ctx);
    }

    private void init(ru.smartflex.tools.pg.PgSQLIncludeParser.AnonymousParameterContext ctx) {
        order = Integer.parseInt(ctx.ANONYMOUS_PAR().getText().substring(1));
    }

    Integer getOrder() {
        return order;
    }
}
