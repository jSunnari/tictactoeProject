package client.gui;

import client.beans.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Jonas on 2016-05-27.
 */

public class ResultView extends VBox{
    private VBox root;
    private HighscoreTable highscoreTable = new HighscoreTable();
    private HBox personalResultsHbox = new HBox();
    private HBox highScoreHeaderHbox = new HBox();
    private HBox highScoreHbox = new HBox();
    private HBox headerHbox = new HBox();
    private HBox menuHbox = new HBox();
    private VBox wonMatchesVbox = new VBox();
    private VBox tieMatchesVbox = new VBox();
    private VBox lostMatchesVbox = new VBox();
    private VBox scoreVbox = new VBox();

    private User currentUser;

    private Label resultLabel = new Label("Your results");
    private Label highScoreLabel = new Label("Highscore");
    private Button backBtn = new Button("Back");
    private Label wonLabel = new Label("Won");
    private Label tieLabel = new Label("Tie");
    private Label lostLabel = new Label("Lost");
    private Label scoreLabel = new Label("Score");
    private Label wonPointsLbl = new Label();
    private Label tiePointsLbl = new Label();
    private Label lostPointsLbl = new Label();
    private Label scorePointsLbl = new Label();

    public ResultView() {
        root = this;

        root.setAlignment(Pos.CENTER);
        root.setSpacing(5);

        menuHbox.getChildren().add(backBtn);
        menuHbox.setAlignment(Pos.TOP_CENTER);

        highScoreHeaderHbox.getChildren().add(highScoreLabel);
        highScoreHeaderHbox.setAlignment(Pos.BOTTOM_CENTER);

        highScoreHbox.getChildren().add(highscoreTable);
        highScoreHbox.setAlignment(Pos.CENTER);

        wonMatchesVbox.getChildren().addAll(wonLabel, wonPointsLbl);
        wonMatchesVbox.setAlignment(Pos.CENTER);

        tieMatchesVbox.getChildren().addAll(tieLabel, tiePointsLbl);
        tieMatchesVbox.setAlignment(Pos.CENTER);

        lostMatchesVbox.getChildren().addAll(lostLabel, lostPointsLbl);
        lostMatchesVbox.setAlignment(Pos.CENTER);

        scoreVbox.getChildren().addAll(scoreLabel, scorePointsLbl);
        scoreVbox.setAlignment(Pos.CENTER);

        headerHbox.getChildren().add(resultLabel);
        headerHbox.setAlignment(Pos.BOTTOM_CENTER);

        personalResultsHbox.setSpacing(30);
        personalResultsHbox.setAlignment(Pos.CENTER);
        personalResultsHbox.getChildren().addAll(
                wonMatchesVbox,
                tieMatchesVbox,
                lostMatchesVbox,
                scoreVbox
        );

        root.getChildren().addAll(
                headerHbox,
                personalResultsHbox,
                highScoreHeaderHbox,
                highScoreHbox,
                menuHbox
        );

        headerHbox.setMinHeight(60);
        highScoreHeaderHbox.setMinHeight(60);
        menuHbox.setMinHeight(60);


        resultLabel.getStyleClass().add("form-header");
        highScoreLabel.getStyleClass().add("form-header");

        backBtn.getStyleClass().add("form-button");
        wonLabel.getStyleClass().add("result-label");
        lostLabel.getStyleClass().add("result-label");
        tieLabel.getStyleClass().add("result-label");
        scoreLabel.getStyleClass().add("result-label");

        wonPointsLbl.getStyleClass().add("result-label");
        lostPointsLbl.getStyleClass().add("result-label");
        tiePointsLbl.getStyleClass().add("result-label");
        scorePointsLbl.getStyleClass().add("result-label");

    }

    public void backBtnListener (EventHandler<ActionEvent> buttonListener){
        backBtn.setOnAction(buttonListener);
    }

    public void setHighscoreList(ObservableList<User> userObservableList){
        highscoreTable.setTableList(userObservableList);
    }

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;

        wonPointsLbl.setText(String.valueOf(currentUser.getWonMatches()));
        tiePointsLbl.setText(String.valueOf(currentUser.getTieMatches()));
        lostPointsLbl.setText(String.valueOf(currentUser.getLostMatches()));
        scorePointsLbl.setText(String.valueOf(currentUser.getRank()));
        resultLabel.setText("Your results, " + currentUser.getUsername());
    }
}
