package tra4.bogdan.vetregistry2;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TownsController {
    @FXML
    private TextField inputFilterIme;

    @FXML
    private TableColumn<Town, String> townNameColumn;

    @FXML
    private TableColumn<Town, String> townZipColumn;

    @FXML
    private TableView<Town> townsTable;

    @FXML
    private TableColumn<Town, Void> townActionColumn;

    public void initialize() {
        // Povezava stolpcev z lastnostmi modela `Clinic`
        townNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        townZipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        townActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Izbriši");

            {
                button.setOnAction(event -> {
                    Town town = getTableView().getItems().get(getIndex());
                    System.out.println("Brisanje: " + town.getId());
                    String query = "DELETE FROM town WHERE id = ?";
                    try (Connection connection = DatabaseConnection.connect();
                         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, town.getId());
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    loadTownData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }

        });

        // Napolni tabelo s podatki iz baze
        loadTownData();
    }

    private void loadTownData() {
        ObservableList<Town> towns = this.filterTowns("");
        townsTable.setItems(towns);
    }



    @FXML
    private void showTownForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-town-form.fxml"));
            AddTownFormController controller = new AddTownFormController();
            loader.setController(controller);

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Town");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadTownData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyFilter(){
        String ime  = inputFilterIme.getText();
        ObservableList<Town> towns = this.filterTowns(ime);
        townsTable.setItems(towns);
    }

    public void clearFilter(){
        inputFilterIme.clear();
        applyFilter();
    }

    public ObservableList<Town> filterTowns(String ime) {
        ObservableList<Town> towns = FXCollections.observableArrayList();
        String query = "SELECT t.* FROM town t WHERE t.name ILIKE ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + ime + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Preberite podatke iz rezultata
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int zip = resultSet.getInt("zip");


                // Dodajte zapis v seznam
                towns.add(new Town(id, name, zip));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return towns;
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }
}