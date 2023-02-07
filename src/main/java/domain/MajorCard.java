package domain;

public class MajorCard extends Card{
    private String hebrew;

    public MajorCard(int id, String imageFile, String arcana, String name, String keyword, String hebrew, String correspondence, String desc) {
        super(id, imageFile,"Major", name, keyword,correspondence, desc);
        this.hebrew = hebrew;
    }

    public String getHebrew() {
        return hebrew;
    }

    public void setHebrew(String hebrew) {
        this.hebrew = hebrew;
    }

    @Override
    public String toString() {
        return super.getId() + " added as: " + getImageFile() + ", " + getArcana() + ", " + getName() + "\nWith keyword: " + super.getKeyword() + "\nWith hebrew: " + hebrew + "\nWith correspondence: " + super.getCorrespondence() +"\nWith Description:\n" + super.getDesc();
    }
}
