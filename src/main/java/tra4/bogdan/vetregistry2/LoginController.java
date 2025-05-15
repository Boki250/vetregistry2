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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        // Validate credentials against the users table in the database
        if (validateCredentials(username, password)) {
            try {
                // Store the current username in the application
                VetRegistryApplication.setCurrentUsername(username);

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

    private boolean validateCredentials(String username, String password) {
        // Connect to the database
        try (Connection conn = DatabaseConnection.connect()) {
            if (conn == null) {
                showError("Database connection failed");
                return false;
            }

            // Prepare SQL query to check credentials
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                // Execute query
                try (ResultSet rs = stmt.executeQuery()) {
                    // If a record is found, credentials are valid
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error: " + e.getMessage());
            return false;
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
