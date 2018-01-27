package view;

import controller.ViewController;
import model.ImageFile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/* Scene of Manager All Modified Image Window */
public class ImageModifierScene implements IsScene {

  private ViewController viewController;
  private ImageFile imageFile;
  private Text filePathText;
  private Text fileNameText;

  /**
   * Constructs this view.ImageModifierScene
   *
   * @param imageFile the model.ImageFile to be modified via this scene
   */
  ImageModifierScene(ViewController viewController, ImageFile imageFile) {
    this.viewController = viewController;
    this.imageFile = imageFile;
  }

  /**
   * Creates and returns a scene associated with this object
   *
   * @return the scene associated with this object
   */
  public Scene createAndReturnScene() {
    // Button to select ImageFile to edit
    filePathText = new Text("Chosen File : " + imageFile.getImagePath());
    filePathText.setFont(new Font(14));

    // Information Message on Top of this Scene
    fileNameText = new Text("Image Name : " + imageFile.getImageName());
    fileNameText.setFont(new Font(15));
    fileNameText.setUnderline(true);

    // Button to go back
    final Button backButton = new Button("Back");
    backButton.setMinSize(150, 50);
    backButton.setPadding(new Insets(20, 20, 20, 20));

    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    //Manage model.Tag button
    final Button tagButton = new Button("Manage Tags");
    tagButton.setMinSize(150, 50);
    tagButton.setPadding(new Insets(20, 20, 20, 20));

    // Display the manageTag window
    tagButton.setOnAction(
        event -> viewController.switchScene(new TagWindowForImage(viewController, imageFile)));

    //Move button
    final Button moveButton = new Button("Move File");
    moveButton.setMinSize(150, 50);
    moveButton.setPadding(new Insets(20, 20, 20, 20));

    moveButton.setOnAction(event -> {
          // prompt the user to choose a directory
          final DirectoryChooser directoryChooser = new DirectoryChooser();
          // get the directory chosen
          final File selectedDirectory = directoryChooser.showDialog(viewController.getWindow());

          if (selectedDirectory != null) {
            try {
              imageFile.moveImage(selectedDirectory);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
    );

    // openImageFolder Button
    final Button openImageFolder = new Button("Open Image Folder");
    openImageFolder.setMinSize(150, 50);
    openImageFolder.setPadding(new Insets(20, 20, 20, 20));

    openImageFolder.setId("openImageFolderButton");

    openImageFolder.setOnAction(event -> imageFile.openImageDirectory());

    // viewLogButton
    final Button viewLogButton = new Button("view Log");
    viewLogButton.setMinSize(150, 50);
    viewLogButton.setPadding(new Insets(20, 20, 20, 20));

    viewLogButton.setId("viewLogButton");

    viewLogButton.setOnAction(event -> {
      // transition to the log window
      viewController.switchScene(new LogScene(viewController, imageFile));
    });

    // Layout for the scene
    VBox layout = new VBox(20);
    layout.setPadding(new Insets(20, 20, 20, 20));
    layout.getChildren().add(backButton);
    layout.getChildren().add(filePathText);

    // Adding image preview
    try {
      layout.getChildren().addAll(imageFile.getImage(200, 200), fileNameText);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    layout.getChildren().addAll(tagButton, moveButton, openImageFolder, viewLogButton);

    layout.setAlignment(Pos.CENTER);

    return new Scene(layout, 1280, 720);
  }

  /**
   * Refreshes this Scene and updates all modifications made
   */
  @Override
  public void refreshScene() {
    // Refresh the name to reflect any changes
    fileNameText.setText("Image Name : " + imageFile.getImageName());
    // Refresh the path to reflect any changes
    filePathText.setText("Chosen File : " + imageFile.getImagePath());
  }
}
