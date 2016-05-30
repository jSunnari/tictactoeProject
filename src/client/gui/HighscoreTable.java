package client.gui;

/**
 * The model for a Table based on the class User.
 */

import client.beans.User;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * Created by Jonas on 2016-03-03.
 */

public class HighscoreTable extends TableView<User>{

    private TableColumn<User, String> username = new TableColumn<User, String>("Username");
    private TableColumn<User, Integer> wonMatches = new TableColumn<User, Integer>("Won");
    private TableColumn<User, Integer> tieMatches = new TableColumn<User, Integer>("Tie");
    private TableColumn<User, Integer> lostMatches = new TableColumn<User, Integer>("Lost");
    private TableColumn<User, Integer> rank = new TableColumn<User, Integer>("Score");
    private TableColumn<User, Number> indexColumn = new TableColumn<User, Number>("#");

    public HighscoreTable() {
        this.setEditable(false);
        this.setPrefWidth(643);
        this.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);

        indexColumn.setSortable(false);
        indexColumn.setMinWidth(60);
        indexColumn.setMaxWidth(60);
        indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(this.getItems().indexOf(column.getValue())+1));


        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        username.setMinWidth(260);
        username.setMaxWidth(260);
        username.setSortable(false);
        wonMatches.setCellValueFactory(new PropertyValueFactory<User, Integer>("wonMatches"));
        wonMatches.setSortable(false);
        wonMatches.setMinWidth(wonMatches.getWidth());
        wonMatches.setMaxWidth(wonMatches.getWidth());
        tieMatches.setCellValueFactory(new PropertyValueFactory<User, Integer>("tieMatches"));
        tieMatches.setSortable(false);
        tieMatches.setMinWidth(tieMatches.getWidth());
        tieMatches.setMaxWidth(tieMatches.getWidth());
        lostMatches.setCellValueFactory(new PropertyValueFactory<User, Integer>("lostMatches"));
        lostMatches.setMinWidth(lostMatches.getWidth());
        lostMatches.setMaxWidth(lostMatches.getWidth());
        lostMatches.setSortable(false);
        rank.setCellValueFactory(new PropertyValueFactory<User,Integer>("rank"));
        rank.setMinWidth(rank.getWidth());
        rank.setMaxWidth(rank.getWidth());
        rank.setSortable(false);

        this.getColumns().setAll(indexColumn, username, wonMatches, tieMatches, lostMatches, rank);

        //Prevents dragging columns:
        this.widthProperty().addListener((source, oldWidth, newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) this.lookup("TableHeaderRow");
            header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    header.setReordering(false);
                }
            });
        });

    }

    public void setTableList (ObservableList<User> userObservableList){
        this.setItems(userObservableList);
    }

}
