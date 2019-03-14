package com.cardium.cardieflash.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cardium.cardieflash.Deck;
import com.cardium.cardieflash.MainApp;
import com.cardium.cardieflash.database.DeckDb;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXMasonryPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;

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

    @FXML
    private JFXMasonryPane masonryPane;

    @FXML
    private ScrollPane scrollPane;
    private HashMap<StackPane, DeckViewBlock> stackPaneToDeckView;
    private MainApp mainApp;
    private List<DeckViewBlock> selectedDecks;
    private DeckDb deckDb;

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
        try {
            Deck deck = mainApp.showCreateDeckDialog();
            this.addNewDeckToPane(deck);

        } catch (RuntimeException e) {
            Alert alert = this.createAlert(AlertType.ERROR, "ERROR", "Couldn't create deck. " + e);
            alert.showAndWait();
        }
    }

    @FXML
    void deleteDeck(MouseEvent event) {
        StringBuilder deckDeletionConfirmation = new StringBuilder();
        deckDeletionConfirmation.append("You are going to delete: ");
        deckDeletionConfirmation.append(
                selectedDecks.stream().map(deckView -> deckView.getDeck().getName()).collect(Collectors.joining(", ")));

        deckDeletionConfirmation.append(".");
        Alert alert = this.createAlert(AlertType.CONFIRMATION, "Confirm Deletion", "Are you ok with this?");

        alert.setHeaderText(deckDeletionConfirmation.toString());
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deckDb = mainApp.getDeckDb();
            selectedDecks.forEach(deckView -> deckDb.delete(deckView.getDeck()));
            this.setInfoBox("", "", "");
            this.refreshPane();
        } else {
        }
    }

    @FXML
    void editDeck(MouseEvent event) {
        DeckViewBlock block = selectedDecks.get(0);
        Deck deck = block.getDeck();
        DeckViewBlock previewBlock = new DeckViewBlock(deck);

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Editing: " + deck.getName());
        dialog.setHeaderText("You are currently editing " + deck.getName());

        ButtonType buttonSubmit = new ButtonType("Submit");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().setAll(buttonSubmit, buttonCancel);

        JFXColorPicker colorPicker = new JFXColorPicker(Color.web(deck.getColor()));
        colorPicker.setOnAction(actionEvent -> {
            ColorPicker picker = (ColorPicker) actionEvent.getTarget();
            previewBlock.updateColor(String.valueOf(picker.getValue()));
        });

        TextField deckName = new TextField();
        deckName.setPromptText(deck.getName());
        deckName.setOnKeyReleased(actionEvent -> {
            TextField field = (TextField) actionEvent.getTarget();
            previewBlock.setText(field.getText());
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Desired name: "), 0, 0);
        grid.add(deckName, 1, 0);
        grid.add(new Label("Desired color: "), 0, 1);
        grid.add(colorPicker, 1, 1);
        grid.add(previewBlock.getStackPane(), 0, 2);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> deckName.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonSubmit) {
                return new Pair<>(deckName.getText(), String.valueOf(colorPicker.getValue()));
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(results -> {
            String decksName = results.getKey();
            String pickedColor = results.getValue();

            if (decksName.isBlank()) {
                Alert alert = this.createAlert(AlertType.ERROR, "Warning", "Deck name can't be blank.");
                alert.showAndWait();
            } else if (!decksName.equals(deck.getName())) {
                deckDb = mainApp.getDeckDb();
                deck.setName(decksName);
                block.setText(decksName);
                editDeck.setDisable(true);
                block.updateColor(pickedColor);

                String tempColor = deckDb.getColor(deck);
                System.out.println(tempColor);
                if (tempColor.equals(deckDb.getDefaultColor())) {
                    deckDb.setColor(deck, pickedColor);
                } else {
                    deckDb.updateColor(deck, pickedColor);
                }
                deckDb.updateDeck(deck);

                this.loadSelectedDecks();
            }

        });
    }

    public void refreshPane() {
        deckDb = mainApp.getDeckDb();
        ArrayList<Deck> deckList = deckDb.getAllDecks();
        stackPaneToDeckView = new HashMap<StackPane, DeckViewBlock>();

        try {
            masonryPane.getChildren().clear();

            for (Deck deck : deckList) {
                deck.setColor(deckDb.getColor(deck));
                this.addNewDeckToPane(deck);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void addNewDeckToPane(Deck deck) {
        DeckViewBlock deckView = new DeckViewBlock(deck);
        StackPane stack = deckView.getStackPane();
        this.stackPaneToDeckView.put(stack, deckView);
        masonryPane.getChildren().add(stack);

        stack.setOnMouseClicked(this::handleDeckViewAction);
    }

    @FXML
    void handleDeckViewAction(MouseEvent event) {
        DeckViewBlock selected = this.stackPaneToDeckView.get((StackPane) event.getTarget());
        Deck deck = selected.getDeck();
        selected.select();
        this.loadSelectedDecks();

        if (selected.isSelected()) {
            deck.setTotalCardCount(deckDb.getTotalCardCount(deck.getDeckId()));
            this.setInfoBox(String.valueOf(deck.getDeckId()), deck.getName(), String.valueOf(deck.getTotalCardCount()));
        } else {
            this.setInfoBox("", "", "");
        }

        if (event.getClickCount() == 2) {
            mainApp.showCardView(deck);
        }

    }

    public void loadSelectedDecks() {
        this.selectedDecks = this.stackPaneToDeckView.values().stream().filter(DeckViewBlock::isSelected)
                .collect(Collectors.toList());
        editDeck.setDisable(selectedDecks.size() != 1);

    }

    public void setInfoBox(String id, String name, String cards) {
        deckId.setText(id);
        deckName.setText(name);
        totalCards.setText(cards);
    }

    public int selectedDecksCardCount() {
        return this.selectedDecks.stream().map(DeckViewBlock::getDeck).map(Deck::getTotalCardCount)
                .collect(Collectors.summingInt((Integer::intValue)));
    }

    public Alert createAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert;
    }
}
