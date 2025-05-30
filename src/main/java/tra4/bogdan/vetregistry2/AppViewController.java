package tra4.bogdan.vetregistry2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AppViewController {
    @FXML
    private BorderPane rootPane;

    @FXML
    private MenuItem usersMenuItem;

    @FXML
    private void initialize() {
        try {
            // Check if current user has "skrbnik" status
            String currentUserStatus = VetRegistryApplication.getCurrentUserStatus();
            // Hide the Users menu item if the user doesn't have "skrbnik" status
            if (!"skrbnik".equals(currentUserStatus)) {
                // If not "skrbnik", first disable the Users menu item
                usersMenuItem.setDisable(true);

                // Then use Platform.runLater to ensure the scene is fully loaded before trying to remove the menu item
                Platform.runLater(() -> {
                    Menu parentMenu = usersMenuItem.getParentMenu();
                    if (parentMenu != null) {
                        parentMenu.getItems().remove(usersMenuItem);
                    }
                });
            }

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
    private void showUsers() throws IOException {
        // Check if current user has "skrbnik" status
        String currentUserStatus = VetRegistryApplication.getCurrentUserStatus();
        if (!"skrbnik".equals(currentUserStatus)) {
            // If not "skrbnik", show an error message and return
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dostop zavrnjen");
            alert.setHeaderText("Nimate dovoljenja za dostop do tega pogleda");
            alert.setContentText("Za dostop do seznama uporabnikov potrebujete status 'skrbnik'.");
            alert.showAndWait();
            return;
        }

        Parent view = FXMLLoader.load(getClass().getResource("users.fxml"));
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

    @FXML
    private void handleLogout() {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potrditev odjave");
        alert.setHeaderText("Ali ste prepričani, da se želite odjaviti?");
        alert.setContentText("Vsi neshranjeni podatki bodo izgubljeni.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Load the login view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
                Parent root = loader.load();

                // Get the current stage
                Stage stage = (Stage) rootPane.getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root, 400, 300);
                stage.setTitle("Prijava");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
