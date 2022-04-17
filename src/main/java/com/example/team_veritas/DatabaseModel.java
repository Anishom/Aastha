package com.example.team_veritas;
import java.util.HashMap;

class User {
    private String id;
    private String name;
    private String city;
    private String postcode;

    public User(String id, String name, String city, String postcode) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.postcode = postcode;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getCity() {
        return this.city;
    }

    public String getPostcode() {
        return this.postcode;
    }
}

class Person extends User {
    private String bloodGroup;
    private boolean isDonor;
    private String lastDonated;

    public Person(String id, String name, String city, String postcode, String bloodGroup) {
        super(id, name, city, postcode);
        this.bloodGroup = bloodGroup;
        this.isDonor = false;
        this.lastDonated = "";
    }

    void setAsDonor() {
        this.isDonor = true;
    }

    void setLastDonated(String date) {
        this.lastDonated = date;
    }

    public String toString() {
        return "-----------\n"
                + "Person:\n"
                + "ID: " + this.getId() + "\n"
                + "Name: " + this.getName() + "\n"
                + "City: " + this.getCity() + "\n"
                + "Postcode: " + this.getPostcode() + "\n"
                + "Blood Group: " + this.bloodGroup + "\n"
                + "Donor: " + (this.isDonor ? "Yes" : "No") + "\n"
                + "Last donated: " + this.lastDonated + "\n"
                + "-----------\n";
    }
}

class BloodBank extends User {
    private HashMap<String, Integer> bloodStorage;

    public BloodBank(String id, String name, String city, String postcode) {
        super(id, name, city, postcode);

        bloodStorage = new HashMap<>();
        bloodStorage.put("A+", 0);
        bloodStorage.put("A-", 0);
        bloodStorage.put("B+", 0);
        bloodStorage.put("B-", 0);
        bloodStorage.put("AB+", 0);
        bloodStorage.put("AB-", 0);
        bloodStorage.put("O+", 0);
        bloodStorage.put("O-", 0);
    }

    public void setBloodAmount(String bloodGroup, int amount) {
        this.bloodStorage.put(bloodGroup, amount);
    }

    public int getBloodAmount(String bloodGroup) {
        return this.bloodStorage.get(bloodGroup);
    }

    public String toString() {
        return "-----------\n"
                + "Blood Bank:\n"
                + "ID: " + this.getId() + "\n"
                + "Name: " + this.getName() + "\n"
                + "City: " + this.getCity() + "\n"
                + "Postcode: " + this.getPostcode() + "\n"
                + "Inventory: \n"
                + "\tA+: " + this.getBloodAmount("A+") + " litre\n"
                + "\tA-: " + this.getBloodAmount("A-") + " litre\n"
                + "\tB+: " + this.getBloodAmount("B+") + " litre\n"
                + "\tB-: " + this.getBloodAmount("B-") + " litre\n"
                + "\tAB+: " + this.getBloodAmount("AB+") + " litre\n"
                + "\tAB-: " + this.getBloodAmount("AB-") + " litre\n"
                + "\tO+: " + this.getBloodAmount("O+") + " litre\n"
                + "\tO-: " + this.getBloodAmount("O-") + " litre\n"
                + "-----------\n";
    }
}
