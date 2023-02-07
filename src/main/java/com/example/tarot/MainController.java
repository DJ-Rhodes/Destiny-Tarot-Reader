package com.example.tarot;


import dbManagers.LogDBManager;
import dbManagers.MajorDBManager;
import dbManagers.MinorDBManager;
import domain.Card;
import domain.LogItem;
import domain.MajorCard;
import domain.MinorCard;
import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private String majorDatabasePath = "jdbc:h2:./~cardsDB";
    private MajorDBManager majorDBManager = new MajorDBManager(majorDatabasePath);
    private MinorDBManager minorDBManager = new MinorDBManager(majorDatabasePath);
    private LogDBManager logDBManager = new LogDBManager(majorDatabasePath);
    private boolean dailyCardDrawn = false; //Main card flipped or not
    private boolean lessonsExpanded = false; //Lessons submenu expanded
    private boolean dbExpanded = false; //Database submenu expanded
    private boolean majorViewed; //Major Arcana database currently in view
    private String dailyCardName;
    private String infoState = "first"; //How many information panes are currently shown
    private int itemViewed;
    private int dbMajorCardNum = 1;
    private int dbMinorCardNum = 1;

    @FXML
    private ImageView dailyCard;
    @FXML
    private ImageView closeButton;
    @FXML
    private ImageView sadRating;
    @FXML
    private ImageView neutralRating;
    @FXML
    private ImageView happyRating;
    @FXML
    private ImageView ratingEntryIcon;
    @FXML
    private ImageView expandArrow;
    @FXML
    private ImageView dbCard;
    @FXML
    private Label arcanaLabel;
    @FXML
    private Label numberLabel;
    @FXML
    private Label suitLabel;
    @FXML
    private Label keywordLabel;
    @FXML
    private Label corrLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Label corrHeading;
    @FXML
    private Label suitHeader;
    @FXML
    private Label hebrewHeader;
    @FXML
    private Label confirmLabel;
    @FXML
    private Label savedEntryLabel;
    @FXML
    private Label entryCard;
    @FXML
    private Label ratingEntryText;
    @FXML
    private Label headingLabel;
    @FXML
    private Label infoLabel;
    @FXML
    private Label infoLabel2;
    @FXML
    private Label infoLabel3;
    @FXML
    private Label infoLabel4;
    @FXML
    private Label infoLabel5;
    @FXML
    private Label dBTitle;
    @FXML
    private Label dBKeyword;
    @FXML
    private Label dBHebrew;
    @FXML
    private Label dBCorr;
    @FXML
    private Label dBDesc;
    @FXML
    private Label headerLabel;
    @FXML
    private Pane savedEntryPane;
    @FXML
    private Pane arcanaPane;
    @FXML
    private Pane numberPane;
    @FXML
    private Pane suitPane;
    @FXML
    private Pane keywordPane;
    @FXML
    private Pane corrPane;
    @FXML
    private Pane journalPane;
    @FXML
    private Pane cardEntryPane;
    @FXML
    private Pane ratingEntryPane;
    @FXML
    private Pane successPane;
    @FXML
    private Pane introPane2;
    @FXML
    private Pane introPane3;
    @FXML
    private Pane introPane4;
    @FXML
    private Pane introPane5;
    @FXML
    private Pane introPane;
    @FXML
    private Button saveToLogBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Button logBtn;
    @FXML
    private Button deleteEntryButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button deleteCheckButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button infoButton;
    @FXML
    private Button introBtn;
    @FXML
    private Button meaningBtn;
    @FXML
    private Button majorBtn;
    @FXML
    private Button minorBtn;
    @FXML
    private Button dataMenuBtn;
    @FXML
    private Button majorDBBtn;
    @FXML
    private Button minorDBBtn;
    @FXML
    private AnchorPane dashboard;
    @FXML
    private AnchorPane logList;
    @FXML
    private AnchorPane dbPane;
    @FXML
    private Slider ratingSlider;
    @FXML
    private TextArea journalEntryText;
    @FXML
    private VBox logItems;
    @FXML
    private ScrollPane logListPane;
    @FXML
    private VBox menuVBox;




    //Flips card and shows information on click
    @FXML
    void dailyCardClicked(MouseEvent event) throws InterruptedException, SQLException, ClassNotFoundException {
        if(!dailyCardDrawn) {
            //Rolls to randomly choose a database to draw from
            boolean minorRolled = false;
            Random random = new Random();
            Card card;
            int roll = random.nextInt(78) + 1;
            if (roll < 23) {
                card = drawMajor();
            } else {
                card = drawMinor();
                minorRolled = true;
            }
            boolean finalMinorRolled = minorRolled;

            Image image = drawCardImage(card);
            BooleanProperty front = new SimpleBooleanProperty(true);

            //Scales image down if mouse hovered
            ScaleTransition scale = scaleDown();

            RotateTransition rotator1 = createRotator(0, 90);

            rotator1.setOnFinished(evt -> {
                //Sets card image to front
                dailyCard.setImage(image);
                front.set(false);
                if(finalMinorRolled) {
                    setMinorLabels((MinorCard) card);
                } else {
                    setMajorLabels((MajorCard) card);
                }
            });

            RotateTransition rotator2 = createRotator(90, 180);

            rotator2.setOnFinished(evt -> saveToLogBtn.setText("SAVE TO LOG"));

            //Plays card flip animation
            SequentialTransition st = new SequentialTransition(scale, rotator1, rotator2);
            st.setCycleCount(1);
            st.play();

            dailyCardDrawn = true;
        }
    }

    //Sets the value for the front of the card
    private Image drawCardImage(Card card) throws SQLException, ClassNotFoundException {
        Image image = new Image(getClass().getResourceAsStream("/cards/" + card.getImageFile()));
        return image;
    }


    //Draws a random card from the Major Arcana
    private MajorCard drawMajor() throws SQLException, ClassNotFoundException {
        MajorCard card;
        Random random = new Random();
        int id = random.nextInt(22) + 1;
        card = majorDBManager.drawById(id);
        dailyCardName = card.getName();
        return card;
    }

    //Draws a random card from the Minor Arcana
    private MinorCard drawMinor() throws SQLException, ClassNotFoundException {
        MinorCard card;
        Random random = new Random();
        int id = random.nextInt(56) + 1;
        card = minorDBManager.drawById(id);
        dailyCardName = card.getName() + " " + card.getSuit();
        return card;
    }

    //Sets information labels if a minor card is drawn
    private void setMinorLabels(MinorCard card) {
        arcanaLabel.setText(card.getArcana());
        numberLabel.setText(card.getName());
        suitLabel.setText(card.getSuit());
        keywordLabel.setText(card.getKeyword());
        corrLabel.setText(card.getCorrespondence());
        descLabel.setText(card.getDesc());
    }

    //Sets information labels if a major card is drawn
    private void setMajorLabels(MajorCard card) {
        arcanaLabel.setText(card.getArcana());
        numberLabel.setText(card.getName());
        suitLabel.setText(card.getKeyword());
        suitHeader.setText("KEYWORD");
        keywordLabel.setText(card.getHebrew());
        hebrewHeader.setText("HEBREW");
        corrLabel.setText(card.getCorrespondence());
        descLabel.setText(card.getDesc());
    }


    //Creates the rotator for card flip animation
    private RotateTransition createRotator(double fromAngle, double toAngle) {
        RotateTransition rotator = new RotateTransition(Duration.millis(500), dailyCard);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(fromAngle);
        rotator.setToAngle(toAngle);
        rotator.setInterpolator(Interpolator.LINEAR);

        return rotator;
    }

    //Create the scale transition for scaling down cards
    private ScaleTransition scaleDown() {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), dailyCard);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setToZ(1.0);

        return st;
    }

    //Plays shifting animation of information labels
    private TranslateTransition shiftLabels() {
        TranslateTransition tt;
            tt = new TranslateTransition(Duration.millis(350), numberPane);
            tt.setToY(-83);
            tt.play();

            tt = new TranslateTransition(Duration.millis(450), suitPane);
            tt.setToY(-173);
            tt.play();

            tt = new TranslateTransition(Duration.millis(550), keywordPane);
            tt.setToY(-261);
            tt.play();

            tt = new TranslateTransition(Duration.millis(650), corrPane);
            tt.setToY(-346);
            tt.play();

        return tt;
    }

    //Sets labels for writing a new journal entry
    private void setSaveLabels() {
        corrLabel.setLayoutX(133);
        corrLabel.setText("How do you feel about this card today?");
        corrHeading.setLayoutX(222);
        corrHeading.setText("RATING");
        saveBtn.setVisible(true);
    }

    //Plays animation of expanding journal entry pane
    public void changeJournalSize() {
        Duration cycleDuration = Duration.millis(800);
        Timeline timeline = new Timeline(
                new KeyFrame(cycleDuration,
                        new KeyValue(journalPane.maxWidthProperty(),472,Interpolator.EASE_BOTH)),
               new KeyFrame(cycleDuration,
                        new KeyValue(journalPane.maxHeightProperty(),390,Interpolator.EASE_BOTH))
        );
        journalPane.setVisible(true);
        timeline.play();

        timeline.play();

        Timeline jTimeline = new Timeline(
                new KeyFrame(cycleDuration, new KeyValue(journalEntryText.opacityProperty(), 1, Interpolator.LINEAR))
        );

        timeline.setOnFinished(evt -> jTimeline.play());
    }

    //When saveToLogBtn clicked play all animations and set labels for writing a new journal entry
    @FXML
    void saveToLogBtnClicked(MouseEvent event) throws InterruptedException {
        if (dailyCardDrawn) {
            setSaveLabels();

            shiftLabels().setOnFinished(evt -> {
                ratingSlider.setVisible(true);
                sadRating.setVisible(true);
                neutralRating.setVisible(true);
                happyRating.setVisible(true);
                dashboard.toFront();
            });

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), journalPane);
            tt.setToY(173);

            SequentialTransition sequential = new SequentialTransition(shiftLabels(), tt);
            sequential.play();

            sequential.setOnFinished(evt -> changeJournalSize());
        }

    }


    //Scales up card when moused entered
    @FXML
    void dailyCardHover(MouseEvent event) throws InterruptedException {
        if(!dailyCardDrawn) {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), dailyCard);
            st.setToX(1.1);
            st.setToY(1.1);
            st.setToZ(1.1);
            st.play();
        }
    }

    //Scales down card when mouse exited
    @FXML
    void dailyCardExited(MouseEvent event) throws InterruptedException {
        if (!dailyCardDrawn) {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), dailyCard);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setToZ(1.0);
            st.play();
        }
    }

    //Saves a new entry to the journal
    private void saveEntry() throws SQLException, ClassNotFoundException {
        LocalDate date = LocalDate.now();
        String dateString = "" + date;
        int value = (int) ratingSlider.getValue();
        LogItem logItem = new LogItem(-1, dateString, "No Question For Now",dailyCardName, value, journalEntryText.getText());

        //sets entry panes invisible and displays save confirmation message
        journalPane.setVisible(false);
        ratingSlider.setVisible(false);
        arcanaPane.setVisible(false);
        numberPane.setVisible(false);
        suitPane.setVisible(false);
        keywordPane.setVisible(false);
        corrPane.setVisible(false);
        successPane.setVisible(true);
        sadRating.setVisible(false);
        neutralRating.setVisible(false);
        happyRating.setVisible(false);
        saveBtn.setVisible(false);
        saveToLogBtn.setText("CARD SAVED");

        //Adds new card to database and adds them to the log list
        logDBManager.add(logItem);
        logItems.getChildren().removeAll();
        logDBManager.resetKey();
        listLogEntries();
    }

    //Saves new entry to the log database when save button clicked
    @FXML
    void saveBtnClicked(MouseEvent event) throws InterruptedException, SQLException, ClassNotFoundException {
        saveEntry();
    }

    //Closes program when exit clicked
    @FXML
    void closeClicked(MouseEvent event) throws InterruptedException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    //Changes X icon to red when hovered over with mouse
    @FXML
    void closeHover(MouseEvent event) throws InterruptedException {
        Image redClose = new Image(getClass().getResourceAsStream("/icons/closeHover.png"));
        closeButton.setImage(redClose);
    }

    //Changes X icon back to grey when mouse exited
    @FXML
    void closeExit(MouseEvent event) throws InterruptedException {
        Image close = new Image(getClass().getResourceAsStream("/icons/close.png"));
        closeButton.setImage(close);
    }

    //Displays confirmation message when attempting to delete journal entry
    private void setDeleteConfirmationLabels() {
        deleteEntryButton.setVisible(true);
        cancelButton.setVisible(true);
        confirmLabel.setVisible(true);
        deleteCheckButton.setVisible(false);
        returnButton.setVisible(false);
    }

    //Displays delete confirmation when delete clicked
    @FXML
    void deleteCheckButtonClicked(MouseEvent event) throws InterruptedException {
        setDeleteConfirmationLabels();
    }

    //Displays panes for list of log entries
    private void displayLogList() {
        savedEntryPane.setVisible(false);
        logListPane.setVisible(true);
        cardEntryPane.setVisible(false);
        ratingEntryPane.setVisible(false);
        deleteEntryButton.setVisible(false);
        returnButton.setVisible(false);
        cancelButton.setVisible(false);
        confirmLabel.setVisible(false);
        deleteCheckButton.setVisible(false);
    }

    //removes selected item from log database
    private void removeLogEntry() throws SQLException, ClassNotFoundException {
        logDBManager.removeItem(itemViewed);
        logDBManager.resetKey();
        listLogEntries();
    }

    //Removes item from log database and changes view to log list
    @FXML
    void deleteButtonClicked(MouseEvent event) throws InterruptedException, SQLException, ClassNotFoundException {
        displayLogList();
        removeLogEntry();
    }

    //Cancels deletion of log entry
    @FXML
    void cancelButtonClicked(MouseEvent event) throws InterruptedException {
        deleteCheckButton.setVisible(true);
        deleteEntryButton.setVisible(false);
        cancelButton.setVisible(false);
        returnButton.setVisible(true);
        confirmLabel.setVisible(false);
    }

    //Displays log list when return clicked
    @FXML
    void returnButtonClicked(MouseEvent event) throws InterruptedException {
        displayLogList();
    }

    //Creates translation animation for expanding information panes
    private void createPaneTranslation(int time, Node node, int y) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(time), node);
        tt.setToY(y);
        tt.play();
    }

    //Expands information panes
    private void expandInformationPanes() {
        if(infoState.equals("first")) {
            introPane2.setVisible(true);

            //expands second set of information
            createPaneTranslation(400, introPane2, 113);
            createPaneTranslation(400, expandArrow, 113);

            infoState = "second";
        } else if(infoState.equals("second")) {
            introPane3.setVisible(true);

            //expands third set of information
            createPaneTranslation(400, introPane3, 113);
            createPaneTranslation(400, expandArrow, 226);

            infoState = "third";
        } else if(infoState.equals("third")) {
            introPane4.setVisible(true);

            //expands fourth set of information
            createPaneTranslation(44, introPane4, 113);
            createPaneTranslation(400, expandArrow, 339);

            infoState = "fourth";
        } else if(infoState.equals("fourth")) {
            introPane5.setVisible(true);

            //expands final set of information
            createPaneTranslation(400, introPane5, 113);

            expandArrow.setVisible(false);
            infoState = "fifth";
        }
    }

    //Plays and displays information panes when arrow clicked
    @FXML
    void expandArrowClicked(MouseEvent event) throws InterruptedException {
        expandInformationPanes();
    }

    //Gets and displays information for next major arcana card in database
    private void majorCardScroll() throws SQLException, ClassNotFoundException {
        //Reverts back to first card if end of database reached
        if(dbMajorCardNum == 23) {
            dbMajorCardNum = 1;
        }
        //Goes to the final card of the database if beginning of database reached
        if(dbMajorCardNum == 0) {
            dbMajorCardNum = 22;
        }

        //Draws next card in order
        MajorCard card = majorDBManager.drawById(dbMajorCardNum);

        //Sets all information and image for new card
        dBTitle.setText(card.getName().toUpperCase());
        dBKeyword.setText(card.getKeyword().toUpperCase());
        dBHebrew.setText(card.getHebrew().toUpperCase());
        dBCorr.setText(card.getCorrespondence().toUpperCase());
        dBDesc.setText(card.getDesc());
        Image image = new Image(getClass().getResourceAsStream("/cards/" + card.getImageFile()));
        dbCard.setImage(image);
        dbCard.setScaleX(-1);
    }

    private void minorCardScroll() throws SQLException, ClassNotFoundException {
        //Reverts back to first card if end of database reached
        if(dbMinorCardNum == 57) {
            dbMinorCardNum = 1;
        }
        //Goes to the final card of the database if beginning of database reached
        if(dbMinorCardNum == 0) {
            dbMinorCardNum = 57;
        }

        //Draws next card in order
        MinorCard card = minorDBManager.drawById(dbMinorCardNum);

        //Sets all information and image for new card
        dBTitle.setText(card.getName().toUpperCase() + " " +card.getSuit().toUpperCase());
        dBKeyword.setText(card.getKeyword().toUpperCase());
        dBHebrew.setText(card.getSuit().toUpperCase());
        dBCorr.setText(card.getCorrespondence().toUpperCase());
        dBDesc.setText(card.getDesc());
        Image image = new Image(getClass().getResourceAsStream("/cards/" + card.getImageFile()));
        dbCard.setImage(image);
        dbCard.setScaleX(-1);
    }

    //Scrolls through database when next arrow clicked
    @FXML
    void rightArrowClicked(MouseEvent event) throws InterruptedException, SQLException, ClassNotFoundException {
        if(majorViewed) {
            dbMajorCardNum++;
            majorCardScroll();
        } else {
            dbMinorCardNum++;
            minorCardScroll();
        }
    }

    @FXML
    void leftArrowClicked(MouseEvent event) throws InterruptedException, SQLException, ClassNotFoundException {
        if(majorViewed) {
            dbMajorCardNum--;
            majorCardScroll();
        } else {
            dbMinorCardNum--;
            minorCardScroll();
        }
    }

    //Displays different colours for rating slider based on selected rating
    private void setRatingSliderDetails() {
        ratingSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Image sad = new Image(getClass().getResourceAsStream("/icons/sad.png"));
            Image sadColor = new Image(getClass().getResourceAsStream("/icons/sadColor.png"));
            Image happy = new Image(getClass().getResourceAsStream("/icons/happy.png"));
            Image happyColor = new Image(getClass().getResourceAsStream("/icons/happyColor.png"));

            if (ratingSlider.getValue() == 1) {
                sadRating.setImage(sadColor);
                happyRating.setImage(happy);
            }
            if (ratingSlider.getValue() == 2) {
                sadRating.setImage(sad);
                happyRating.setImage(happy);
            }
            if (ratingSlider.getValue() == 3) {
                sadRating.setImage(sad);
                happyRating.setImage(happyColor);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //Displays daily card back
        Image cardBack = new Image(getClass().getResourceAsStream("/cards/back.png"));
        dailyCard.setImage(cardBack);

        //Changes journal entry text to white
        journalEntryText.setStyle("-fx-control-inner-background:#434242; -fx-text-fill: #FFFFFF; -fx-prompt-text-fill: #FFFFFF;");

        //Resets auto_id of log items such that numeric values always sequential
        try {
            logDBManager.resetKey();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        listLogEntries();

        //Sets rating slider colours
        setRatingSliderDetails();
    }

    //Hides all panes from the log list
    private void hideLogList() {
        savedEntryPane.setVisible(false);
        logListPane.setVisible(true);
        cardEntryPane.setVisible(false);
        ratingEntryPane.setVisible(false);
        deleteEntryButton.setVisible(false);
        returnButton.setVisible(false);
        deleteCheckButton.setVisible(false);
        cancelButton.setVisible(false);
        confirmLabel.setVisible(false);
    }

    //Minimizes the sub menu for lessons
    private void hideLessonsSubMenu() {
        menuVBox.getChildren().remove(introBtn);
        menuVBox.getChildren().remove(meaningBtn);
        menuVBox.getChildren().remove(minorBtn);
        menuVBox.getChildren().remove(majorBtn);
        introBtn.setVisible(false);
        meaningBtn.setVisible(false);
        minorBtn.setVisible(false);
        majorBtn.setVisible(false);
        lessonsExpanded = false;
    }

    //Minimizes the submenu for the database
    private void hideDatabaseSubMenu() {
        menuVBox.getChildren().remove(minorDBBtn);
        menuVBox.getChildren().remove(majorDBBtn);
        minorDBBtn.setVisible(false);
        majorDBBtn.setVisible(false);
        dbExpanded = false;
    }

    //Expands the lessons submenu
    private void createLessonsSubMenu() {
        menuVBox.getChildren().add(4, introBtn);
        menuVBox.getChildren().add(5, meaningBtn);
        menuVBox.getChildren().add(6, minorBtn);
        menuVBox.getChildren().add(7, majorBtn);
        introBtn.setVisible(true);
        meaningBtn.setVisible(true);
        minorBtn.setVisible(true);
        majorBtn.setVisible(true);
        lessonsExpanded = true;
    }

    //Expands the database submenu
    private void createDatabaseSubMenu() {
        menuVBox.getChildren().add(5, majorDBBtn);
        menuVBox.getChildren().add(6, minorDBBtn);
        majorDBBtn.setVisible(true);
        minorDBBtn.setVisible(true);
        dbExpanded = true;
    }

    //Resets positions of all information panes
    private void resetInfoPanes() {
        introPane2.setVisible(false);
        introPane2.setTranslateY(0);
        introPane3.setVisible(false);
        introPane3.setTranslateY(0);
        introPane4.setVisible(false);
        introPane4.setTranslateY(0);
        introPane5.setVisible(false);
        introPane5.setTranslateY(0);
        expandArrow.setTranslateY(0);
        expandArrow.setVisible(true);
    }

    //Sets introduction labels
    private void setIntroLabels() {
        headingLabel.setText("INTRODUCTION TO TAROT");
        infoLabel.setText("Traditionally tarot has been used for divination - as a method to 'look into the future'. However, this is absolutely not the most productive way to approach tarot.");
        infoLabel2.setText("A more interesting way to understand tarot is as a tool to unravel the unspoken voice of the self. Cards are something tangible on which we can project our feelings and thoughts.");
        infoLabel3.setText("The symbols in the tarot are pulled from a deep history of human myth and archetypes, part of the world that psychologist Carl Gustav Jung would call the collective unconscious.");
        infoLabel4.setText("Tarot allows us to put these archetypes together in various configurations so that we can see parallels between the stories within the cards and our own experiences.");
        infoLabel5.setText("The story you tell about yourself greatly influences the way you think about your future. Tarot is about helping you tell your story, finding facets you might have missed.");
    }

    //Sets meaning of tarot labels
    private void setMeaningLabels() {
        headingLabel.setText("THE MEANING OF MAGIC");
        infoLabel.setText("There is an ancient saying found in hermeticism, from which we can derive what magic really is: 'As above, so below'. Essentially, earth mirrors heaven, the macrocosm mirrors the microcosm.");
        infoLabel2.setText("'The world' is simply a mirror of our own inner worlds. The boundary between those two is arbitrary - and self and other are really just one.");
        infoLabel3.setText("If you understand yourself and your reality deeply, all paths of action will be clear. Hence the usefulness of the stories the tarot helps you tell.");
        infoLabel4.setText("Magic becomes simply understanding what each symbol does in relation to the others and the world, and interacting with them.");
        infoLabel5.setText("This meaning od magic teaches us that once we shed our inner light on the story reality is telling, we can see all parts at play, and transform it.");
    }

    //Sets major arcana info labels
    private void setMajorInfoLabels() {
        headingLabel.setText("THE MAJOR ARCANA");
        infoLabel.setText("Each of thee 22 major arcana is a stage in the journey from ignorance to wisdom, from the wide-eyed potential of The Fool to unity with The World.");
        infoLabel2.setText("These cards represent archetypes: crucial aspects in every life and situation. Having them in a reading highlights which archetypes are at play in your reading.");
        infoLabel3.setText("Because major arcana represent progression through life's stages, when you're looking at one, remember where it comes from and where it goes.");
        infoLabel4.setText("Don't ignore your intuition: even if each card has general meaning, the imagery might speak directly to you. Let it talk to you with your own voice.");
        infoLabel5.setText("Content matters: reading cards is all about weaving together universal aspects of the cards to tell a story. How did this card alongside the circumstances in your life?");
    }

    //Sets minor arcana info labels
    private void setMinorInfoLabels() {
        headingLabel.setText("THE MINOR ARCANA");
        infoLabel.setText("The 56 minor arcana deal with more specific, and less big-picture aspects of a reading. They let you look at the everyday details - each describing a point along a path in their particular kingdom.");
        infoLabel2.setText("Augments represent creativity, passion, desire, and will in the realm of the physical. Fire is the element. In the process of creation, wands are the spark of beginnings.");
        infoLabel3.setText("Chalices deal with emotions, relationships, and love. They're receptive and spiritual, and represent water. After having desire, one must also emotionally align to create.");
        infoLabel4.setText("Blades are about reason, logic, and intellectual endeavors. Action in the spiritual world. They are third in the path to creation, where one begins to plan. They represent air.");
        infoLabel5.setText("Oracles relate to worldly things: finances, sensuality, the body. They're receptive whilst Augments are active, and the final stage in creation, representing manifestation and all things earthly.");
    }

    public void handleClicks(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        //Brings dashboard to the front and resets submenus
        if(actionEvent.getSource() == dashboardBtn) {
            dashboard.toFront();
            headerLabel.setText("DASHBOARD");
            hideLogList();
            if(lessonsExpanded) {
                hideLessonsSubMenu();
            }
            if(dbExpanded) {
                hideDatabaseSubMenu();
            }
        }

        //Brings log list to the front and resets submenus
        if(actionEvent.getSource() == logBtn) {
            headerLabel.setText("LOG LIST");
            logList.toFront();
            if(lessonsExpanded) {
                hideLessonsSubMenu();
            }
            if(dbExpanded) {
                hideDatabaseSubMenu();
            }
        }

        //Expands the Lessons submenu
        if(actionEvent.getSource() == infoButton) {
            if(dbExpanded) {
                hideDatabaseSubMenu();
            }
            if(!lessonsExpanded) {
                createLessonsSubMenu();
            }
        }

        //Resets Info panes and sets labels to introduction texts
        if(actionEvent.getSource() == introBtn) {
            headerLabel.setText("INTRODUCTION");
            introPane.toFront();
            hideLogList();
            resetInfoPanes();
            setIntroLabels();
            infoState = "first";
        }

        //Resets Info panes and sets label to Meaning texts
        if(actionEvent.getSource() == meaningBtn) {
            headerLabel.setText("MEANING");
            introPane.toFront();
            hideLogList();
            resetInfoPanes();
            setMeaningLabels();
            infoState = "first";
        }

        //Resets Info panes and sets labels to Major Arcana texts
        if(actionEvent.getSource() == majorBtn) {
            headerLabel.setText("MAJOR INFO");
            introPane.toFront();
            hideLogList();
            resetInfoPanes();
            setMajorInfoLabels();
            infoState = "first";
        }

        //Resets Info panes and sets labels to Minor Arcana texts
        if(actionEvent.getSource() == minorBtn) {
            headerLabel.setText("MINOR INFO");
            introPane.toFront();
            hideLogList();
            resetInfoPanes();
            setMinorInfoLabels();
            infoState = "first";
        }

        //Expands database submenu when main menu clicked
        if(actionEvent.getSource() == dataMenuBtn) {
            if(lessonsExpanded) {
                hideLessonsSubMenu();
            }
            if(!dbExpanded) {
                createDatabaseSubMenu();
            }
        }

        //Displays database with previously selected major card
        if(actionEvent.getSource() == majorDBBtn) {
            dbPane.toFront();
            headerLabel.setText("MAJOR DATABASE");
            hideLogList();
            if(lessonsExpanded) {
                hideDatabaseSubMenu();
            }

            majorViewed = true;
            MajorCard card = majorDBManager.drawById(dbMajorCardNum);
            dBTitle.setText(card.getName().toUpperCase());
            dBKeyword.setText(card.getKeyword().toUpperCase());
            dBHebrew.setText(card.getHebrew().toUpperCase());
            dBCorr.setText(card.getCorrespondence().toUpperCase());
            dBDesc.setText(card.getDesc());
            Image image = new Image(getClass().getResourceAsStream("/cards/" + card.getImageFile()));
            dbCard.setImage(image);
            dbCard.setScaleX(-1);
        }

        if(actionEvent.getSource() == minorDBBtn) {
            dbPane.toFront();
            headerLabel.setText("MINOR DATABASE");
            hideLogList();
            if(lessonsExpanded) {
                hideDatabaseSubMenu();
            }

            majorViewed = false;
            MinorCard card = minorDBManager.drawById(dbMinorCardNum);
            dBTitle.setText(card.getName().toUpperCase() + " " +card.getSuit().toUpperCase());
            dBKeyword.setText(card.getKeyword().toUpperCase());
            dBHebrew.setText(card.getSuit().toUpperCase());
            dBCorr.setText(card.getCorrespondence().toUpperCase());
            dBDesc.setText(card.getDesc());
            Image image = new Image(getClass().getResourceAsStream("/cards/" + card.getImageFile()));
            dbCard.setImage(image);
            dbCard.setScaleX(-1);
        }
    }

    //Creates and adds all saved entries to log list
    public void listLogEntries() {
        try {
            logItems.getChildren().clear();
            List<LogItem> itemList = logDBManager.list();
            itemList.forEach((LogItem item) -> {
                try {
                    createEntries(item);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Creates a list of all items in log database
    public void createEntries(LogItem li) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LogItem.fxml"));
        Node pane = loader.load();
        LogItemController controller = loader.getController();
        logItems.getChildren().add(pane);
        controller.setEntryID("" + li.getId());
        controller.setEntryCard(li.getCard());
        controller.setEntryDate(li.getDate());
        if(li.getRating() == 1) {
            controller.setEntryRating("Negative");
        } if(li.getRating() == 3) {
            controller.setEntryRating("Positive");
        } else if (li.getRating() == 2) {
            controller.setEntryRating("Neutral");
        }
        pane.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #434242;");


        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                savedEntryPane.setVisible(true);
                logListPane.setVisible(false);
                cardEntryPane.setVisible(true);
                ratingEntryPane.setVisible(true);
                deleteCheckButton.setVisible(true);
                returnButton.setVisible(true);
                entryCard.setText("You drew " + li.getCard());
                if(li.getRating() == 1) {
                    Image sad = new Image(getClass().getResourceAsStream("/icons/sadRated.png"));
                    ratingEntryText.setText("You felt negatively about this card");
                    ratingEntryIcon.setImage(sad);
                } if(li.getRating() == 3) {
                    Image happy = new Image(getClass().getResourceAsStream("/icons/happyColor.png"));
                    ratingEntryText.setText("You felt positively about this card");
                    ratingEntryIcon.setImage(happy);
                } if (li.getRating() == 2) {
                    Image neutral = new Image(getClass().getResourceAsStream("/icons/neutralRated.png"));
                    ratingEntryText.setText("You felt neutral about this card");
                    ratingEntryIcon.setImage(neutral);
                }
                savedEntryLabel.setText(li.getEntry());
                itemViewed = li.getId();
            }
        });

        pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #3c3b3b;");
            }
        });

        pane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pane.setStyle("-fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #434242;");
            }
        });
    }


}

