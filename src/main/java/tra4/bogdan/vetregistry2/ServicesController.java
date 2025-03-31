package tra4.bogdan.vetregistry2;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicesController {
    @FXML
    private TextField inputFilterIme;

    @FXML
    private TableColumn<Service, String> servicesNameColumn;

    @FXML
    private TableColumn<Service, Integer> servicesCostColumn;

    @FXML
    private TableView<Service> servicesTable;

    @FXML
    private TableColumn<Service, Void> servicesActionColumn;

    public void initialize() {
        // Povezava stolpcev z lastnostmi modela `Clinic`
        servicesNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        servicesCostColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        servicesActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Izbriši");
            private final Button editButton = new Button("Uredi");
            private final HBox buttonContainer = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Service service = getTableView().getItems().get(getIndex());
                    System.out.println("Uredi: " + service.getId());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-service-form.fxml"));
                        EditServiceFormController controller = new EditServiceFormController(service);
                        loader.setController(controller);

                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle("Update Services");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();

                        loadServiceData();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                deleteButton.setOnAction(event -> {
                    Service service = getTableView().getItems().get(getIndex());
                    System.out.println("Brisanje: " + service.getId());
                    String query = "DELETE FROM services WHERE id = ?";
                    try (Connection connection = DatabaseConnection.connect();
                         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, service.getId());
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    loadServiceData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonContainer);
                }
            }

        });

        // Napolni tabelo s podatki iz baze
        loadServiceData();
    }

    private void loadServiceData() {
        ObservableList<Service> services = this.filterServices("");
        servicesTable.setItems(services);
    }



    @FXML
    private void showServiceForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-service-form.fxml"));
            AddServiceFormController controller = new AddServiceFormController();
            loader.setController(controller);

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Service");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadServiceData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyFilter(){
        String ime  = inputFilterIme.getText();
        ObservableList<Service> services = this.filterServices(ime);
        servicesTable.setItems(services);
    }

    public void clearFilter(){
        inputFilterIme.clear();
        applyFilter();
    }

    public ObservableList<Service> filterServices(String ime) {
        ObservableList<Service> services = FXCollections.observableArrayList();
        String query = "SELECT s.* FROM services s WHERE s.service ILIKE ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + ime + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Preberite podatke iz rezultata
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String service = resultSet.getString("service");
                int cost = resultSet.getInt("cost");


                // Dodajte zapis v seznam
                services.add(new Service(id, service, cost));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return services;
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }
}