package tra4.bogdan.vetregistry2;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ChangePasswordController {
    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorLabel;

    private static String currentPassword = "admin"; // Default password

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

        // Validate current password
        if (!currentInput.equals(currentPassword)) {
            showError("Trenutno geslo ni pravilno.");
            return;
        }

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

        // Update password
        ChangePasswordController.currentPassword = newPassword;

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

    // Static method to get the current password for authentication
    public static String getCurrentPassword() {
        return currentPassword;
    }
}
