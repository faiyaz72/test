package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/* Class for making an alert box pop up */
class AlertBox {

  // Class adapted from thenewboston's JavaFX Youtube Tutorial 5

  /**
   * Constructs an warning alert box with a title and a message
   *
   * @param title String title of this AlertBox object
   * @param message String message body of this AlertBox object
   */
  static void display(String title, String message) {
    //Setting up AlertBox Stage
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle(title);
    window.setMinWidth(300);
    window.setMinHeight(200);

    //Setting up AlertBox title and message body
    Label label = new Label();
    label.setText(message);
    Button closeButton = new Button("Close window");
    closeButton.setOnAction(e -> window.close());

    //Alert Box Layout
    VBox layout = new VBox(10);
    layout.getChildren().addAll(label, closeButton);
    layout.setAlignment(Pos.CENTER);

    Scene scene = new Scene(layout);
    window.setScene(scene);
    window.showAndWait();
  }
}
