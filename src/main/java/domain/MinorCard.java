package domain;

public class MinorCard extends Card{

    private String suit;

    public MinorCard(int id, String imageFile, String arcana, String name, String suit, String keyword, String correspondence, String desc) {
        super(id, imageFile, "Minor", name, keyword, correspondence ,desc);
        this.suit = suit;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String toString() {
        return "carded entered as: " + getId() + " " + getImageFile() + " " + getArcana() + " " + getName() + " " + getSuit() + " " + getKeyword() + " " + getCorrespondence() + "\n" + getDesc();
    }
}
