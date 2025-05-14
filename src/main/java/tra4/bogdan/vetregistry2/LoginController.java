package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // For simplicity, we're using hardcoded credentials
        // In a real application, you would validate against a database
        if (username.equals("admin") && password.equals("admin")) {
            try {
                // Load the main application view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("app-view.fxml"));
                Parent root = loader.load();
                
                // Get the current stage
                Stage stage = (Stage) usernameField.getScene().getWindow();
                
                // Set the new scene
                Scene scene = new Scene(root, 800, 800);
                stage.setTitle("Register veterinarskih klinik");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error loading application: " + e.getMessage());
            }
        } else {
            showError("Invalid username or password");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}