package com.cardium.cardieflash.view;

import java.util.ArrayList;
import java.util.HashSet;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.Deck;
import com.cardium.cardieflash.MainApp;
import com.cardium.cardieflash.Tag;
import com.cardium.cardieflash.database.CardDb;
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

public class CardEditController {

    @FXML
    private JFXTextField textFront;

    @FXML
    private JFXTextField textBack;

    @FXML
    private JFXChipView<String> chipView;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    @Setter
    private Deck deck;

    @Setter
    private MainApp mainApp;

    private HashSet<Tag> tagList;

    private HashSet<Tag> tagsBeforeEdit;

    @Setter
    private CardViewBlock block;

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
    void applyEdit(MouseEvent event) {
        applyButton.setDisable(true);

        Card card = block.getCard();
        String front = textFront.getText();
        String back = textBack.getText();

        Card newCard = new Card(card.getCid(), front, back);

        if (front.trim().isEmpty() | back.trim().isEmpty()) {
            Alert alert = this.createAlert(AlertType.ERROR, "Warning", "Front and back fields can't be blank.");
            alert.showAndWait();
            applyButton.setDisable(false);

        } else if (!newCard.equals(card)) {

            CardDb cardDb = mainApp.getCardDb();
            TagDb tagDb = mainApp.getTagDb();
            cardDb.updateCard(newCard);

            HashSet<String> tagNamesBeforeEdit = new HashSet<>();
            HashSet<String> tagsAfterEdit = new HashSet<>();

            ArrayList<String> tagsToRemove = new ArrayList<>();

            tagsBeforeEdit.forEach(tag -> tagNamesBeforeEdit.add(tag.getName()));
            chipView.getChips().forEach(chip -> tagsAfterEdit.add(chip));

            if (!(tagNamesBeforeEdit == tagsAfterEdit)) {
                tagNamesBeforeEdit.stream().forEach(e -> {
                    if (!tagsAfterEdit.contains(e)) {
                        tagsToRemove.add(e);
                    } else {
                        tagsAfterEdit.remove(e);
                    }
                });

                cardDb.removeTag(card.getCid(), tagsToRemove);
                tagsAfterEdit.stream().forEach(tagName -> {
                    Tag tag;
                    try {
                        tag = tagDb.createNewTag(tagName);
                    } catch (Exception e) {
                        tag = tagDb.getTag(tagName);
                    }

                    tagDb.addCard(tag, card);
                });

            }
            block.setCard(new Card(card.getCid(), front, back));

            block.updateAlignments();
            dialogStage.close();

        }


    }

    public void updateTextBlocks(String front, String back)
    {
        textFront.setText(front);
        textBack.setText(back);
    }
    public void updateTagList() {
        TagDb tagDb = mainApp.getTagDb();
        CardDb cardDb = mainApp.getCardDb();
        tagList = tagDb.getAllTags();
        tagsBeforeEdit = cardDb.getTags(block.getCard());
        tagsBeforeEdit.stream().forEach(tag -> chipView.getChips().add(tag.getName()));
        tagList.stream().forEach(tag -> chipView.getSuggestions().add(tag.getName()));
    }

    public Alert createAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert;
    }
}
