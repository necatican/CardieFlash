package com.cardium.cardieflash.view;

import java.util.ArrayList;
import java.util.HashSet;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.Deck;
import com.cardium.cardieflash.MainApp;
import com.cardium.cardieflash.Tag;
import com.cardium.cardieflash.database.CardDb;
import com.cardium.cardieflash.database.DeckDb;
import com.cardium.cardieflash.database.TagDb;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class CardCreationController {

    @FXML
    private JFXTextField textFront;

    @FXML
    private JFXTextField textBack;

    @FXML
    private JFXChipView<String> chipView;

    @FXML
    private Button buttonNext;

    @FXML
    private Button buttonDone;

    @Setter
    private Deck deck;

    @Setter
    private MainApp mainApp;

    private HashSet<Tag> tagList;

    @Getter
    private ArrayList<Card> createdCards = new ArrayList<Card>();

    private Stage dialogStage;

    @FXML
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    void close(MouseEvent event) {
        dialogStage.close();
    }

    @FXML
    void nextScene(MouseEvent event) {
        buttonNext.setDisable(true);

        String front = textFront.getText();
        String back = textBack.getText();

        if (front.isEmpty() | back.isEmpty()) {
            Alert alert = this.createAlert(AlertType.ERROR, "Warning", "Front and back fields can't be blank.");
            alert.showAndWait();
            buttonNext.setDisable(false);

        } else {
            TagDb tagDb = mainApp.getTagDb();
            CardDb cardDb = mainApp.getCardDb();
            DeckDb deckDb = mainApp.getDeckDb();

            HashSet<String> chips = new HashSet<>();
            HashSet<String> savedTags = new HashSet<>();

            tagList.forEach(tag -> savedTags.add(tag.getName()));
            chipView.getChips().forEach(chip -> chips.add(chip));

            Card card = cardDb.createNewCard(front, back);
            deckDb.addCard(this.deck, card);
            createdCards.add(card);

            for (String chip : chips) {
                Tag tag;

                if (!savedTags.contains(chip)){
                    tag = tagDb.createNewTag(chip);
                    chipView.getSuggestions().add(chip);
                } else {
                    tag = tagDb.getTag(chip);
                }
                tagList.add(tag);

                try {
                    tagDb.addCard(tag, card);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            textFront.setText("");
            textBack.setText("");
            chipView.getChips().clear();
            buttonNext.setDisable(false);
        }
    }

    public void updateTagList() {
        TagDb tagDb = mainApp.getTagDb();
        this.tagList = tagDb.getAllTags();
        tagList.stream().forEach(tag -> chipView.getSuggestions().add(tag.getName()));
    }

    public Alert createAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert;
    }
}
