package view;

import helperClasses.FillWithThumbnails;
import controller.ViewController;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.ImageFile;
import model.Tag;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

/* Scene responsible for displaying all Image files associated with selected tag */
public class ShowFilesWithSelectedTagScene extends TagScene {

  // Tag object to check
  private Tag chosenTag;
  // List of all ImageFiles with chosenTag
  private ListView<ImageFile> fileList = new ListView<>();

  /**
   * Constructs this view.ShowFilesWithSelectedTagScene
   *
   * @param viewController the controller.viewController object to take in
   * @param imageFile the model.ImageFile object to take in.
   * @param existingTagsList the List of existing Tags
   */
  ShowFilesWithSelectedTagScene(ViewController viewController, ImageFile imageFile,
      ListView<Tag> existingTagsList) {
    super(viewController, imageFile, existingTagsList);
  }

  /**
   * Creates the Scene which allows User to View or Modify all ImageFiles associated with
   * chosenTag.
   *
   * @return desired Scene for Displaying All ImageFiles associated with Tag object chosenTag
   */
  public Scene createAndReturnScene() {
    // Button to go back to previous Scene
    Button backButton = new Button("Back");
    backButton.setMinSize(100, 60);
    backButton.setPadding(new Insets(20, 20, 20, 20));

    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    // Button to remove chosenTag from selected ImageFile
    Button deleteFile = new Button("Delete Chosen File from this Tag");
    deleteFile.setMinSize(100, 60);
    deleteFile.setPadding(new Insets(20, 20, 20, 20));

    // Button to modify selected ImageFile
    Button modifyFileButton = new Button("Modify Chosen File");
    modifyFileButton.setMinSize(100, 60);
    modifyFileButton.setPadding(new Insets(20, 20, 20, 20));

    chosenTag = existingTagsList.getSelectionModel().getSelectedItem();

    // add the files tp the list
    fileList.getItems().addAll(centralController.getTagController().showFilesWithTag(chosenTag));
    // Add thumbnails of the image to list view 1
    fileList.setCellFactory(param -> new FillWithThumbnails());

    //Layout for this scene
    VBox topLayout = new VBox(20);
    topLayout.setPadding(new Insets(20, 20, 20, 20));
    topLayout.getChildren().add(backButton);

    VBox midLayout = new VBox(20);
    midLayout.setPadding(new Insets(20, 20, 20, 20));
    midLayout.getChildren().addAll(new Text("All Files Contains : " + chosenTag.getTagName()),
        fileList);

    HBox bottomLayout = new HBox(20);
    bottomLayout.setPadding(new Insets(20, 20, 20, 20));
    bottomLayout.getChildren().addAll(modifyFileButton, deleteFile);

    // Combined top, middle and bottom layout
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(topLayout);
    borderPane.setCenter(midLayout);
    borderPane.setBottom(bottomLayout);

    // Delete Chosen File from this Button, which deletes the tag selected from the chosen file
    deleteFile.setOnAction(e -> {
      ImageFile selectedFile = fileList.getSelectionModel().getSelectedItem();
      if (selectedFile != null) {
        try {
          centralController.dissociateTag(chosenTag, selectedFile);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
      viewController.goBackToPreviousScene();
    });

    // change scene from which the file can be modified
    modifyFileButton.setOnAction(
        event -> {
          ImageFile selectedFile = fileList.getSelectionModel().getSelectedItem();
          // Only respond if user has selected a file
          if (selectedFile != null) {
            viewController.switchScene(
                new ImageModifierScene(viewController, selectedFile));
          }
        });

    return new Scene(borderPane, 1280, 720);
  }

  /**
   * Refreshes this Scene and updates the list of all ImageFiles associated with chosenTag after
   * modification made
   */
  @Override
  public void refreshScene() {
    fileList.getItems().removeAll(fileList.getItems());
    fileList.getItems().addAll(centralController.getTagController().showFilesWithTag(chosenTag));
  }
}
