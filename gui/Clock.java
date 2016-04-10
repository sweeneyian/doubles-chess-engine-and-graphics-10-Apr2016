package com.DoublesChess.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.DoublesChess.gui.GUIUtils.BLACK_COLOUR;
import static com.DoublesChess.gui.GUIUtils.RED_COLOUR;
import static com.DoublesChess.gui.GUIUtils.SELECTED_LIGHT_TILE_COLOUR;

/**
 * Created by Owner on 29/03/2016.
 */
public class Clock extends Parent {
    // private Calendar calendar = Calendar.getInstance();
    private Timeline delayTimeline, secondTimeline;
    private Duration gameTime, fisherBonus, originalTime;
    private boolean countingDown;
    private double MAX, MIN, value, hue;

    private Text timerText = new Text("hello");

    HBox hBox = new HBox();

    public Clock(Duration gameTime, Duration fisherBonus, boolean countingDown) {

        this.gameTime = gameTime;
        this.originalTime = gameTime;
        this.fisherBonus = fisherBonus;
        this.MAX = gameTime.toMillis();
        this.MIN = 0;
        this.hue = 1;
        this.value = MAX;
        this.timerText.setFont(Font.font(26));
        this.countingDown = countingDown;
        hBox.getChildren().add(timerText);
        getChildren().add(hBox);

        refreshClocks();
        play();
    }

    public void addFisherBonus(){
        gameTime = gameTime.add(fisherBonus);
        refreshClocks();
    }

    public void setCountingDown(boolean countingDown){
        this.countingDown = countingDown;
    }

    private void refreshClocks() {
        int hours = (int) gameTime.toHours();
        int minutes = (int) gameTime.toMinutes();
        int seconds = (int) gameTime.toSeconds();
        int milliseconds = (int) gameTime.toMillis();
        if (hours!=0)
            timerText.setText(String.valueOf(String.format("%02d:%02d:%02d.%01d", hours%12, minutes%60, seconds%60, milliseconds/100%10)));
        else
            timerText.setText(String.valueOf(String.format("%02d:%02d.%01d", minutes%60, seconds%60, milliseconds/100%10)));

        if (value/MAX >0.1) {
            timerText.setFill(Color.BLACK);
        }else {
            timerText.setFill(Color.RED);
        }

    }

    public void resetClock(){
        this.gameTime = originalTime;
        refreshClocks();
    }

    public void play() {
        // wait till start of next second then start a timeline to call refreshClocks() every second
        delayTimeline = new Timeline();
        delayTimeline.getKeyFrames().add(
                new KeyFrame(new Duration(1000 - (System.currentTimeMillis() % 1000)), new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent event) {
                        if (secondTimeline != null) {
                            secondTimeline.stop();
                        }
                        secondTimeline = new Timeline();
                        secondTimeline.setCycleCount(Timeline.INDEFINITE);
                        secondTimeline.getKeyFrames().add(
                                new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
                                    @Override public void handle(ActionEvent event) {
                                        if(countingDown && (gameTime.toMillis()>0)) {
                                            gameTime = gameTime.subtract(Duration.millis(100));
                                            value = gameTime.toMillis();
                                        }
                                        refreshClocks();
                                    }
                                }));
                        secondTimeline.play();
                    }
                })
        );
        delayTimeline.play();
    }

    public void stop(){
        delayTimeline.stop();

        if (secondTimeline != null) {
            secondTimeline.stop();
        }
    }
}