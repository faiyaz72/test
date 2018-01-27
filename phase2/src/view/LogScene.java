package view;

import controller.CentralController;
import controller.ViewController;
import model.ImageFile;
import model.Tag;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

/* Scene for Displaying the name history of an image file, and allows user to revert to an older name. */
class LogScene implements IsScene {

  private ImageFile imageFile;
  private Button backButton = new Button("Back");
  private Button revertButton = new Button("Revert to Old Name");

  private ViewController viewController;
  private CentralController centralController;

  /**
   * Constructs this view.LogScene
   *
   * @param viewController the controller.viewController object to take in
   * @param imageFile the model.ImageFile object which was selected from previous Scene
   */
  LogScene(ViewController viewController, ImageFile imageFile) {
    this.viewController = viewController;
    this.centralController = viewController.getCentralController();
    this.imageFile = imageFile;
  }

  /**
   * Creates and returns a scene associated with Master Log Manager Window
   *
   * @return The scene associated the Master Log Manager Window
   */
  public Scene createAndReturnScene() {
    // Setting text of image file path
    Text filePathText = new Text("Chosen File : " + imageFile.getImagePath());
    filePathText.setFont(new Font(14));
    // Setting text of image name
    Text fileNameText = new Text("Log of " + imageFile.getImageName());
    fileNameText.setFont(new Font(16));

    // setting up back button
    backButton.setMinSize(150, 50);
    backButton.setPadding(new Insets(20, 20, 20, 20));
    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    // From https://stackoverflow.com/questions/18618653/binding-hashmap-with-tableview-javafx
    // How to create JavaFX table using data from a Map

    // use fully detailed type for Map.Entry<String, List<Tag>>
    TableColumn<Map.Entry<String, List<Tag>>, String> column1 = new TableColumn<>("Date");
    column1.setCellValueFactory(p -> {
      // for first column we use keys of the map.
      return new SimpleStringProperty(p.getValue().getKey());
    });

    TableColumn<Map.Entry<String, List<Tag>>, String> column2 = new TableColumn<>(
        "Name on this Date");
    column2.setCellValueFactory(p -> {
      // for second column we use values of the map.
      return new SimpleStringProperty(imageFile.getOriginalNameWithTags(p.getValue().getValue()));
    });

    // set the table, viewLog
    ObservableList<Map.Entry<String, List<Tag>>> items = FXCollections
        .observableArrayList(imageFile.getTagHistory().entrySet());
    final TableView<Map.Entry<String, List<Tag>>> viewLog = new TableView<>(items);

    // set column widths
    column1.prefWidthProperty().bind(viewLog.widthProperty().multiply(0.15));
    column2.prefWidthProperty().bind(viewLog.widthProperty().multiply(0.85));

    // add columns to table
    viewLog.getColumns().add(column1);
    viewLog.getColumns().add(column2);
    viewLog.getSortOrder().add(column1);

    // setting up button to revert back to an older name
    revertButton.setOnAction(
        event -> {
          List<Tag> selectedTags = viewLog.getSelectionModel().getSelectedItem().getValue();
          // Respond only if there is a selection
          if (selectedTags != null) {
            centralController.revertImageFileToTags(imageFile, selectedTags);
            // return to the previous scene after reverting name
            viewController.goBackToPreviousScene();
          }
        });
    revertButton.setMinSize(100, 50);

    // setting up the vbox
    VBox layout = new VBox(20);
    layout.setPadding(new Insets(20, 20, 20, 20));
    // adding all items into the layout
    layout.getChildren().addAll(backButton, filePathText, fileNameText, viewLog,
        revertButton);
    return new Scene(layout, 1280, 720);
  }

  /**
   * Refreshes this Scene and updates all modification made
   */
  @Override
  public void refreshScene() {
  }
}
