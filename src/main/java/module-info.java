module vincent.firetest {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    requires firebase.admin;
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires google.cloud.firestore;
    requires google.cloud.core;
    requires com.google.api.apicommon;
    requires java.net.http;
    requires org.json;

    opens vincent.firetest to javafx.fxml;
    exports vincent.firetest;
    exports vincent.firetest.Controllers;
    opens vincent.firetest.Controllers to javafx.fxml;
}