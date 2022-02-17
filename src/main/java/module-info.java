module application {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    exports application;

    opens application to javafx.fxml;
    opens application.control to javafx.fxml;

    /*exports application.control;
    exports application.model.networking.TDA;
    exports application.model.networking.subrutines;
    exports application;
    exports application.model.engine.subrutines;
    exports application.model.networking;
    exports application.model.engine.TDA;
    exports application.model.engine;

    opens application.control;

    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.media;*/
}