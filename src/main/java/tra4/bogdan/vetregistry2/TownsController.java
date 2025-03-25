package tra4.bogdan.vetregistry2;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    public void initialize() {
        // Povezava stolpcev z lastnostmi modela `Clinic`
        townNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        townZipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));

        // Napolni tabelo s podatki iz baze
        loadClinicData();
    }

    private void loadClinicData() {
        ObservableList<Town> clinics = this.filterClinics("");
        townsTable.setItems(clinics);
    }



    @FXML
    private void showClinicForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-clinic-form.fxml"));
            AddClinicFormController controller = new AddClinicFormController();
            loader.setController(controller);

            Parent root = loader.load();
            controller.prefillTownComboBox(); // Prefill the combo box with data

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Clinic");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyFilter(){
        String ime  = inputFilterIme.getText();
        ObservableList<Town> towns = this.filterClinics(ime);
        townsTable.setItems(towns);
    }

    public void clearFilter(){
        inputFilterIme.clear();
        applyFilter();
    }

    public ObservableList<Town> filterClinics(String ime) {
        ObservableList<Town> towns = FXCollections.observableArrayList();
        String query = "SELECT t.* FROM town t";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            //preparedStatement.setString(1, "%" + ime + "%");
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