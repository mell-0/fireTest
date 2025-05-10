package vincent.firetest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

import com.google.firebase.auth.*;
import com.google.cloud.firestore.*;
import com.google.api.core.ApiFuture;

// testBranch test comment
public class HelloApplication extends Application {
    public static Scene scene;

    public static Firestore fstore;
    public static FirebaseAuth fauth;
    private final FirestoreContext contxtFirebase = new FirestoreContext();

    @Override
    public void start(Stage stage) throws IOException {
        fstore = contxtFirebase.firebase();
        fauth = FirebaseAuth.getInstance();

        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //scene = new Scene(loadFXML("hello-view"), 640, 480);
        scene = new Scene(loadFXML("hello-view")); // adding the width & hight here overrides the size on the fxml

        stage.setTitle("Hello!");

        stage.setScene(scene);
        stage.show();
    }

    // need public static Scene, loadFXML
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));

        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene(); // This resizes the window to fit the new root’s preferred size
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}