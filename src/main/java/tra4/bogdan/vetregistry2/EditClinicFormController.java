package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditClinicFormController {
    @FXML private TextField nameField;
    private Clinic clinic;

    @FXML private TextField addressField;

    @FXML private TextField phoneNumberField;

    @FXML private ComboBox<TownItem> townIdField;

    @FXML private ListView<ServiceItem> serviceIdField;

    // Constructor receives the database connection
    public EditClinicFormController(Clinic clinic) {
        this.clinic = clinic;
    }

    public void initialize() {
        prefillTownComboBox(clinic.getTown());
        prefillServiceListView();
        nameField.setText(clinic.getTitle());
        addressField.setText(clinic.getAddress());
        phoneNumberField.setText(clinic.getPhoneNumber());
        serviceIdField.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void prefillServiceListView() {
        String sql = "SELECT id, service FROM services";
        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String service = rs.getString("service");
                int serviceId = rs.getInt("id");
                serviceIdField.getItems().add(new ServiceItem(serviceId, service));
            }
            //serviceIdField.setValue(serviceItem);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve services.");
            e.printStackTrace();
        }
    }

    public void prefillTownComboBox(TownItem townItem) {
        String sql = "SELECT id, name FROM town";
        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String townName = rs.getString("name");
                int townId = rs.getInt("id");
                // Assuming you have a ComboBox named townComboBox (not shown in the code)
                townIdField.getItems().add(new TownItem(townId, townName));
            }
            townIdField.setValue(townItem);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve towns.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateClinic() {
        String name = nameField.getText();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izponjena.");
            return;
        }

        String sql = "UPDATE clinic SET title = ?, address = ?, town_id = ?, phone_number = ? WHERE id = ?";
        int townId = townIdField.getValue().getTownId();

        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(3, townId);
            pstmt.setString(4, phoneNumberField.getText());
            pstmt.setString(2, addressField.getText());
            pstmt.setInt(5, clinic.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update clinic.");
            e.printStackTrace();
        }

        String sql2 = "DELETE FROM services_clinic WHERE clinic_id = ?";

        try (PreparedStatement pstmt2 = DatabaseConnection.connect().prepareStatement(sql2)) {
            pstmt2.setInt(1, clinic.getId());
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update clinic.");
            e.printStackTrace();
        }

        String sql3 = "INSERT INTO services_clinic (clinic_id, services_id) VALUES (?, ?)";

        try (PreparedStatement pstmt3 = DatabaseConnection.connect().prepareStatement(sql3)) {
            var selectedItems = serviceIdField.getSelectionModel().getSelectedItems();

            for (ServiceItem item : selectedItems) {
                System.out.println("Izbrani element: " + item);
                pstmt3.setInt(1, clinic.getId());
                pstmt3.setInt(2, item.getServiceId());
                pstmt3.executeUpdate();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update clinic.");
            e.printStackTrace();
        }

        ((Stage) nameField.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType alertType, String invalidInput, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(invalidInput);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
