package com.cardium.cardieflash;

import java.io.IOException;
import com.cardium.cardieflash.database.CardDb;
import com.cardium.cardieflash.database.Database;
import com.cardium.cardieflash.database.DeckDb;
import com.cardium.cardieflash.database.TagDb;
import com.cardium.cardieflash.view.CreateDeckController;
import com.cardium.cardieflash.view.DeckViewController;
import lombok.Getter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Getter
    private CardDb cardDb;

    @Getter
    private DeckDb deckDb;

    @Getter
    private TagDb tagDb;

    @Getter
    private Database database;

    public MainApp() {
        database = new Database("jdbc:sqlite:src/main/resources/db/cards.db");
        database.startConnection();
        cardDb = new CardDb(database);
        deckDb = new DeckDb(database);
        tagDb = new TagDb(database);

    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("FlashCardie");

        initRootLayout();
        showDeckView();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDeckView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/DeckView.fxml"));
            AnchorPane deckView = (AnchorPane) loader.load();

            rootLayout.setCenter(deckView);

            DeckViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.refreshPane();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showCreateDeckDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CreateDeck.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create a new deck");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CreateDeckController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}