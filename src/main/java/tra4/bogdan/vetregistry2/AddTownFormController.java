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

public class AddTownFormController {
    @FXML private TextField nameField;
    @FXML private TextField zipField;

    private Connection conn;

    // Constructor receives the database connection
    public AddTownFormController() {}

    @FXML
    private void handleAddTown() {
        String name = nameField.getText();
        Integer zip = Integer.valueOf(zipField.getText());

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izponjena.");
            return;
        }

        String sql = "INSERT INTO town (name, zip) VALUES (?, ?)";

        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, zip);

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Town added successfully!");

            // Clear form fields
            nameField.clear();
            zipField.clear();

            // Close modal dialog after success
            ((Stage) nameField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to insert town.");
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
