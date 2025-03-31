package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditTownFormController {
    @FXML private TextField nameField;
    private Town town;

    @FXML private TextField zipField;
    private Integer zip;

    // Constructor receives the database connection
    public EditTownFormController(Town town) {
        this.town = town;
    }

    public EditTownFormController (Integer zip) {
        this.zip = zip;
    }

    public void initialize() {
        nameField.setText(town.getName());
        zipField.setText(Integer.toString(town.getZip()));
    }

    @FXML
    private void handleUpdateTown() {
        String name = nameField.getText();
        Integer zip = Integer.parseInt(zipField.getText());

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izpolnjena.");
            return;
        }

        String sql = "UPDATE town SET name = ?, zip = ? WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, zip);
            pstmt.setInt(3, town.getId());

            pstmt.executeUpdate();
            town.setName(name);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Town updated successfully!");

            // Clear form fields
            nameField.clear();

            // Close modal dialog after success
            ((Stage) nameField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update town.");
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
