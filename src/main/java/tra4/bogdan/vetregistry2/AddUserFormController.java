package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddUserFormController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> statusField;

    private Connection conn;

    // Constructor
    public AddUserFormController() {}

    @FXML
    public void initialize() {
        conn = DatabaseConnection.connect();
        prefillStatusComboBox();
    }

    private void prefillStatusComboBox() {
        String sql = "SELECT title FROM status";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String statusTitle = rs.getString("title");
                statusField.getItems().add(statusTitle);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve status options.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String status = statusField.getValue();

        if (username.isEmpty() || password.isEmpty() || status == null) {
            showAlert(Alert.AlertType.ERROR, "Vpisani podatek ni pravilen", "Vsa polja morajo biti izponjena.");
            return;
        }

        // Check if username already exists
        if (usernameExists(username)) {
            showAlert(Alert.AlertType.ERROR, "Uporabniško ime že obstaja", "Izberite drugo uporabniško ime.");
            return;
        }

        // Get status ID from status title
        int statusId = getStatusId(status);
        if (statusId == -1) {
            showAlert(Alert.AlertType.ERROR, "Napaka", "Izbrani status ne obstaja.");
            return;
        }

        String sql = "INSERT INTO users (username, password, status_id) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, statusId);

            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Uspeh", "Uporabnik uspešno dodan!");

            // Clear form fields
            usernameField.clear();
            passwordField.clear();
            statusField.setValue(null);

            // Close modal dialog after success
            ((Stage) usernameField.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to insert user.");
            e.printStackTrace();
        }
    }

    private boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getStatusId(String statusTitle) {
        String sql = "SELECT id FROM status WHERE title = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, statusTitle);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}