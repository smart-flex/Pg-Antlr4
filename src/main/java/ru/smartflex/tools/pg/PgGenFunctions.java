package ru.smartflex.tools.pg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Stream;

public class PgGenFunctions {

    private static final int THREAD_AMOUNT = 2;

    public void genFromEnum(Stream<PgPlSQLEnums> stream) {

        Function<PgPlSQLEnums, InputStream> funcIStream = pgEum -> {
            String sql = pgEum.getSqlName();
            return PgGenFunctions.class.getClassLoader().getResourceAsStream(sql);
        };

        genFromIs(stream.map(funcIStream));
    }

    public void genFromIs(Stream<InputStream> stream) {

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

                // TODO remove
                System.out.println("##### " + result);

                listResult.add(result);
            } catch (Exception e) {

            }

        });

        service.shutdown();
    }
}
