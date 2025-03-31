package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    // Constructor receives the database connection
    public EditClinicFormController(Clinic clinic) {
        this.clinic = clinic;
    }

    public void initialize() {
        prefillTownComboBox(clinic.getTown());
        nameField.setText(clinic.getTitle());
        addressField.setText(clinic.getAddress());
        phoneNumberField.setText(clinic.getPhoneNumber());
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
            //clinic.setTitle(name);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Clinic updated successfully!");

            // Clear form fields
            nameField.clear();

            // Close modal dialog after success
            ((Stage) nameField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update clinic.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String invalidInput, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(invalidInput);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
