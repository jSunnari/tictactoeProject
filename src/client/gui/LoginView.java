package client.gui;

/**
 * Login-form, also the startview:
 */

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Created by Jonas on 2016-05-20.
 */

public class LoginView extends VBox{

    private Label usernameLabel = new Label("Username:");
    private Label passwordLabel = new Label("Password:");
    private Label errorLabel = new Label();
    private TextField usernameTextField = new TextField();
    private PasswordField passwordTextField = new PasswordField();
    private Button loginBtn = new Button("Log in");
    private Button createAccountBtn = new Button("Create account");
    private Button settingsBtn = new Button("Connection settings");
    private Button quitBtn = new Button("Quit");
    private VBox vBox;

    public LoginView() {
        vBox = this;

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        vBox.getChildren().setAll(
                usernameLabel,
                usernameTextField,
                passwordLabel,
                passwordTextField,
                errorLabel,
                loginBtn,
                createAccountBtn,
                settingsBtn,
                quitBtn
                );

        loginBtn.getStyleClass().add("form-button");
        createAccountBtn.getStyleClass().add("form-button");
        settingsBtn.getStyleClass().add("form-button");
        errorLabel.getStyleClass().add("error-label");
        quitBtn.getStyleClass().add("form-button");
    }

    public void loginBtnListener (EventHandler<ActionEvent> buttonListener){
        loginBtn.setOnAction(buttonListener);
    }

    public void settingsBtnListener (EventHandler<ActionEvent> buttonListener){
        settingsBtn.setOnAction(buttonListener);
    }

    public void createAccountBtnListener (EventHandler<ActionEvent> buttonListener) {
        createAccountBtn.setOnAction(buttonListener);
    }

    public void quitBtnListener (EventHandler<ActionEvent> buttonListener) {
        quitBtn.setOnAction(buttonListener);
    }

    public String getPasswordTextField() {
        return passwordTextField.getText();
    }

    public String getUserTextField() {
        return usernameTextField.getText();
    }

    public void setErrorLabel(String message){
        errorLabel.setText(message);
    }

    public void clearFields(){
        usernameTextField.clear();
        passwordTextField.clear();
    }

}
