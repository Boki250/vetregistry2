package tra4.bogdan.vetregistry2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
}