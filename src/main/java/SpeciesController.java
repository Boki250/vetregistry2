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

public class SpeciesController {
    @FXML
    private TextField inputFilterIme;

    @FXML
    private TableColumn<Species, String> speciesNameColumn;


    @FXML
    private TableView<Species> speciesTable;

    public void initialize() {
        // Povezava stolpcev z lastnostmi modela `Clinic`
        speciesNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Napolni tabelo s podatki iz baze
        loadSpeciesData();
    }

    private void loadSpeciesData() {
        ObservableList<Species> species = this.filterSpecies("");
        speciesTable.setItems(species);
    }



    @FXML
    private void showSpeciesForm() {
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
        ObservableList<Species> species = this.filterSpecies(ime);
        speciesTable.setItems(species);
    }

    public void clearFilter(){
        inputFilterIme.clear();
        applyFilter();
    }

    public ObservableList<Species> filterSpecies(String ime) {
        ObservableList<Species> species = FXCollections.observableArrayList();
        String query = "SELECT s.* FROM species s WHERE s.species_name ILIKE ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + ime + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Preberite podatke iz rezultata
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");


                // Dodajte zapis v seznam
                species.add(new Species(id, name));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return species;
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }
}