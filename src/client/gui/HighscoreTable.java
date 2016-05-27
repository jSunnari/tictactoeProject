package client.gui;

/**
 * The model for a Table based on the class User.
 */

import client.beans.User;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


/**
 * Created by Jonas on 2016-03-03.
 */

public class HighscoreTable extends TableView<User>{

    private TableColumn<User, String> username = new TableColumn<User, String>("Username");
    private TableColumn<User, Integer> rank = new TableColumn<User, Integer>("Score");

    public HighscoreTable() {
        this.setEditable(true);

        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        username.setPrefWidth(266);
        rank.setCellValueFactory(new PropertyValueFactory<User,Integer>("rank"));
        rank.setPrefWidth(266);

        this.getColumns().setAll(username, rank);

    }

    public void setTableList (ObservableList<User> userObservableList){
        this.setItems(userObservableList);
    }

    public void doubleClickRow(EventHandler<MouseEvent> listener){
        this.setOnMouseClicked(listener);
    }

}
