package com.cardium.cardieflash.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.Deck;
import com.cardium.cardieflash.MainApp;
import com.cardium.cardieflash.database.CardDb;
import com.cardium.cardieflash.database.DeckDb;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class CardViewController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private JFXMasonryPane masonryPane;

    @FXML
    private Button newCard;

    @FXML
    private Button editCard;

    @FXML
    private Button deleteCard;

    @FXML
    private Text deckIndicatorText;

    @Setter
    private MainApp mainApp;

    @Getter
    @Setter
    private Deck deck;

    private HashMap<StackPane, CardViewBlock> stackPaneToCardView = new HashMap<StackPane, CardViewBlock>();
    private List<CardViewBlock> selectedCards;
    private Stage dialogStage;

    @FXML
    private void initialize() {
    }

    @FXML
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void refreshPane() {
        DeckDb deckDb = mainApp.getDeckDb();
        HashMap<Integer, Card> cards = new HashMap<Integer, Card>();

        try {
            cards = deckDb.getCardsInDeck(deck);
        } catch (Exception e) {
            System.out.println(e);
        }
        deck.setCards(cards);

        try {
            masonryPane.getChildren().clear();

            for (Card card : deck.getCards().values()) {
                addNewCardToPane(card);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addNewCardToPane(Card card) {

        CardViewBlock cardView = new CardViewBlock(card, this.deck);
        // DeckViewBlock deckView = new DeckViewBlock(deck);
        StackPane stack = cardView.getStackPane();
        this.stackPaneToCardView.put(stack, cardView);
        masonryPane.getChildren().add(stack);

        stack.setOnMouseClicked(this::handleDeckViewAction);
    }

    @FXML
    void handleDeckViewAction(MouseEvent event) {
        CardViewBlock selected = this.stackPaneToCardView.get((StackPane) event.getTarget());
        Card card = selected.getCard();
        selected.select();
        loadSelectedCards();
        if (event.getClickCount() % 2 == 0) {
            selected.flip();
        }

    }

    public void updateText() {
        deckIndicatorText.setText(deck.getName());

    }

    @FXML
    void createNewCard(MouseEvent event) {
        ArrayList<Card>  createdCards = mainApp.showCardCreation(this.getDeck());
        createdCards.stream().forEach(this::addNewCardToPane);

    }

    @FXML
    void deleteCard(MouseEvent event) {
        String cardDeletionConfirmation = "You are about to delete " + selectedCards.size() + " cards. Are you sure?";

        Alert alert = this.createAlert(AlertType.CONFIRMATION, "Confirm Deletion", "Are you ok with this?");

        alert.setHeaderText(cardDeletionConfirmation);
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            CardDb cardDb = mainApp.getCardDb();
            selectedCards.forEach(cardView -> cardDb.delete(cardView.getCard()));
            this.refreshPane();
        } else {
        }

    }

    @FXML
    void editCard(MouseEvent event) {

    }

    public void loadSelectedCards() {
        selectedCards = stackPaneToCardView.values().stream().filter(CardViewBlock::isSelected)
                .collect(Collectors.toList());
        editCard.setDisable(selectedCards.size() != 1);

    }

    public Alert createAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert;
    }
}
