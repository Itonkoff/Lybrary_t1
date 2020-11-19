package com.kofu.brighton.lybrary.models.resources;

public class BorrowBook {
    private int book;
    private String studentId;

    public BorrowBook(int book, String studentId) {
        this.book = book;
        this.studentId = studentId;
    }
}
