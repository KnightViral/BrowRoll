package sample.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import sample.Utils;
import sample.services.DonationAlertsConnection;

public class DAConnectionController {

    @FXML
    public TextField urlTF;
    @FXML
    public Label resultLabel;

    private DonationAlertsConnection connection;

    @FXML
    void initialize() {}

    @FXML
    public void onOpenRequest() {
        Utils.openWebpage(DonationAlertsConnection.BROWSER_LINK);
    }

    @FXML
    public void onConnect() {
        if (connection.connect(urlTF.getText())) {
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
