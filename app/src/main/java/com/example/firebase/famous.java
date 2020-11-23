package com.example.firebase;

public class famous {
    public String name;
    public String famousFor;
    public int rating;

    public famous()
    {}

    public famous(String name, String famousFor, int rating) {
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
