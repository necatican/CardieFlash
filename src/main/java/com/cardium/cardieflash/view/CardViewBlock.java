package com.cardium.cardieflash.view;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.Deck;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import lombok.Getter;
import lombok.Setter;

public class CardViewBlock {
    static final double PREF_ARC_WIDTH = 10;
    static final double PREF_ARC_HEIGHT = 10;

    static final double RECTANGLE_WIDTH = 150;
    static final double RECTANGLE_HEIGHT = 195;
    static final double COLOR_RECTANGLE_HEIGHT_DIFF = 6;
    static final double OUTER_RECTANGLE_SIZE_DIFF = 6;
    static final String OUTER_RECTANGLE_COLOR = "0x000000";
    static final String INNER_RECTANGLE_COLOR = "0xfffff0";
    static final double INNER_RECTANGLE_OPACITY = 0.5;

    @Getter
    StackPane stackPane;

    @Setter
    @Getter
    Card card;

    @Getter
    Deck deck;

    Rectangle innerRectangle;
    Rectangle deckColorRectangle;
    Rectangle outerRectangle;

    Text text;
    Boolean isSelected = false;
    Boolean isFlipped = false;

    public CardViewBlock(Card card, Deck deck) {
        this.card = card;
        this.deck = deck;

        stackPane = new StackPane();
        text = new Text(deck.getName());
        text.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.WHITE, 3, 0.9, 0, 0));

        outerRectangle = createRectangle(RECTANGLE_WIDTH + OUTER_RECTANGLE_SIZE_DIFF,
                RECTANGLE_HEIGHT + OUTER_RECTANGLE_SIZE_DIFF, Color.web(OUTER_RECTANGLE_COLOR));

        deckColorRectangle = createRectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT + COLOR_RECTANGLE_HEIGHT_DIFF,
                Color.web(this.deck.getColor()));

        innerRectangle = createRectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT, Color.web(INNER_RECTANGLE_COLOR));

        updateAlignments();

        stackPane.getChildren().addAll(outerRectangle, deckColorRectangle, innerRectangle, text);

        text.setMouseTransparent(true);
        innerRectangle.setMouseTransparent(true);
        outerRectangle.setMouseTransparent(true);
        deckColorRectangle.setMouseTransparent(true);

        // stackPane.setPickOnBounds(true);

    }

    public Rectangle createRectangle(double width, double height, Color color) {
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(color);
        rectangle.setArcWidth(PREF_ARC_WIDTH);
        rectangle.setArcHeight(PREF_ARC_HEIGHT);
        return rectangle;
    }

    public void setColor(String color) {
        deck.setColor(color);
    }

    public void updateColor() {
        innerRectangle.setFill(Color.web(deck.getColor()));
    }

    public void updateColor(String color) {
        innerRectangle.setFill(Color.web(color));
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public String getText() {
        return text.getText();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public boolean select() {
        isSelected = !isSelected;
        if (isSelected) {
            // new DropShadow(blurType, color, radius, spread, offsetX, offsetY)
            outerRectangle.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.RED, 14, 0.2, 0, 0));
        } else {
            outerRectangle.setEffect(null);
        }
        return isSelected;
    }

    public boolean flip() {
        isFlipped = !isFlipped;
        updateAlignments();
        return isFlipped;
    }

    public void updateAlignments() {
        if (!isFlipped) {
            StackPane.setAlignment(outerRectangle, Pos.CENTER);
            StackPane.setAlignment(deckColorRectangle, Pos.CENTER);
            StackPane.setAlignment(innerRectangle, Pos.CENTER);
            StackPane.setMargin(innerRectangle, new Insets(0, 0, COLOR_RECTANGLE_HEIGHT_DIFF, 0));

            setText(card.getFront());
        } else {
            StackPane.setAlignment(outerRectangle, Pos.BOTTOM_CENTER);
            StackPane.setAlignment(deckColorRectangle, Pos.BOTTOM_CENTER);
            StackPane.setAlignment(innerRectangle, Pos.BOTTOM_CENTER);
            StackPane.setMargin(innerRectangle, new Insets(COLOR_RECTANGLE_HEIGHT_DIFF, 0, 0, 0));

            setText(card.getBack());
        }

    }
}