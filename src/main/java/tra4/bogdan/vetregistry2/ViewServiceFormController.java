package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewServiceFormController {
    @FXML private TextField serviceField;
    @FXML private TextField costField;
    private Service service;

    // Constructor receives the service object
    public ViewServiceFormController(Service service) {
        this.service = service;
    }

    public void initialize() {
        // Set the text fields with the service data
        serviceField.setText(service.getName());
        costField.setText(Integer.toString(service.getCost()));
        
        // Make the text fields non-editable
        serviceField.setEditable(false);
        costField.setEditable(false);
    }

    @FXML
    private void handleClose() {
        // Close the dialog
        ((Stage) serviceField.getScene().getWindow()).close();
    }
}