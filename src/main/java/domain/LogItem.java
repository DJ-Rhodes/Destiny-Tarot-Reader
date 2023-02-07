package domain;

import java.util.List;

public class LogItem {
    private int id;
    private String date;
    private String question;
    private String card;
    private int rating;
    private String entry;


    public LogItem(int id, String date, String question, String card, int rating, String entry) {
        this.id = id;
        this.date = date;
        this.question = question;
        this.card = card;
        this.rating = rating;
        this.entry = entry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }


    @Override
    public String toString() {
        return "Entered as:" + this.id + "\n" + this.date + "\n" + this.question + "\n" + this.card + "\n" + this.rating + "\n" + this.entry;
    }
}
