package client.logic;

/**
 * ClientController
 */

import client.beans.User;
import client.gui.LoginView;
import client.gui.MainView;
import client.gui.UserdetailsView;

/**
 * Created by Jonas on 2016-05-20.
 */

public class ClientController{
    private MainView mainView;
    private LoginView loginView;
    private UserdetailsView userdetailsView = new UserdetailsView();

    //Gets the mainview and loginform from ClientApp-class (main-class).
    public ClientController(MainView mainView, LoginView loginView) {
        this.mainView = mainView;
        this.loginView = loginView;

        /**
         * LoginView, Listeners:
         */
        //LOG IN - call method login():
        loginView.loginBtnListener(event -> login());

        //CREATE ACCOUNT(Menu button) - Change the content to the userDetailsView:
        loginView.createAccountBtnListener(event -> mainView.setMainContent(userdetailsView));

        //QUIT GAME - closes application:
        loginView.quitBtnListener(event1 -> System.exit(0));

        /**
         * UserdetailsView, Listeners:
         */
        //CREATE ACCOUNT - call method createAccount():
        userdetailsView.createAccountBtn(event1 -> createAccount());

        //BACK - switches to the loginform again.
        userdetailsView.backBtnListener(event -> mainView.setMainContent(loginView));

    }

    /**
     * LOG IN,
     * Validation and
     */
    void login() {
        String username = loginView.getUserTextField();
        String password = loginView.getPasswordTextField();
        if (!username.equals("") && !password.equals("")) {
            loginView.setErrorLabel("");
            System.out.println(username + " " + password);
        }
        else {
            loginView.setErrorLabel("Fyll i alla fält.");
        }
    }

    void createAccount() {
        String username = userdetailsView.getUsername();
        String password = userdetailsView.getPassword();
        String email = userdetailsView.getEmail();

        User user = new User(username,password,email);
    }


}
