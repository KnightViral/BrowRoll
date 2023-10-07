package sample.screens.fragments;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import sample.entity.Donation;

public class DonationFragment extends VBox {

    private final DonationsTable table;
    private Donation donation;
    private Label username;
    private Label message;
    private Label amount;

    public DonationFragment(DonationsTable table) {
        super();
        this.table = table;
        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.NEVER);
        setStyle("-fx-background-color: lightgray;" + "-fx-border-style: solid;"
                + "-fx-border-width: 1;" + "-fx-border-color: black;");
        setPadding(new Insets(5,5,5,5));

        VBox wrap = new VBox();
        VBox.setVgrow(wrap, Priority.ALWAYS);
        wrap.getChildren().add(initUsername());
        wrap.getChildren().add(initAmount());
        wrap.getChildren().add(initMessage());
        wrap.getChildren().add(initActions());
        getChildren().add(wrap);
    }

    private Region initUsername() {
        username = new Label();
        username.setStyle("-fx-font-size: 18px;");
        return username;
    }

    private Region initMessage() {
        message = new Label();
        message.setWrapText(true);
        message.setMaxHeight(120);
        VBox wrap = new VBox(message);
        VBox.setVgrow(wrap, Priority.ALWAYS);
        return wrap;
    }

    private Region initAmount() {
        amount = new Label();
        amount.setStyle("-fx-font-size: 15px;");
        HBox wrap = new HBox(amount);
        wrap.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        return wrap;
    }

    private Region initActions() {
        HBox body = new HBox();
        HBox bodyLeft = new HBox();
        HBox bodyRight = new HBox();
        body.getChildren().add(bodyLeft);
        body.getChildren().add(bodyRight);
        bodyRight.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        HBox.setHgrow(bodyRight, Priority.ALWAYS);
        Button addNew = new Button("Новый");
        Button delete = new Button("Убрать");
        addNew.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;" + "-fx-border-color: black;");
        delete.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;" + "-fx-border-color: black;");
        bodyLeft.getChildren().add(addNew);
        bodyRight.getChildren().add(delete);
        addNew.setOnAction(event -> {
            table.addWheelPoint(donation.getMessage(), donation.getAmount());
            table.getItems().remove(donation);
        });
        delete.setOnAction(event -> table.getItems().remove(donation));
        return body;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
        username.setText(String.format("%s", donation.getUsername()));
        message.setText(donation.getMessage());
        amount.setText(String.format("%s  %s", donation.getAmount(), donation.getCurrency()));
    }

}
