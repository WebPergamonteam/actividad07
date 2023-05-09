/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package actividad07.libreria;


/**
 * @author Vadym Volokhov
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String country;
    private int pages;
    private String genre;

    public Book(int id, String title, String author, String country, int pages, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.country = country;
        this.pages = pages;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return String.format("%d - %s, %s, %s, %d p., %s", id, title, author, country, pages, genre);
    }
}