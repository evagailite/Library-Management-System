package entity;

import types.Genre;
import types.Status;

public class Book {
    private int id;
    private String author;
    private String title;
    private Genre genre;
    private String ISBN;
    private int pages;
    private Status status;

    public Book() {
    }

    public Book(int id, String author, String title, Genre genre,
                String ISBN, int pages, Status status) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.ISBN = ISBN;
        this.pages = pages;
        this.status = status;
    }

    public Book(int id, String author, String title, String ISBN, int pages) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.genre = genre;
        this.ISBN = ISBN;
        this.pages = pages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

}
