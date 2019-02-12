package com.cardium.cardieflash.view;

import com.cardium.cardieflash.MainApp;
import com.cardium.cardieflash.database.DeckDb;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;

public class CreateDeckController {

    @FXML
    private TextField deckNameTextBox;

    @FXML
    private Button createConfirmation;

    @FXML
    private Button createCancel;

    private boolean okClicked = false;
    private Stage dialogStage;

    @Setter
    private MainApp mainApp;

    @FXML
    private void initialize() {

    }

    @FXML
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    void clickCancel(MouseEvent event) {
        dialogStage.close();
    }

    @FXML
    void clickConfirm(MouseEvent event) {
        String userQuery = deckNameTextBox.getText();
        if (userQuery.length() < 3) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Deck name warning");
            alert.setHeaderText("Deck name error.");
            alert.setContentText("Deck names must consist of at least 3 characters.");
            alert.showAndWait();
        } else {
            DeckDb deckDb = mainApp.getDeckDb();
            deckDb.createNewDeck(userQuery);
            okClicked = true;
            dialogStage.close();
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

}
