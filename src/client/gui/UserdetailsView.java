package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by Jonas on 2016-05-20.
 */

public class UserdetailsView extends VBox {
    private Label headerText = new Label("Create user");
    private Label usernameLabel = new Label("Username:");
    private Label emailLabel = new Label("Email");
    private Label passwordLabel = new Label("Password:");
    private Label errorLabel = new Label();
    private TextField usernameTextField = new TextField();
    private TextField emailTextField = new TextField();
    private PasswordField passwordTextField = new PasswordField();
    private Button createAccountBtn = new Button("OK");
    private Button updateAccountBtn = new Button("Save changes");
    private Button backBtn = new Button("Back");
    private VBox vBox;

    public UserdetailsView() {
        vBox = this;

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        vBox.getChildren().setAll(
                headerText,
                usernameLabel,
                usernameTextField,
                passwordLabel,
                passwordTextField,
                emailLabel,
                emailTextField,
                errorLabel,
                createAccountBtn,
                backBtn
        );

        VBox.setMargin(headerText, new Insets(0,0,20,0));
        createAccountBtn.getStyleClass().add("form-button");
        backBtn.getStyleClass().add("form-button");
        errorLabel.getStyleClass().add("error-label");
        headerText.getStyleClass().add("form-header");
    }

    public void createAccountBtn(EventHandler<ActionEvent> buttonListener){
        createAccountBtn.setOnAction(buttonListener);
    }

    public void backBtnListener(EventHandler<ActionEvent> buttonListener){
        backBtn.setOnAction(buttonListener);
    }

    public String getUsername(){
        return usernameTextField.getText();
    }

    public String getPassword(){
        return passwordTextField.getText();
    }

    public String getEmail(){
        return emailTextField.getText();
    }

}
