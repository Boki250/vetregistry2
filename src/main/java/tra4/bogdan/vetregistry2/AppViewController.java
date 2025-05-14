package tra4.bogdan.vetregistry2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AppViewController {
    @FXML
    private BorderPane rootPane;

    @FXML
    private void initialize() {
        try {
            showClinics();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    private void showClinics() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("clinics.fxml"));
        rootPane.setCenter(view);
    }

    @FXML
    private void showTowns() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("towns.fxml"));
        rootPane.setCenter(view);
    }

    @FXML
    private void showSpecies() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("species.fxml"));
        rootPane.setCenter(view);
    }
    @FXML
    private void showServices() throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("services.fxml"));
        rootPane.setCenter(view);
    }

    @FXML
    private void showChangePasswordForm() {
        try {
            // Load the change password form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("change-password-form.fxml"));
            Parent root = loader.load();

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Menjava gesla");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(rootPane.getScene().getWindow());

            // Set the scene
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // Show the dialog and wait for it to close
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
