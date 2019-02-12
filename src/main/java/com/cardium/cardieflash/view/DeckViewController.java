package com.cardium.cardieflash.view;

import com.cardium.cardieflash.Deck;
import com.cardium.cardieflash.MainApp;
import com.cardium.cardieflash.database.DeckDb;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
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
    private List<Deck> selectedDecks;

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
        if (okClicked) {
            this.refreshPane();
        }
    }

    @FXML
    void deleteDeck(MouseEvent event) {
        StringBuilder deckDeletionConfirmation = new StringBuilder();
        deckDeletionConfirmation.append("You are going to delete: ");
        deckDeletionConfirmation
                .append(selectedDecks.stream().map(deck -> deck.getName()).collect(Collectors.joining(", ")));

        deckDeletionConfirmation.append(".");

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(deckDeletionConfirmation.toString());
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            DeckDb deckDb = mainApp.getDeckDb();
            selectedDecks.forEach(deckDb::delete);
            this.setInfoBox("", "", "");
            this.refreshPane();
        } else {
        }
    }

    @FXML
    void editDeck(MouseEvent event) {
        Deck deck = selectedDecks.get(0);
        TextInputDialog dialog = new TextInputDialog(deck.getName());
        dialog.setTitle("Editing: " + deck.getName());
        dialog.setHeaderText("You are currently editing " + deck.getName());
        dialog.setContentText("Please enter a new name: ");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (!deck.getName().equals(result.get())) {
                DeckDb deckDb = mainApp.getDeckDb();
                deck.setName(result.get());
                deckDb.updateDeck(deck);
                this.refreshPane();
                editDeck.setDisable(true);
            }
        }
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
        this.selectedDecks = checkboxList.stream().filter(CheckBox::isSelected).map(checkboxToDeckMap::get)
                .collect(Collectors.toList());

        if (checked.isSelected()) {
            Deck deck = checkboxToDeckMap.get(checked);
            this.setInfoBox(String.valueOf(deck.getDeckId()), deck.getName(), String.valueOf(deck.getTotalCardCount()));
        } else {
            this.setInfoBox("", "", "");
        }
        if (selectedDecks.size() != 1) {
            editDeck.setDisable(true);
        } else {
            editDeck.setDisable(false);
        }

    }

    public void setInfoBox(String id, String name, String cards) {
        deckId.setText(id);
        deckName.setText(name);
        totalCards.setText(cards);
    }

    public int selectedDecksCardCount() {
        return this.selectedDecks.stream().map(Deck::getTotalCardCount)
                .collect(Collectors.summingInt((Integer::intValue)));
    }
}
