package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditServiceFormController {
    @FXML private TextField costField;
    @FXML private TextField serviceField;
    private Service service;

    // Constructor receives the database connection
    public EditServiceFormController(Service service) {
        this.service = service;
    }

    public void initialize() {
        serviceField.setText(service.getName());
        costField.setText(String.valueOf(service.getCost()));
    }

    @FXML
    private void handleUpdateService() {
        String name = serviceField.getText();
        Integer cost = Integer.valueOf(costField.getText());

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izponjena.");
            return;
        }

        String sql = "UPDATE services SET service = ?, cost = ? WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, cost);
            pstmt.setInt(3, service.getId());

            pstmt.executeUpdate();
            service.setName(name);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Service updated successfully!");

            // Clear form fields
            serviceField.clear();

            // Close modal dialog after success
            ((Stage) serviceField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update service.");
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
