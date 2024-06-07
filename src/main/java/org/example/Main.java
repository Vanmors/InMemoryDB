package org.example;

public class Main {
    public static void main(String[] args) {
        InMemoryDB db = new InMemoryDB();
        Record record1 = new Record(234678, "Иванов Иван Иванович", 2035.34);
        Record record2 = new Record(234677, "Иванов Иван Иванович", 2036.34);
        Record record3 = new Record(234679, "Петров Петр Петрович", 1000.00);

        db.addRecord(record1);
        db.addRecord(record2);
        db.addRecord(record3);

        System.out.println("Record by account 234678: " + db.getRecordByAccount(234678));
        System.out.println("Records by name 'Иванов Иван Иванович': " + db.getRecordsByName("Иванов Иван Иванович"));
        System.out.println("Records by value 2035.34: " + db.getRecordsByValue(2035.34));
        Record newRecord = new Record(234678, "Иванов Иван Иванович", 2500.00);
        db.updateRecord(newRecord);
        db.removeRecord(234679);
        System.out.println("Updated record by account 234678: " + db.getRecordByAccount(234678));
    }
}