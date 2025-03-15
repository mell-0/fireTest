package vincent.firetest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class SecondViewController
{
    @FXML
    public Button backButton;

    public void backbuttonClicked(ActionEvent actionEvent) throws IOException {
        System.out.println("back button clicked");
        HelloApplication.setRoot("hello-view");
    }
}
