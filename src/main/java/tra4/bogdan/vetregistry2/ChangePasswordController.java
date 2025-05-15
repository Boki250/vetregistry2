package tra4.bogdan.vetregistry2;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePasswordController {
    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    // No longer using static password - now using database

    @FXML
    private void initialize() {
        // Hide error label initially
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleChangePassword() {
        String currentInput = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate new password
        if (newPassword.isEmpty()) {
            showError("Novo geslo ne sme biti prazno.");
            return;
        }

        // Validate password confirmation
        if (!newPassword.equals(confirmPassword)) {
            showError("Novo geslo in potrditev se ne ujemata.");
            return;
        }

        // Validate current password against database and update password
        if (!validateAndUpdatePassword(currentInput, newPassword)) {
            showError("Trenutno geslo ni pravilno.");
            return;
        }

        // Show success message
        showSuccess("Geslo je bilo uspešno spremenjeno.");

        // Close the dialog after a short delay
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(event -> closeDialog());
        pause.play();
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void showError(String message) {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void closeDialog() {
        Stage stage = (Stage) currentPasswordField.getScene().getWindow();
        stage.close();
    }

    // Validate current password against database and update password if valid
    private boolean validateAndUpdatePassword(String currentPassword, String newPassword) {
        try (Connection conn = DatabaseConnection.connect()) {
            if (conn == null) {
                showError("Database connection failed");
                return false;
            }

            // Get the current username from the application
            String username = VetRegistryApplication.getCurrentUsername();
            if (username == null || username.isEmpty()) {
                showError("No user is currently logged in");
                return false;
            }

            // First, validate the current password for the logged-in user
            String validateSql = "SELECT username FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement validateStmt = conn.prepareStatement(validateSql)) {
                validateStmt.setString(1, username);
                validateStmt.setString(2, currentPassword);

                try (ResultSet rs = validateStmt.executeQuery()) {
                    if (!rs.next()) {
                        // No user found with the given username and password
                        return false;
                    }

                    // Now update the password for this user
                    String updateSql = "UPDATE users SET password = ? WHERE username = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setString(1, newPassword);
                        updateStmt.setString(2, username);

                        int rowsAffected = updateStmt.executeUpdate();
                        return rowsAffected > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: " + e.getMessage());
            return false;
        }
    }

    // No longer using static method to get password - now using database
}
