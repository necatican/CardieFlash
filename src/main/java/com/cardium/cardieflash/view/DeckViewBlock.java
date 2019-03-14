package com.cardium.cardieflash.view;

import com.cardium.cardieflash.Deck;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class DeckViewBlock {
    static final double RECTANGLE_WIDTH = 120;
    static final double RECTANGLE_HEIGHT = 110;
    static final double OUTER_RECTANGLE_SIZE_DIFFERENCE = 5;
    static final String OUTER_RECTANGLE_COLOR = "0x000000";
    static final double INNER_RECTANGLE_OPACITY = 0.5;
    static final double STROKE_WIDTH = 3;

    StackPane stackPane;
    Deck deck;
    Rectangle innerRectangle;
    Rectangle outerRectangle;
    Text text;
    Boolean isSelected = false;

    public DeckViewBlock(Deck deck) {
        this.stackPane = new StackPane();
        this.text = new Text(deck.getName());
        this.text.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.WHITE, 3, 0.9, 0, 0));
        this.deck = deck;

        this.innerRectangle = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        innerRectangle.setOpacity(0.5);

        this.outerRectangle = new Rectangle(RECTANGLE_WIDTH + OUTER_RECTANGLE_SIZE_DIFFERENCE,
                RECTANGLE_HEIGHT + OUTER_RECTANGLE_SIZE_DIFFERENCE);

        this.updateColor();
        outerRectangle.setFill(Color.web(OUTER_RECTANGLE_COLOR));

        stackPane.getChildren().add(outerRectangle);
        stackPane.getChildren().add(innerRectangle);
        stackPane.getChildren().add(text);

        text.setMouseTransparent(true);

        innerRectangle.setMouseTransparent(true);

        outerRectangle.setMouseTransparent(true);

        // stackPane.setPickOnBounds(true);

    }

    public Deck getDeck() {
        return this.deck;
    }

    public StackPane getStackPane() {
        return this.stackPane;
    }

    public void setColor(String color) {
        this.deck.setColor(color);
    }

    public void updateColor() {
        this.innerRectangle.setFill(Color.web(this.deck.getColor()));
    }

    public void updateColor(String color) {
        this.innerRectangle.setFill(Color.web(color));
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public String getText() {
        return this.text.getText();
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public boolean select() {
        this.isSelected = !isSelected;
        if (this.isSelected) {
            this.outerRectangle.setStrokeWidth(STROKE_WIDTH);
            this.outerRectangle.setStroke(Color.RED);
        } else {
            this.outerRectangle.setStrokeWidth(0);
        }
        return this.isSelected;
    }

}