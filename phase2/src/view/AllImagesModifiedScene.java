package view;

import controller.CentralController;
import helperClasses.FillWithThumbnails;
import controller.ViewController;
import model.ImageFile;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/* Scene responsible for managing all modified Image files */
public class AllImagesModifiedScene implements IsScene {

  private ViewController viewController;
  // The List of all ImageFiles that has been modified
  private ListView<ImageFile> listView;
  CentralController centralController;

  /**
   * Constructs this ImageModifierScene
   *
   * @param viewController the controller.ViewController object to take in
   */
  AllImagesModifiedScene(ViewController viewController) {
    this.viewController = viewController;
    centralController = viewController.getCentralController();

    listView = new ListView<>();
    //add imageFiles already in CentralController
    listView.getItems().addAll(centralController.getImageFileController().getAllImageFilesList());
    // Add thumbnails of the image to list view 1
    listView.setCellFactory(
        param -> new FillWithThumbnails());
  }

  /**
   * Creates and returns a scene associated with this object
   *
   * @return the scene associated with this object
   */
  public Scene createAndReturnScene() {
    // Button to go back to previous Scene
    final Button backButton = new Button("Back");
    backButton.setMinSize(100, 50);
    backButton.setPadding(new Insets(20, 20, 20, 20));
    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    // Button to modify a particular imageFile
    final Button modifyButton = new Button("Modify Chosen File");
    modifyButton.setMinSize(100, 50);
    modifyButton.setPadding(new Insets(20, 20, 20, 20));

    //Layout for this scene
    VBox layout = new VBox(20);
    layout.setPadding(new Insets(20, 20, 20, 20));
    layout
        .getChildren()
        .addAll(backButton, new Text("All Images Modified till Date : "), listView, modifyButton);
    Scene thisScene = new Scene(layout, 1280, 720);

    modifyButton.setOnAction(
        event -> {
          ImageFile selectedFile = listView.getSelectionModel().getSelectedItem();

          // Only respond if user has selected a file
          if (selectedFile != null) {
            viewController.switchScene(
                new ImageModifierScene(viewController, selectedFile));
          }
        });
    return thisScene;
  }

  /**
   * Refreshes this Scene and updates the list of all modified files, listView, after made
   * modification on Imagefile
   */
  @Override
  public void refreshScene() {
    listView.getItems().removeAll(listView.getItems());
    listView.getItems().addAll(centralController.getImageFileController().getAllImageFilesList());
    // Add thumbnails of the image to list view 1
    listView.setCellFactory(
        param -> new FillWithThumbnails());
  }

}
