module Scopone_Project_Eclipse {
    exports application.control;
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
    requires transitive javafx.media;
}