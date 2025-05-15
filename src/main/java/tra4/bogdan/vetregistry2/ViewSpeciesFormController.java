package tra4.bogdan.vetregistry2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewSpeciesFormController {
    @FXML private TextField nameField;
    private Species species;

    // Constructor receives the species object
    public ViewSpeciesFormController(Species species) {
        this.species = species;
    }

    public void initialize() {
        // Set the text field with the species name
        nameField.setText(species.getName());
        
        // Make the text field non-editable
        nameField.setEditable(false);
    }

    @FXML
    private void handleClose() {
        // Close the dialog
        ((Stage) nameField.getScene().getWindow()).close();
    }
}