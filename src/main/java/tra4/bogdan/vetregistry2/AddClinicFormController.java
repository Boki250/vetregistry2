package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.sql.Connection;

public class AddClinicFormController {
    @FXML private TextField titleField;
    @FXML private TextField addressField;
    @FXML private TextField townIdField;
    @FXML private TextField phoneField;

    private Connection conn;

    // Constructor receives the database connection
    public AddClinicFormController(/*Connection conn*/) {
        //this.conn = conn;
    }

    @FXML
    private void handleAddClinic() {
        String title = titleField.getText();
        String address = addressField.getText();
        int townId = Integer.parseInt(townIdField.getText());
        String phone = phoneField.getText();

/*        ClinicDAO clinicDAO = new ClinicDAO(conn);
        clinicDAO.addClinic(title, address, townId, phone);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Clinic added successfully!");
        alert.showAndWait();*/

        // Clear fields
        titleField.clear();
        addressField.clear();
        townIdField.clear();
        phoneField.clear();
    }
}
