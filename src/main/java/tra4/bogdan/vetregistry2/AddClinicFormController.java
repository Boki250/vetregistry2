package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddClinicFormController {
    @FXML private TextField titleField;
    @FXML private TextField addressField;
    @FXML private ComboBox<TownItem> townIdField;
    @FXML private TextField phoneField;

    private Connection conn;

    // Constructor receives the database connection
    public AddClinicFormController() {}

    public void prefillTownComboBox() {
        conn = DatabaseConnection.connect();
        String sql = "SELECT id, name FROM town";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String townName = rs.getString("name");
                int townId = rs.getInt("id");
                // Assuming you have a ComboBox named townComboBox (not shown in the code)
                townIdField.getItems().add(new TownItem(townId, townName));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve towns.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddClinic() {
        String title = titleField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();

        if (title.isEmpty() || address.isEmpty() || townIdField.getValue() == null || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izponjena.");
            return;
        }

        String sql = "INSERT INTO clinic (title, address, town_id, phone_number) VALUES (?, ?, ?, ?)";
        int townId = townIdField.getValue().getTownId();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, address);
            pstmt.setInt(3, townId);
            pstmt.setString(4, phone);

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Clinic added successfully!");

            // Clear form fields
            titleField.clear();
            addressField.clear();
            townIdField.setValue(null);
            phoneField.clear();

            // Close modal dialog after success
            ((Stage) titleField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to insert clinic.");
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
