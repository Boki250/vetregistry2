package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditSpeciesFormController {
    @FXML private TextField nameField;
    private Species species;

    // Constructor receives the database connection
    public EditSpeciesFormController(Species species) {
        this.species = species;
    }

    public void initialize() {
        nameField.setText(species.getName());
    }

    @FXML
    private void handleUpdateSpecies() {
        String name = nameField.getText();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izponjena.");
            return;
        }

        String sql = "UPDATE species SET species_name = ? WHERE id = ?";

        try (PreparedStatement pstmt = DatabaseConnection.connect().prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, species.getId());

            pstmt.executeUpdate();
            species.setName(name);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Species updated successfully!");

            // Clear form fields
            nameField.clear();

            // Close modal dialog after success
            ((Stage) nameField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update species.");
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
