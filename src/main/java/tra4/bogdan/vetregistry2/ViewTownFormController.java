package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewTownFormController {
    @FXML private TextField nameField;
    @FXML private TextField zipField;
    private Town town;

    // Constructor receives the town object
    public ViewTownFormController(Town town) {
        this.town = town;
    }

    public void initialize() {
        // Set the text fields with the town data
        nameField.setText(town.getName());
        zipField.setText(Integer.toString(town.getZip()));
        
        // Make the text fields non-editable
        nameField.setEditable(false);
        zipField.setEditable(false);
    }

    @FXML
    private void handleClose() {
        // Close the dialog
        ((Stage) nameField.getScene().getWindow()).close();
    }
}