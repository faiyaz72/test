package view;

import controller.ViewController;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.ImageFile;
import model.Tag;

/* Scene responsible for Rename tag */
public class RenameTagScene extends TagScene {

  // Tag object to rename
  private Tag chosenTag;

  /**
   * Constructs this view.RenameTagScene
   *
   * @param viewController the controller.viewController object to take in
   * @param imageFile the model.ImageFile object to take in.
   * @param existingTagsList the List of existing Tags
   */
  RenameTagScene(ViewController viewController, ImageFile imageFile,
      ListView<Tag> existingTagsList) {
    super(viewController, imageFile, existingTagsList);
  }

  /**
   * Creates the Scene which allows User to rename chosenTag.
   *
   * @return desired Scene for Rename Tag object chosenTag
   */
  public Scene createAndReturnScene() {
    // Button to go back to previous Scene
    final Button backButton = new Button("Back");
    backButton.setMinSize(100, 60);
    backButton.setPadding(new Insets(20, 20, 20, 20));

    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    // Button to rename chosenTag
    final Button renameTagButton = new Button("Rename to this New Tag");
    renameTagButton.setMinSize(100, 60);
    renameTagButton.setPadding(new Insets(20, 20, 20, 20));

    TextField newTagName = new TextField();

    chosenTag = existingTagsList.getSelectionModel().getSelectedItem();

    //layout for sceneRenameTag
    VBox layoutRenameTag = new VBox(20);
    layoutRenameTag.setPadding(new Insets(20, 20, 20, 20));
    layoutRenameTag.getChildren().add(backButton);
    layoutRenameTag.getChildren().add(new Text("Current Tag Name : " + chosenTag.getTagName()));
    layoutRenameTag.getChildren().add(new Text("Enter New Tag Name : "));
    layoutRenameTag.getChildren().add(newTagName);
    layoutRenameTag.getChildren().add(renameTagButton);
    layoutRenameTag.getChildren()
        .add(new Text("Note : Renaming the tag will rename all image files containing the tag"));
    newTagName.setOnAction(g -> helperRename(newTagName));

    renameTagButton.setOnAction(f -> helperRename(newTagName));

    return new Scene(layoutRenameTag, 1280, 720);
  }

  /**
   * Helper function to rename existing Tags
   *
   * @param newTagName new name of the tag
   */
  private void helperRename(TextField newTagName) {
    if (!newTagName.getText().isEmpty()) {
      List<ImageFile> imageFileWithTagList = centralController.getTagController()
          .showFilesWithTag(chosenTag);
      centralController.getTagController().renameTag(chosenTag, newTagName.getText());

      for (ImageFile imageFile : imageFileWithTagList) {

        // Looks up all ImageFiles and updates their names
        imageFile.renameImage();
      }
      viewController.goBackToPreviousScene();
    }
  }

  /**
   * Refreshes this Scene and updates the list of all tags after modification made
   */
  @Override
  public void refreshScene() {
    existingTagsList.getItems().removeAll(existingTagsList.getItems());
    existingTagsList.getItems().addAll(centralController.getTagController().getExistingTags());
  }
}