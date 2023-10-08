package sample.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import sample.Utils;
import sample.entity.SaveLoadWizard;
import sample.services.DonationAlertsConnection;

public class DAConnectionController {

    @FXML
    public TextField urlTF;
    @FXML
    public Label resultLabel;
    @FXML
    public AnchorPane mainPane;

    private final static String TOKEN_FILENAME = "token";

    private DonationAlertsConnection connection;

    @FXML
    void initialize() {
        Object token = SaveLoadWizard.loadObject(TOKEN_FILENAME);
        if (token != null) {
            urlTF.setText(token.toString());
        }
    }

    @FXML
    public void onOpenRequest() {
        Utils.openWebpage(DonationAlertsConnection.BROWSER_LINK);
    }

    @FXML
    public void onConnect() {
        if (!urlTF.getText().isEmpty() && connection.connect(urlTF.getText())) {
            SaveLoadWizard.saveObject(urlTF.getText(), TOKEN_FILENAME);
            resultLabel.setText("Вы замечательны");
            resultLabel.setTextFill(Color.rgb(0, 225, 0));
        } else {
            resultLabel.setText("Что-то пошло не так.");
            resultLabel.setTextFill(Color.rgb(225, 0, 0));
        }
    }

    public void setConnection(DonationAlertsConnection connection) {
        this.connection = connection;
    }
}
