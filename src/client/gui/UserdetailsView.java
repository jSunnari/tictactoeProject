package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * Created by Jonas on 2016-05-20.
 */

public class UserdetailsView extends VBox {
    private Label headerText = new Label("Create user");
    private Label usernameLabel = new Label("Username:");
    private Label emailLabel = new Label("Email:");
    private Label passwordLabel = new Label("Password:");
    private Label validationLabel = new Label();
    private TextField usernameTextField = new TextField();
    private TextField emailTextField = new TextField();
    private PasswordField passwordTextField = new PasswordField();
    private Button createAccountBtn = new Button("OK");
    private Button updateAccountBtn = new Button("Save changes");
    private Button backBtn = new Button("Back");
    private VBox vBox;

    public UserdetailsView() {
        vBox = this;

        vBox.getChildren().setAll(
                headerText,
                usernameLabel,
                usernameTextField,
                passwordLabel,
                passwordTextField,
                emailLabel,
                emailTextField,
                validationLabel,
                createAccountBtn,
                backBtn
        );

        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);

        VBox.setMargin(headerText, new Insets(0,0,30,0));
        createAccountBtn.getStyleClass().add("form-button");
        updateAccountBtn.getStyleClass().add("form-button");
        backBtn.getStyleClass().add("form-button");
        headerText.getStyleClass().add("form-header");

        usernameTextField.addEventFilter(KeyEvent.KEY_TYPED, maxLength(12));
    }

    public void createAccountBtn(EventHandler<ActionEvent> buttonListener){
        createAccountBtn.setOnAction(buttonListener);
    }

    public void updateAccountBtn(EventHandler<ActionEvent> buttonListener){
        updateAccountBtn.setOnAction(buttonListener);
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

    public void setUsername(String username){
        usernameTextField.setText(username);
    }

    public void setPassword(String password){
        passwordTextField.setText(password);
    }

    public void setEmail(String email){
        emailTextField.setText(email);
    }

    //Setting validation/error-label:
    public void setValidationLabel(String command, String message){
        switch (command) {
            case "error":
                validationLabel.getStyleClass().add("error-label");
                validationLabel.setText(message);
                break;
            case "created":
                validationLabel.getStyleClass().add("ok-label");
                clearFields();
                validationLabel.setText(message);
                break;
            case "updated":
                validationLabel.getStyleClass().add("ok-label");
                validationLabel.setText(message);
                break;

        }

    }

    //Check that all fields is filled:
    public boolean checkFields(){
        boolean error = true;
        if (
                !usernameTextField.getText().equals("")
                        &&
                !passwordTextField.getText().equals("")
                        &&
                !emailTextField.getText().equals("")){
            error = false;
        }
        return error;
    }

    //Initialize when "update user"
    public void initUpdateSettings(){
        headerText.setText("Account settings");
        vBox.getChildren().setAll(
                headerText,
                usernameLabel,
                usernameTextField,
                passwordLabel,
                passwordTextField,
                emailLabel,
                emailTextField,
                validationLabel,
                updateAccountBtn,
                backBtn
        );
    }

    //Clear fields:
    public void clearFields(){
        usernameTextField.clear();
        passwordTextField.clear();
        emailTextField.clear();
        validationLabel.setText("");
    }

    //Maxlength on username:
    public EventHandler<KeyEvent> maxLength(final Integer i) {
        return new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent arg0) {
                TextField tx = (TextField) arg0.getSource();
                if (tx.getText().length() >= i) {
                    arg0.consume();
                }
            }
        };
    }

}
