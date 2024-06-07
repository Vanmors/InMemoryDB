package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryDB {

    private final ConcurrentMap<Long, Record> records = new ConcurrentHashMap<>();
    private final TreeMap<String, Set<Long>> nameIndex = new TreeMap<>();
    private final TreeMap<Double, Set<Long>> valueIndex = new TreeMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public void addRecord(Record record) {
        lock.lock();
        try {
            records.put(record.getAccount(), record);
            nameIndex.computeIfAbsent(record.getName(), k -> new HashSet<>()).add(record.getAccount());
            valueIndex.computeIfAbsent(record.getValue(), k -> new HashSet<>()).add(record.getAccount());
        } finally {
            lock.unlock();
        }
    }

    public void removeRecord(long account) {
        lock.lock();
        try {
            Record record = records.remove(account);
            if (record != null) {
                nameIndex.get(record.getName()).remove(account);
                if (nameIndex.get(record.getName()).isEmpty()) {
                    nameIndex.remove(record.getName());
                }
                valueIndex.get(record.getValue()).remove(account);
                if (valueIndex.get(record.getValue()).isEmpty()) {
                    valueIndex.remove(record.getValue());
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void updateRecord(Record newRecord) {
        lock.lock();
        try {
            removeRecord(newRecord.getAccount());
            addRecord(newRecord);
        } finally {
            lock.unlock();
        }
    }

    public Record getRecordByAccount(long account) {
        return records.get(account);
    }

    public List<Record> getRecordsByName(String name) {
        List<Record> recordsByName = new ArrayList<>();
        Set<Long> accountSet = nameIndex.get(name);
        for (Long account: accountSet) {
            recordsByName.add(records.get(account));
        }
        return recordsByName;
    }

    public List<Record> getRecordsByValue(double value) {
        List<Record> recordsByValue = new ArrayList<>();
        Set<Long> accountSet = valueIndex.get(value);
        for (Long account: accountSet) {
            recordsByValue.add(records.get(account));
        }
        return recordsByValue;
    }

}
