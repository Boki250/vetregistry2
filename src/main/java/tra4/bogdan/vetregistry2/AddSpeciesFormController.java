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

public class AddSpeciesFormController {
    @FXML private TextField nameField;

    private Connection conn;

    // Constructor receives the database connection
    public AddSpeciesFormController() {}

    @FXML
    private void handleAddSpecies() {
        String name = nameField.getText();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izponjena.");
            return;
        }

        String sql = "INSERT INTO species (species_name) VALUES (?)";

        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql)) {
            pstmt.setString(1, name);

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Species added successfully!");

            // Clear form fields
            nameField.clear();

            // Close modal dialog after success
            ((Stage) nameField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to insert species.");
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
