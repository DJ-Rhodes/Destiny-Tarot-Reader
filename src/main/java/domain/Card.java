package domain;

public abstract class Card {

    private int id;
    private String arcana;
    private String name;
    private String imageFile;
    private String keyword;
    private String desc;
    private String correspondence;

    public Card(int id, String imageFile, String arcana, String name, String keyword, String correspondence, String desc) {
        this.id = id;
        this.imageFile = imageFile;
        this.arcana = arcana;
        this.name = name;
        this.keyword = keyword;
        this.desc = desc;
        this.correspondence = correspondence;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCorrespondence() {
        return correspondence;
    }

    public void setCorrespondence(String correspondence) {
        this.correspondence = correspondence;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArcana() {
        return arcana;
    }

    public void setArcana(String arcana) {
        this.arcana = arcana;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String toString() {
        return id + " added as: " + getImageFile() + ", " + getArcana() + ", " + getName() + "\nWith keyword: " + keyword + "\nWith correspondence: " + correspondence +"\nWith Description:\n" + desc;
    }
}
