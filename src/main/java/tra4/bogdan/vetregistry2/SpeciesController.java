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

public class SpeciesController {
    @FXML
    private TextField inputFilterIme;

    @FXML
    private TableColumn<Species, String> speciesNameColumn;

    @FXML
    private TableView<Species> speciesTable;

    @FXML
    private TableColumn<Species, Void> speciesActionColumn;

    public void initialize() {
        // Povezava stolpcev z lastnostmi modela `Clinic`
        speciesNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesActionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Uredi");
            private final Button deleteButton = new Button("Izbriši");
            private final Button detailsButton = new Button("Podrobnosti");
            private final HBox buttonContainer = new HBox(5);

            {
                editButton.setOnAction(event -> {
                    Species species = getTableView().getItems().get(getIndex());
                    System.out.println("Uredi: " + species.getId());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-species-form.fxml"));
                        EditSpeciesFormController controller = new EditSpeciesFormController(species);
                        loader.setController(controller);

                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle("Update Species");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();

                        loadSpeciesData();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                detailsButton.setOnAction(event -> {
                    Species species = getTableView().getItems().get(getIndex());
                    System.out.println("Podrobnosti: " + species.getId());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("view-species-form.fxml"));
                        ViewSpeciesFormController controller = new ViewSpeciesFormController(species);
                        loader.setController(controller);

                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle("Species Details");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                deleteButton.setOnAction(event -> {
                    Species species = getTableView().getItems().get(getIndex());
                    System.out.println("Brisanje: " + species.getId());
                    String query = "DELETE FROM species WHERE id = ?";
                    try (Connection connection = DatabaseConnection.connect();
                         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, species.getId());
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    loadSpeciesData();
                });

                // Get user status
                String userStatus = VetRegistryApplication.getCurrentUserStatus();

                // Add appropriate buttons based on user status
                if (userStatus != null && userStatus.equals("skrbnik")) {
                    // Admin user gets edit and delete buttons
                    buttonContainer.getChildren().add(deleteButton);
                    buttonContainer.getChildren().add(editButton);
                } else {
                    // Regular user gets only details button
                    buttonContainer.getChildren().add(detailsButton);
                }
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
        loadSpeciesData();
    }

    private void loadSpeciesData() {
        ObservableList<Species> species = this.filterSpecies("");
        speciesTable.setItems(species);
    }



    @FXML
    private void showSpeciesForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-species-form.fxml"));
            AddSpeciesFormController controller = new AddSpeciesFormController();
            loader.setController(controller);

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Species");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadSpeciesData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyFilter(){
        String ime  = inputFilterIme.getText();
        ObservableList<Species> species = this.filterSpecies(ime);
        speciesTable.setItems(species);
    }

    public void clearFilter(){
        inputFilterIme.clear();
        applyFilter();
    }

    public ObservableList<Species> filterSpecies(String ime) {
        ObservableList<Species> species = FXCollections.observableArrayList();
        String query = "SELECT s.* FROM species s WHERE s.species_name ILIKE ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + ime + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Preberite podatke iz rezultata
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("species_name");


                // Dodajte zapis v seznam
                species.add(new Species(id, name));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return species;
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) {
        Platform.exit();
    }
}
