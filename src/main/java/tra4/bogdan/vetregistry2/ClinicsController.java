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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClinicsController {
    @FXML
    private TextField inputFilterIme;

    @FXML
    private TableColumn<Clinic, String> clinicTitleColumn;

    @FXML
    private TableColumn<Clinic, String> clinicAddressColumn;

    @FXML
    private TableColumn<Clinic, TownItem> clinicTownColumn;

    @FXML
    private TableColumn<Clinic, String> clinicPhoneNumberColumn;

    @FXML
    private TableColumn<Clinic, Void> clinicActionColumn;

    @FXML
    private TableView<Clinic> clinicTable;
    public void initialize() {
        // Povezava stolpcev z lastnostmi modela `Clinic`
        clinicTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        clinicAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        clinicTownColumn.setCellValueFactory(new PropertyValueFactory<>("town"));
        clinicPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        clinicActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Izbriši");
            private final Button editButton = new Button("Uredi");
            private final HBox buttonContainer = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Clinic clinic = getTableView().getItems().get(getIndex());
                    System.out.println("Uredi: " + clinic.getId());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-clinic-form.fxml"));
                        EditClinicFormController controller = new EditClinicFormController(clinic);
                        loader.setController(controller);

                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle("Update Clinic");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();

                        loadClinicData();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                deleteButton.setOnAction(event -> {
                    Clinic clinic = getTableView().getItems().get(getIndex());
                    System.out.println("Brisanje: " + clinic.getId());
                    String query = "DELETE FROM clinic WHERE id = ?";
                    try (Connection connection = DatabaseConnection.connect();
                         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, clinic.getId());
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    loadClinicData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonContainer);
                }
            }
        });

        // Napolni tabelo s podatki iz baze
        loadClinicData();
    }

    private void loadClinicData() {
        ObservableList<Clinic> clinics = this.filterClinics("");
        clinicTable.setItems(clinics);
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

            loadClinicData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyFilter(){
        String ime  = inputFilterIme.getText();
        ObservableList<Clinic> clinics = this.filterClinics(ime);
        clinicTable.setItems(clinics);
    }

    public void clearFilter(){
        inputFilterIme.clear();
        applyFilter();
    }

    public ObservableList<Clinic> filterClinics(String ime) {
        ObservableList<Clinic> clinics = FXCollections.observableArrayList();
        String query = "SELECT c.id, c.title, c.address, c.town_id, c.phone_number, t.name AS town_name FROM clinic c INNER JOIN town t ON t.id = c.town_id WHERE c.title ILIKE ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + ime + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Preberite podatke iz rezultata
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String address = resultSet.getString("address");
                int townId = resultSet.getInt("town_id");
                String townName = resultSet.getString("town_name");
                String phoneNumber = resultSet.getString("phone_number");

                // Dodajte zapis v seznam
                clinics.add(new Clinic(id, title, address, new TownItem(townId, townName), phoneNumber));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return clinics;
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }
}