package com.gui;

import com.observer.LoginObserver;
import com.repository.model.communication.LoginUserRequest;
import com.repository.model.communication.LoginUserResponse;
import com.repository.model.database.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


@Slf4j
@Controller
@Component
public class LoginPanel extends GuiPanel {

    @FXML
    public Label loginAfterReg;
    @FXML
    private PasswordField logPassw;
    @FXML
    private TextField emailLabel;
    @FXML
    private Label loginError;
    private User user;
    @Autowired
    private List<LoginObserver> loginObserverList;

    public LoginPanel() {
        super();
    }

    public void logButton() {
        if (emailLabel.getText().isEmpty()) {
            loginError.setText("Complete e-mail field!");
        }
        if (logPassw.getText().isEmpty()) {
            loginError.setText("Complete password field!");
        }
        if (logPassw.getText().isEmpty() && emailLabel.getText().isEmpty()) {
            loginError.setText("Complete required fields!");
        }

        LoginUserRequest loginUserRequest = new LoginUserRequest(emailLabel.getText(), logPassw.getText());
        LoginUserResponse loginUserResponse = clientControl.loginUserCommunication(loginUserRequest);
        log.info(loginUserResponse.getStatus());
        if ((loginUserResponse.getUser() == null) && (!emailLabel.getText().isEmpty()) && (!logPassw.getText().isEmpty())) {
            loginError.setText("Incorrect data. Try again!");
        }
        if (loginUserResponse.getUser() != null) {
            notifyGui(loginUserResponse.getUser());
            mainPanel.getMainLoad().getChildren().add(loadUi("/clientPanel"));

        }

    }

    private void notifyGui(User user) {
        loginObserverList.forEach(e -> e.update(user));
    }

    @Override
    public void update(User user) {
        this.user = user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
