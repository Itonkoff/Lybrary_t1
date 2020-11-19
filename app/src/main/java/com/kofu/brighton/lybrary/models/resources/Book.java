package com.kofu.brighton.lybrary.models.resources;

public class Book {
    public int id;
    public String name;
    public String version;
    public String author;
    public int count;
    public int daysAllowable;
    public int penaltyRate;

    public Book(String name, String version, String author) {
        this.name = name;
        this.version = version;
        this.author = author;
        this.count = 0;
        this.daysAllowable = 1;
        this.penaltyRate = 1;
    }
}
