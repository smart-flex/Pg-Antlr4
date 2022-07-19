package ru.smartflex.tools.pg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PgParsingResultBag {

    Lock lock = new ReentrantLock();

    List<PgParsingResult> resultList = new ArrayList<>();

    public void addResult(PgParsingResult result) {

        if (result.isParsingErrorHappened()) {
            return;
        }

        lock.lock();
        try {
            resultList.add(result);
        } finally {
            lock.unlock();
        }
    }

    public PgParsingResult getResultFirst() {
        return resultList.get(0);
    }

    public List<PgParsingResult> getResultList() {
        return resultList;
    }
}
