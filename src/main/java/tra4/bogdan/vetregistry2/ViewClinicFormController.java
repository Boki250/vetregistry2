package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViewClinicFormController {
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneNumberField;
    @FXML private ComboBox<TownItem> townIdField;
    @FXML private ListView<ServiceItem> serviceIdField;
    @FXML private ListView<SpeciesItem> speciesIdField;
    
    private Clinic clinic;

    // Constructor receives the clinic object
    public ViewClinicFormController(Clinic clinic) {
        this.clinic = clinic;
    }

    public void initialize() {
        // Set the text fields with the clinic data
        nameField.setText(clinic.getTitle());
        addressField.setText(clinic.getAddress());
        phoneNumberField.setText(clinic.getPhoneNumber());
        
        // Set the town ComboBox
        townIdField.getItems().add(clinic.getTown());
        townIdField.setValue(clinic.getTown());
        
        // Load services and species
        loadServices();
        loadSpecies();
        
        // Make all fields non-editable
        nameField.setEditable(false);
        addressField.setEditable(false);
        phoneNumberField.setEditable(false);
        townIdField.setDisable(true);
        serviceIdField.setDisable(true);
        speciesIdField.setDisable(true);
    }
    
    private void loadServices() {
        String sql = "SELECT s.id, s.service FROM services s " +
                     "JOIN services_clinic sc ON s.id = sc.services_id " +
                     "WHERE sc.clinic_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clinic.getId());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int serviceId = rs.getInt("id");
                    String serviceName = rs.getString("service");
                    serviceIdField.getItems().add(new ServiceItem(serviceId, serviceName));
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load services.");
            e.printStackTrace();
        }
    }
    
    private void loadSpecies() {
        String sql = "SELECT s.id, s.species_name FROM species s " +
                     "JOIN clinic_species cs ON s.id = cs.species_id " +
                     "WHERE cs.clinic_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clinic.getId());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int speciesId = rs.getInt("id");
                    String speciesName = rs.getString("species_name");
                    speciesIdField.getItems().add(new SpeciesItem(speciesId, speciesName));
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load species.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        // Close the dialog
        ((Stage) nameField.getScene().getWindow()).close();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}