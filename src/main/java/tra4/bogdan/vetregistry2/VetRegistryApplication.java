package tra4.bogdan.vetregistry2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class VetRegistryApplication extends Application {
    private static Connection conn;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VetRegistryApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("Register veterinarskih klinik");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        conn =  DatabaseConnection.connect();
        launch();
    }

    public static Connection getConnection() { return conn; }
}