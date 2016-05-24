package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by Jonas on 2016-05-24.
 */

public class SettingsView extends VBox {
    private VBox vBox;
    private Label headerText = new Label("Connection settings");
    private Label ipLabel = new Label("Ip-number:");
    private Label portLabel = new Label("Port:");
    private TextField ipTextField = new TextField("localhost");
    private TextField portTextField = new TextField("50123");
    private Button backBtn = new Button("Back");


    public SettingsView() {
        vBox = this;

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        vBox.getChildren().addAll(
                headerText,
                ipLabel,
                ipTextField,
                portLabel,
                portTextField,
                backBtn
        );

        VBox.setMargin(headerText, new Insets(0,0,30,0));
        backBtn.getStyleClass().add("form-button");
        headerText.getStyleClass().add("form-header");
    }

    public void backBtnListener(EventHandler<ActionEvent> buttonListener){
        backBtn.setOnAction(buttonListener);
    }

    public String getIpNumber(){
       return ipTextField.getText();
    }

    public int getPort(){
        return Integer.parseInt(portTextField.getText());
    }
}
