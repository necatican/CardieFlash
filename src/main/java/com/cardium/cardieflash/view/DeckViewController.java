package com.cardium.cardieflash.view;

import com.cardium.cardieflash.Deck;
import com.cardium.cardieflash.MainApp;
import com.cardium.cardieflash.database.DeckDb;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

public class DeckViewController {

    @FXML
    private Label totalCards;

    @FXML
    private Label deckName;

    @FXML
    private Label deckId;

    @FXML
    private Button startGame;

    @FXML
    private AnchorPane viewPane;

    @FXML
    private Button newDeck;

    @FXML
    private Button editDeck;

    @FXML
    private Button deleteDeck;

    @FXML
    private VBox vbox;

    private ArrayList<CheckBox> checkboxList;
    private MainApp mainApp;
    private HashMap<CheckBox, Deck> checkboxToDeckMap;

    public DeckViewController() {
    }

    @FXML
    void addDeck(MouseEvent event) {

    }

    @FXML
    private void initialize() {
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }

    @FXML
    void createNewDeck(MouseEvent event) {

        boolean okClicked = mainApp.showCreateDeckDialog();
        if(okClicked)
        {
        this.refreshPane();}
    }

    public void refreshPane() {
        DeckDb deckDb = mainApp.getDeckDb();
        ArrayList<Deck> deckList = deckDb.getAllDecks();
        checkboxToDeckMap = new HashMap<CheckBox, Deck>();
        this.checkboxList = new ArrayList<CheckBox>();
        vbox.getChildren().clear();
        for (Deck deck : deckList) {
            CheckBox temp = new CheckBox(deck.getName());
            deck.setTotalCardCount(deckDb.getTotalCardCount(deck.getDeckId()));
            temp.setOnAction(this::handleCheckBoxAction);
            checkboxList.add(temp);
            checkboxToDeckMap.put(temp, deck);
            vbox.getChildren().add(temp);

        }

    }

    @FXML
    void handleCheckBoxAction(ActionEvent event) {
        CheckBox checked = (CheckBox) event.getTarget();
        if (checked.isSelected()) {
            Deck deck = checkboxToDeckMap.get(checked);
            deckId.setText(String.valueOf(deck.getDeckId()));
            deckName.setText(deck.getName());
            totalCards.setText(String.valueOf(deck.getTotalCardCount()));

        } else {
            deckId.setText("");
            deckName.setText("");
            totalCards.setText("");
        }

    }
}
