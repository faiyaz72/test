package view;

import controller.ViewController;
import model.ImageFile;
import model.Tag;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

/* Scene where new Tags can be created */
class CreateNewTagScene extends TagScene {

  // A checker if this Scene is for MasterTagWindow or TagWindowForImage
  private final boolean forImage;

  /**
   * @param imageFile ImageFile to be modified
   * @param existingTagsList Existing list of tags to be inputted.
   * @param isImage Checker if Scene is for MasterTagWindow or TagWindowForImage
   */
  CreateNewTagScene(ViewController viewController, ImageFile imageFile,
      ListView<Tag> existingTagsList,
      boolean isImage) {
    super(viewController, imageFile, existingTagsList);
    this.forImage = isImage;
  }

  /**
   * Creator of CreateNewTagScene
   *
   * @return new Scene with the desired specifications
   */
  public Scene createAndReturnScene() {

    // Buttons that need to placed in the Scene
    final Button backButton = new Button("Back");
    backButton.setMinSize(100, 50);
    backButton.setPadding(new Insets(20, 20, 20, 20));

    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    final Button makeNewTag = new Button("Create and add Tag");
    makeNewTag.setMinSize(100, 50);
    makeNewTag.setPadding(new Insets(20, 20, 20, 20));

    TextField tagName = new TextField(); //TextField where user will enter new Tag Name.

    // Layout for this Scene
    VBox NewTagLayout = new VBox(20);
    NewTagLayout.setPadding(new Insets(20, 20, 20, 20));
    NewTagLayout.getChildren().add(backButton);
    NewTagLayout.getChildren().add(tagName);
    NewTagLayout.getChildren().add(makeNewTag);
    if (forImage) { //Checks if it's in TagWindowForImage, creates Tag and adds to particular image
      tagName.setOnAction(f -> assignTag(tagName));
    } else { //Only creates Tag but doesn't associate with any Image, in MasterTagWindow
      tagName.setOnAction(g -> createBlankTag(tagName));
    }
    // Alternative ways to do the above job by clicking makeNewTag Button
    if (forImage) {
      makeNewTag.setOnAction(e -> assignTag(tagName));
    } else {
      makeNewTag.setOnAction(l -> createBlankTag(tagName));
    }
    return new Scene(NewTagLayout, 1280, 720);

  }

  /**
   * Helper Function to create and assign new Tag to a particular image
   *
   * @param tagName Name of the Tag to be created
   */
  private void assignTag(TextField tagName) {
    if (centralController.getTagController()
        .checkTag(tagName.getText())) { //Checks if Tag already exists
      AlertBox.display("Create Tag", "Tag already exists!");
      viewController.goBackToPreviousScene();
    } else {
      try {
        centralController.createTagWithImageFile(tagName.getText(), imageFile);
      } catch (IOException e1) {
        e1.printStackTrace();
      }

      viewController.goBackToPreviousScene();
    }
  }

  /**
   * Helper Function to create a Tag but no association to Image
   *
   * @param tagName Name of the Tag to be Created
   */
  private void createBlankTag(TextField tagName) {
    centralController.getTagController().createTag(tagName.getText());

    viewController.goBackToPreviousScene();
  }

  @Override
  public void refreshScene() {
    if (forImage) {
      existingTagsList.getItems().removeAll(existingTagsList.getItems());
      existingTagsList.getItems().addAll(imageFile.getCurrentTags());
    } else {
      existingTagsList.getItems().removeAll(existingTagsList.getItems());
      existingTagsList.getItems().addAll(centralController.getTagController().getExistingTags());
    }
  }
}
