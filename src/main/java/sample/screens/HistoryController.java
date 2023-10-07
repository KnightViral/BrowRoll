package sample.screens;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entity.StyleProvider;
import sample.entity.WheelPoint;
import sample.entity.WheelPointSave;

import java.util.List;

public class HistoryController {

    @FXML
    public TableView<WheelPoint> mainTable;
    @FXML
    public TableColumn<WheelPoint, String> nameColumn;
    @FXML
    public TableColumn<WheelPoint, Double> multiplierColumn;

    @FXML
    void initialize() {
        initTable();
    }

    private void initTable() {
        initNameColumn();
        initMultiplierColumn();
        mainTable.getStylesheets().add(getClass().getResource(StyleProvider.getStyle()).toExternalForm());
        multiplierColumn.setSortType(TableColumn.SortType.DESCENDING);
        mainTable.getSortOrder().clear();
        mainTable.getSortOrder().add(multiplierColumn);
        mainTable.sort();
    }

    private void initNameColumn() {
        nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.50));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void initMultiplierColumn() {
        multiplierColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.50));
        multiplierColumn.setCellValueFactory(new PropertyValueFactory<>("multiplier"));
    }

    public void setWheelPoints(List<WheelPointSave> wheelPoints) {
        wheelPoints.forEach(e -> mainTable.getItems().add(e.load(0)));
    }
}
