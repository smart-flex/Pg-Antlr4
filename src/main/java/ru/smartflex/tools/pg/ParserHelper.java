package ru.smartflex.tools.pg;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ParserHelper {

    public static PgTreeNode makeTree(PgParsingResultBag pgParsingResultBag) {
        PgTreeNode root = PgTreeNode.createRoot();

        List<PgTreeNode> nodeList = new ArrayList();
        for (PgParsingResult res : pgParsingResultBag.getResultList()) {
            PgFuncDefined funcDefined = res.getFuncDefined();
            PgTreeNode node = new PgTreeNode(funcDefined);

            for (PgFuncInvoked inv : res.getFunctionInvocationsList()) {
                PgTreeNode child = new PgTreeNode(node, inv);
                inv.setChildNode(child);
                node.addChild(child);
            }

            nodeList.add(node);
        }
        // sub node м.б. использован дважды , в Null его нельзя переводить
        for (int i = 0; i < nodeList.size(); i++) {
            PgTreeNode node1 = nodeList.get(i);

            for (int k = 0; k < nodeList.size(); k++) {
                PgTreeNode node2 = nodeList.get(k);
                for (PgTreeNode nd : node2.getChildList()) {
                    if (node1.equals(nd)) {
                        node1.setWasUsedAsChild(true);
                        nd.replaceNodeWithBody(node1);
                    }
                }
            }
        }

        for (PgTreeNode nd : nodeList) {
            if (!nd.isWasUsedAsChild()) {
                root.addChild(nd);
            }
        }

        return root;
    }

    static PgPlSqlElEnum defineDataType(ru.smartflex.tools.pg.PgSQLIncludeParser.PgTypeFullContext ctxFull) {
        PgPlSqlElEnum ret = null;

        if (ctxFull.pgTypeEnum() != null) {
            ru.smartflex.tools.pg.PgSQLIncludeParser.PgTypeEnumContext pctx = ctxFull.pgTypeEnum();

            if (pctx.SMALLINT() != null || pctx.INT2() != null) {
                ret = PgPlSqlElEnum.DT_INT2;
            } else if (pctx.INT() != null || pctx.INT4() != null || pctx.INTEGER() != null || pctx.SERIAL() != null
                    || pctx.SERIAL4() != null) {
                ret = PgPlSqlElEnum.DT_INT4;
            } else if (pctx.BIGINT() != null || pctx.INT8() != null || pctx.BIGSERIAL() != null || pctx.SERIAL8() != null) {
                ret = PgPlSqlElEnum.DT_INT8;
            } else if (pctx.REAL() != null || pctx.FLOAT4() != null) {
                ret = PgPlSqlElEnum.DT_FLOAT;
            } else if (pctx.DOUBLE_PRECISION() != null || pctx.FLOAT8() != null) {
                ret = PgPlSqlElEnum.DT_DOUBLE;
            } else if (pctx.DECIMAL() != null || pctx.NUMERIC() != null || pctx.MONEY() != null) {
                ret = PgPlSqlElEnum.DT_NUMERIC;
            } else if (pctx.CHAR() != null || pctx.CHARACTER() != null) {
                ret = PgPlSqlElEnum.DT_CHAR;
            } else if (pctx.VARCHAR() != null || pctx.TEXT() != null) {
                ret = PgPlSqlElEnum.DT_VARCHAR;
            } else if (pctx.DATE() != null) {
                ret = PgPlSqlElEnum.DT_DATE;
            } else if (pctx.TIME() != null || pctx.TIME_WITHOUT_TIME_ZONE() != null || pctx.TIME_WITH_TIME_ZONE() != null ||
                    pctx.TIMETZ() != null || pctx.TIMESTAMP() != null || pctx.TIMESTAMP_WITHOUT_TIME_ZONE() != null ||
                    pctx.TIMESTAMP_WITH_TIME_ZONE() != null || pctx.TIMESTAMPTZ() != null) {
                ret = PgPlSqlElEnum.DT_TIME;
            }

        } else {
            //TODO ? дописать для (identifier PERCENT ROWTYPE) и tableRefColumnType
            // TODO обрабатываем rowtype, column type и пр
            // There is no matching for: t09_yyyymm%ROWTYPE;
            ret = PgPlSqlElEnum.DT_TODO;
        }
        return ret;
    }

    static PgPlSqlElEnum defineDataType(ru.smartflex.tools.pg.PgSQLIncludeParser.DataTypeContext ctx) {
        PgPlSqlElEnum ret = null;
        if (ctx.booleanType() != null) {
            ret = PgPlSqlElEnum.DT_BOOLEAN;
        } else if (ctx.cursorType() != null) {
            ret = PgPlSqlElEnum.DT_CURSOR;
        } else if (ctx.usualType() != null) {
            if (ctx.usualType().pgTypeFull() != null) {
                ret = defineDataType(ctx.usualType().pgTypeFull());
            } else {
                // TODO обрабатываем rowtype, column type и пр
                ret = PgPlSqlElEnum.DT_TODO;
            }
        }

        // TODO дописать соответствие типов

        if (ret == null) {
            throw new PgSQLQjuException("There is no matching for: " + ctx.getText());
        }

        return ret;
    }

    public static String getHash(String text) {
        return  getHash(text, "UTF-8");
    }

    public static String getHash(String text, String enc) {
        String hash = null;
        byte[] bytesOfMessage = null;
        try {
            bytesOfMessage = text.getBytes(enc);
        } catch (UnsupportedEncodingException e) {
            PgSQLQjuException pe = new PgSQLQjuException("Encoding error");
            pe.addSuppressed(e);
            throw pe;
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            StringBuffer hashBuffer = new StringBuffer();
            for (int i = 0; i < thedigest.length; i++) {
                int val = (thedigest[i] & 255) | 0x100;
                hashBuffer = hashBuffer.append(Integer.toHexString(val)
                        .substring(1));
            }
            hash = hashBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            PgSQLQjuException pe = new PgSQLQjuException("Hash error");
            pe.addSuppressed(e);
            throw pe;
        }

        return hash;
    }
}
