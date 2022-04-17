package com.example.team_veritas;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseHelper {
    private Connection conn = null;
    private final String DB_NAME = "bloodbank.db";
    private final String DB_SQL_SCRIPT = "E:\\Team_Veritas\\src\\main\\java\\com\\example\\team_veritas\\database.sql";

    public void connect() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createAllTables() {
        try {
            String sqlContent = Files.readString(Path.of(DB_SQL_SCRIPT));
            this.conn.createStatement().executeUpdate(sqlContent);
        } catch (Exception e) {
            System.err.println("Could not create tables");
        }
    }

    public void example() {
        try {
            this.addPerson("1567", "Minhaz Raufoon", "Chemnitz", "09130", "O+");
            this.addPerson("452", "Mohsina Rifa", "Gazipur", "1234", "O+");
            this.makeDonor("452");
            this.addPerson("666", "Mr Monir", "Pabna", "3212", "B+");
            this.makeDonor("666");

            Person p1 = this.getPersonById("1567");
            System.out.println(p1);
            Person p2 = this.getPersonById("452");
            System.out.println(p2);

            this.addBloodBank("3213", "My Boi Blood Bank", "Dhaka", "1205");
            BloodBank bank = this.getBloodBankById("3213");
            System.out.println(bank);

            bank = this.storeBloodToBank(bank, "A-", 10);
            bank = this.storeBloodToBank(bank, "A-", 5);
            System.out.println(bank);

            ArrayList<Person> donorsOPos = getAllDonors("O+");
            System.out.println("O+ donors:");
            for (Person p : donorsOPos) {
                System.out.println(p);
            }

            ArrayList<Person> donorsBPos = getAllDonors("B+");
            System.out.println("B+ donors:");
            for (Person p : donorsBPos) {
                System.out.println(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insert(String tableName, String[] values) throws SQLException {
        Statement stmt = this.conn.createStatement();
        String query = "INSERT INTO " + tableName + " VALUES (" + String.join(",", values) + ");";
        stmt.executeUpdate(query);
    }

    private void update(String tableName, String where, String[] assignments) throws SQLException {
        Statement stmt = this.conn.createStatement();
        String query = "UPDATE " + tableName + " SET " + String.join(",", assignments) + " WHERE " + where + ";";
        System.out.println(query);
        stmt.executeUpdate(query);
    }

    private ResultSet query(String sql) throws SQLException {
        Statement stmt = this.conn.createStatement();
        return stmt.executeQuery(sql);
    }

    private void insertIfNotExists(String tableName, String[] values) throws SQLException {
        Statement stmt = this.conn.createStatement();

        try {
            String query = "INSERT INTO " + tableName + " VALUES (" + String.join(",", values) + ");";
            System.out.println(query);
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Could not insert to " + tableName + String.join(",", values));
        }
    }

    private String withQuote(String str) {
        return "'" + str + "'";
    }

    private void addUser(String id, String name, String city, String postcode) throws SQLException {
        this.insertIfNotExists("USER", new String[] {
                withQuote(id),
                withQuote(name),
                withQuote(city),
                withQuote(postcode)
        });
    }

    public Person addPerson(String id, String name, String city, String postcode, String bloodGroup)
            throws SQLException {
        this.addUser(id, name, city, postcode);
        insertIfNotExists("PERSON", new String[] {
                withQuote(id),
                withQuote(bloodGroup)
        });

        return new Person(id, name, city, postcode, bloodGroup);
    }

    public Person getPersonById(String personId) throws SQLException {
        ResultSet rs = this.query("SELECT * FROM PERSON NATURAL JOIN USER WHERE ID = '" + personId + "';");

        if (rs.next()) {
            String id = rs.getString("ID");
            String name = rs.getString("NAME");
            String city = rs.getString("CITY");
            String postcode = rs.getString("POSTCODE");
            String bloodGroup = rs.getString("BLOOD_GROUP");
            Person person = new Person(id, name, city, postcode, bloodGroup);

            ResultSet rs2 = this.query("SELECT * FROM PERSON NATURAL JOIN DONOR WHERE ID = '" + personId + "';");
            if (rs2.next()) {
                person.setAsDonor();
                person.setLastDonated(rs2.getString("LAST_DONATED"));
            }

            return person;
        }

        return null;
    }

    public Person makeDonor(String personId) throws SQLException {
        insertIfNotExists("DONOR", new String[] {
                withQuote(personId),
                "''"
        });

        return this.getPersonById(personId);
    }

    public Person makeDonor(Person person) throws SQLException {
        return this.makeDonor(person.getId());
    }

    public BloodBank addBloodBank(String id, String name, String city, String postcode) throws SQLException {
        this.addUser(id, name, city, postcode);
        insertIfNotExists("BLOOD_BANK", new String[] {
                withQuote(id)
        });

        return new BloodBank(id, name, city, postcode);
    }

    public BloodBank getBloodBankById(String bankId) throws SQLException {
        ResultSet rs = this.query("SELECT * FROM BLOOD_BANK NATURAL JOIN USER WHERE ID = '" + bankId + "';");

        if (rs.next()) {
            String id = rs.getString("ID");
            String name = rs.getString("NAME");
            String city = rs.getString("CITY");
            String postcode = rs.getString("POSTCODE");
            BloodBank bank = new BloodBank(id, name, city, postcode);

            ResultSet rs2 = this.query(
                    "SELECT * FROM BLOOD_BANK JOIN BLOOD_STORAGE ON BLOOD_BANK.ID = BLOOD_STORAGE.BANK_ID WHERE BANK_ID = '"
                            + bankId + "';");

            while (rs2.next()) {
                String group = rs2.getString("BLOOD_GROUP");
                int amount = rs2.getInt("AMOUNT");
                bank.setBloodAmount(group, amount);
            }

            return bank;
        }

        return null;
    }

    public BloodBank storeBloodToBank(BloodBank bank, String bloodGroup, int amount) throws SQLException {
        if (bank.getBloodAmount(bloodGroup) == 0) {
            this.insert("BLOOD_STORAGE", new String[] {
                    withQuote(bank.getId()),
                    withQuote(bloodGroup),
                    String.valueOf(amount)
            });
        } else {
            this.update(
                    "BLOOD_STORAGE",
                    "BANK_ID = " + this.withQuote(bank.getId()) + " AND BLOOD_GROUP = " + this.withQuote(bloodGroup),
                    new String[] {
                            "AMOUNT = " + String.valueOf(bank.getBloodAmount(bloodGroup) + amount)
                    });
        }

        return this.getBloodBankById(bank.getId());
    }

    public ArrayList<Person> getAllDonors(String bloodGroup) throws SQLException {
        ResultSet rs = this.query("SELECT * FROM DONOR NATURAL JOIN PERSON NATURAL JOIN USER WHERE BLOOD_GROUP = "
                + this.withQuote(bloodGroup) + ";");

        ArrayList<Person> donors = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString("ID");
            String name = rs.getString("NAME");
            String city = rs.getString("CITY");
            String postcode = rs.getString("POSTCODE");
            Person person = new Person(id, name, city, postcode, bloodGroup);
            person.setAsDonor();
            person.setLastDonated(rs.getString("LAST_DONATED"));
            donors.add(person);
        }

        return donors;
    }

    public static void main(String[] args) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.connect();
        dbHelper.createAllTables();
        dbHelper.example();
        dbHelper.disconnect();
    }
}
