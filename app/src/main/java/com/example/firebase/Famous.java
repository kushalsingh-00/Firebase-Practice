package com.example.firebase;

public class Famous {
    public String name;
    public String famousFor;
    public int rating;

    public Famous()
    {}

    public Famous(String name, String famousFor, int rating) {
        this.name = name;
        this.famousFor = famousFor;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "famous{" +
                "name='" + name + '\'' +
                ", famousFor='" + famousFor + '\'' +
                ", rating=" + rating +
                '}';
    }
}
