module tra4.bogdan.vetregistry2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens tra4.bogdan.vetregistry2 to javafx.fxml;
    exports tra4.bogdan.vetregistry2;
}