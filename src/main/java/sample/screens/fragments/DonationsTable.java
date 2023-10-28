package sample.screens.fragments;

import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.json.JSONObject;
import sample.Controller;
import sample.entity.Donation;
import sample.entity.WheelPoint;
import sample.services.DonationAlertsConnection;

public class DonationsTable extends TableView<Donation> {

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    private final Controller parentController;
    private final TableView<WheelPoint> targetTable;
    private DonationAlertsConnection connection;

    public DonationsTable(Controller parentController, TableView<WheelPoint> target) {
        super();
        this.parentController = parentController;
        this.targetTable = target;
        setTargetTable(target);
        initialize();
    }

    void initialize() {
        HBox.setMargin(this, new Insets(-1,-1,-1,-1));
        setEditable(false);
        HBox.setHgrow(this, Priority.SOMETIMES);
        VBox.setVgrow(this, Priority.ALWAYS);
        setSelectionModel(null);
        pseudoClassStateChanged(PseudoClass.getPseudoClass("donation-table"), true);
        setPlaceholder(new Label(""));

        TableColumn<Donation, Void> column = new TableColumn<>();
        column.prefWidthProperty().bind(this.widthProperty().multiply(1));
        DonationsTable thisTable = this;
        column.setCellFactory(new Callback<TableColumn<Donation, Void>, TableCell<Donation, Void>>() {

            @Override
            public TableCell<Donation, Void> call(TableColumn param) {
                return new TableCell<Donation, Void>() {

                    final DonationFragment fragment = new DonationFragment(thisTable);

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            fragment.setDonation(getItems().get(getIndex()));
                            setGraphic(fragment);
                        }
                        setText(null);
                    }
                };
            }
        });

        getColumns().add(column);

// тестовые
//        Donation d = new Donation();
//        d.setMessage("че опять устал стримить? 2 часика посидел и все, я вон 12 часов пашу в шахте и как огурчик с тян в кроватке шалю, а ты поиграл в игрушечку и все, эх вот бы тебе ремня хорошего реально попукать EZ EZ");
//        d.setAmount(1234);
//        d.setUsername("KnightViral");
//        d.setCurrency("RUB");
//        getItems().add(d);
//        Donation d2 = new Donation();
//        d2.setMessage("Short one");
//        d2.setUsername("StreamElementsWithBooty");
//        d2.setAmount(123);
//        d2.setCurrency("US");
//        getItems().add(d2);

        setRowFactory(tv -> {
            TableRow<Donation> row = new TableRow<>();
            row.setStyle("-fx-background-color: DimGray;");

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != (Integer) db.getContent(SERIALIZED_MIME_TYPE)) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                        targetTable.refresh();
                    }
                }
            });

            return row ;
        });
    }

    public void setTargetTable(TableView<WheelPoint> table) {
        table.setRowFactory(tv -> {
            TableRow<WheelPoint> row = new TableRow<>();

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (!row.isEmpty()) {
                        for (Node node : table.lookupAll(".table-row-cell")) {
                            if (node instanceof TableRow) {
                                node.pseudoClassStateChanged(PseudoClass.getPseudoClass("important"), ((TableRow<?>) node).getIndex() == row.getIndex());
                            }
                        }
                    }
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Donation dragged = getItems().remove(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = table.getItems().size() ;
                    } else {
                        dropIndex = row.getIndex();
                    }

                    if (table.getItems().size() > dropIndex) {
                        WheelPoint wheelPoint = table.getItems().get(dropIndex);
                        wheelPoint.setMultiplier(wheelPoint.getMultiplier() + dragged.getAmount());
                    } else {
                        addWheelPoint(dragged.getMessage(), dragged.getAmount());
                    }
                    event.setDropCompleted(true);
                    event.consume();
                }
                parentController.countAllMultipliers();
                Platform.runLater(() -> {
                    parentController.sort();
                    table.refresh();
                });
            });

            return row ;
        });
    }

    public void setHidden(boolean hidden) {
        setVisible(!hidden);
        if (hidden) {
            setPrefWidth(0);
        } else {
            setPrefWidth(-1);
        }
    }

    public void addWheelPoint(String text, double value) {
        parentController.addWheelPoint(text, value);
    }

    public void addDonation(Donation donation) {
        getItems().add(donation);
        setHidden(false);
    }

    public void setConnection(DonationAlertsConnection connection) {
        if (this.connection != connection) {
            this.connection = connection;
            connection.addMessageHandler(message -> {
                try {
                    addDonation(new Donation(new JSONObject(message)));
                } catch (Exception e) {
                    System.out.println("Failed to parse Donation.");
                }
            });
        }

    }
}
