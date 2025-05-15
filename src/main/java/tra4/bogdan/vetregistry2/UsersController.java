package tra4.bogdan.vetregistry2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersController {
    @FXML
    private TextField inputFilterUsername;

    @FXML
    private TableColumn<User, String> userUsernameColumn;

    @FXML
    private TableColumn<User, String> userStatusColumn;

    @FXML
    private TableColumn<User, Void> userActionColumn;

    @FXML
    private TableView<User> userTable;

    @FXML
    public void initialize() {
        // Check if current user has "skrbnik" or "skrnik" status
        String currentUserStatus = VetRegistryApplication.getCurrentUserStatus();
        if (!"skrbnik".equals(currentUserStatus) && !"skrnik".equals(currentUserStatus)) {
            // If not "skrbnik" or "skrnik", show an error message and return
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dostop zavrnjen");
            alert.setHeaderText("Nimate dovoljenja za dostop do tega pogleda");
            alert.setContentText("Za dostop do seznama uporabnikov potrebujete status 'skrbnik' ali 'skrnik'.");
            alert.showAndWait();
            return;
        }

        // Set up table columns
        userUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set up action column with view details button
        userActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button detailsButton = new Button("Podrobnosti");

            {
                detailsButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    showUserDetails(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                }
            }
        });

        // Load user data
        loadUserData();
    }

    private void loadUserData() {
        ObservableList<User> users = this.filterUsers("");
        userTable.setItems(users);
    }

    @FXML
    private void applyFilter() {
        String username = inputFilterUsername.getText();
        ObservableList<User> users = this.filterUsers(username);
        userTable.setItems(users);
    }

    @FXML
    private void clearFilter() {
        inputFilterUsername.clear();
        applyFilter();
    }

    public ObservableList<User> filterUsers(String username) {
        ObservableList<User> users = FXCollections.observableArrayList();
        String query = "SELECT u.id, u.username, u.password, s.title AS status FROM users u " +
                       "INNER JOIN status s ON s.id = u.status_id " +
                       "WHERE u.username ILIKE ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + username + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Read data from result set
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String status = resultSet.getString("status");

                // Add user to the list
                users.add(new User(id, userName, password, status));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return users;
    }

    private void showUserDetails(User user) {
        // Create an information dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Podrobnosti uporabnika");
        alert.setHeaderText("Uporabnik: " + user.getUsername());

        // Create content with user details
        String content = "ID: " + user.getId() + "\n" +
                         "Uporabniško ime: " + user.getUsername() + "\n" +
                         "Status: " + user.getStatus();

        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void showAddUserForm() {
        try {
            // Check if current user has "skrbnik" or "skrnik" status
            String currentUserStatus = VetRegistryApplication.getCurrentUserStatus();
            if (!"skrbnik".equals(currentUserStatus) && !"skrnik".equals(currentUserStatus)) {
                // If not "skrbnik" or "skrnik", show an error message and return
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Dostop zavrnjen");
                alert.setHeaderText("Nimate dovoljenja za dodajanje uporabnikov");
                alert.setContentText("Za dodajanje uporabnikov potrebujete status 'skrbnik' ali 'skrnik'.");
                alert.showAndWait();
                return;
            }

            // Load the add user form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-user-form.fxml"));
            Parent root = loader.load();

            // Get the controller and initialize it
            AddUserFormController controller = loader.getController();

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dodaj uporabnika");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(userTable.getScene().getWindow());

            // Set the scene
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            // Show the dialog and wait for it to close
            dialogStage.showAndWait();

            // Refresh the user table after adding a new user
            loadUserData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
