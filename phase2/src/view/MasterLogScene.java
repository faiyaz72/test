package view;

import controller.ImageFileController;
import controller.ViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

import java.util.Map;

/* Scene of Master Log Manager Window for all modified ImageFiles */
public class MasterLogScene implements IsScene {

  private final ViewController viewController;
  private Button backButton = new Button("Back");
  private ImageFileController imageFileController;

  /**
   * Constructs this MasterLogScene
   *
   * @param viewController the controller.viewController object to take in
   */
  MasterLogScene(ViewController viewController) {
    this.viewController = viewController;
    this.imageFileController = viewController.getCentralController().getImageFileController();
  }

  /**
   * Creates and returns a scene associated with Master Log Manager Window
   *
   * @return The scene associated the Master Log Manager Window
   */
  @Override
  public Scene createAndReturnScene() {
    // Setting text of image file path
    Text filePathText = new Text("Log of all files ever renamed");
    filePathText.setFont(new javafx.scene.text.Font(14));

    // setting up back button
    backButton.setMinSize(150, 50);
    backButton.setPadding(new Insets(20, 20, 20, 20));
    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    // From https://stackoverflow.com/questions/18618653/binding-hashmap-with-tableview-javafx
    // How to create JavaFX table using data from a Map
    TableColumn<Map.Entry<String, String>, String> column1 = new TableColumn<>("Date");
    column1.setCellValueFactory(p -> {
      // for first column we use keys of the map.
      return new SimpleStringProperty(p.getValue().getKey());
    });

    TableColumn<Map.Entry<String, String>, String> column2 = new TableColumn<>("Name on this Date");
    column2.setCellValueFactory(p -> {
      // for second column we use values of the map.
      return new SimpleStringProperty(imageFileController.getAllLog().get(p.getValue().getKey()));
    });

    // set the table, viewLog
    ObservableList<Map.Entry<String, String>> items = FXCollections
        .observableArrayList(imageFileController.getAllLog().entrySet());
    final TableView<Map.Entry<String, String>> viewLog = new TableView<>(items);
    // set column widths
    column1.prefWidthProperty().bind(viewLog.widthProperty().multiply(0.15));
    column2.prefWidthProperty().bind(viewLog.widthProperty().multiply(0.85));

    // add columns to table
    viewLog.getColumns().add(column1);
    viewLog.getColumns().add(column2);
    viewLog.getSortOrder().add(column1);

    VBox layout = new VBox(20);
    layout.setPadding(new Insets(20, 20, 20, 20));
    // adding all items into the layout
    layout.getChildren().addAll(backButton, filePathText, viewLog);

    return new Scene(layout, 1280, 720);
  }

  /**
   * Refreshes this Scene and updates all modification made
   */
  @Override
  public void refreshScene() {
  }
}
