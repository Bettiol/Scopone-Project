module Scopone_Project {
    requires org.json;

    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.media;

    opens application to javafx.graphics, javafx.controls, javafx.media;
    opens application.control to javafx.fxml;

    exports application;
    exports application.model.engine.types;
}