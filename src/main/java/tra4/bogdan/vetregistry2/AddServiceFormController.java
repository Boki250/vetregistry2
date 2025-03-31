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

public class AddServiceFormController {
    @FXML private TextField serviceField;
    @FXML private TextField costField;

    // Constructor receives the database connection
    public AddServiceFormController() {}

    @FXML
    private void handleAddService() {
        String service = serviceField.getText();
        Integer cost = Integer.valueOf(costField.getText());

        if (service.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izponjena.");
            return;
        }

        String sql = "INSERT INTO services (service, cost) VALUES (?, ?)";

        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql)) {
            pstmt.setString(1, service);
            pstmt.setInt(2, cost);

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Service added successfully!");

            // Clear form fields
            serviceField.clear();
            costField.clear();

            // Close modal dialog after success
            ((Stage) serviceField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to insert service.");
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
