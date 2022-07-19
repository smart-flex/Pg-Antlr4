package ru.smartflex.tools.pg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Stream;

public class PgParseFunctions {

    private static final int THREAD_AMOUNT = 2;

    public PgParsingResultBag oarseFromEnum(Stream<PgPlSQLEnums> stream) {

        Function<PgPlSQLEnums, InputStream> funcIStream = pgEum -> {
            String sql = pgEum.getSqlName();
            return PgParseFunctions.class.getClassLoader().getResourceAsStream(sql);
        };

        return parseFromIs(stream.map(funcIStream));
    }

    public PgParsingResultBag parseFromIs(Stream<InputStream> stream) {

        List<Future<PgParsingResult>> listAns = new ArrayList();

        ExecutorService service = Executors.newFixedThreadPool(THREAD_AMOUNT);

        stream.forEach(is -> {
            Future<PgParsingResult> fut = service.submit(new ThreadPgParser(is));
            listAns.add(fut);
        });

        Stream<Future<PgParsingResult>> streamFut = listAns.stream();
        List<PgParsingResult> listResult = new ArrayList();
        streamFut.forEach(fut -> {
            PgParsingResult result;
            try {
                result = fut.get();

                listResult.add(result);
            } catch (Exception e) {
                // TODO transfer exception to logger or something else
            }

        });

        service.shutdown();

        PgParsingResultBag pgParsingResultBag = new PgParsingResultBag();

        listResult.stream().forEach(res -> pgParsingResultBag.addResult(res));

        return pgParsingResultBag;
    }
}
