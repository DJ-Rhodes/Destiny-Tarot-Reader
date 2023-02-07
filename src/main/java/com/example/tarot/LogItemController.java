package com.example.tarot;

import dbManagers.LogDBManager;
import domain.LogItem;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LogItemController {

    @FXML
    private Label entryID;

    @FXML
    private Label entryDate;

    @FXML
    private Label entryCard;

    @FXML
    private Label entryRating;
    @FXML
    private ImageView removeLogButton;


    public void setEntryDate(String entryDate) {
        this.entryDate.setText(entryDate);
    }

    public void setEntryID(String entryID) {
        this.entryID.setText(entryID);
    }

    public void setEntryCard(String entryCard) {
        this.entryCard.setText(entryCard);
    }

    public void setEntryRating(String entryRating) {
        this.entryRating.setText(entryRating);
    }

    public ImageView getRemoveLogButton() {
        return this.removeLogButton;
    }


}
