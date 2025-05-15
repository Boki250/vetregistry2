package tra4.bogdan.vetregistry2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class VetRegistryApplication extends Application {
    private static Connection conn;
    private static String currentUsername;
    private static String currentUserStatus;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VetRegistryApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setTitle("Login - Veterinary Registry");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        conn =  DatabaseConnection.connect();
        launch();
    }

    public static Connection getConnection() { return conn; }

    public static String getCurrentUsername() { return currentUsername; }

    public static void setCurrentUsername(String username) { currentUsername = username; }

    public static String getCurrentUserStatus() { return currentUserStatus; }

    public static void setCurrentUserStatus(String status) { currentUserStatus = status; }
}
