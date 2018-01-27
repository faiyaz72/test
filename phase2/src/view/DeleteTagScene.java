package view;

import controller.ViewController;
import model.ImageFile;
import model.Tag;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

/* Scene which allows Users to either Delete a particular Tag from the Image Only or Completely
 * Delete a Tag from the Tag Database */
public class DeleteTagScene extends TagScene {

  //Checker whether Scene is being used to affect one Image or the whole database of Tags
  private final boolean forImage;

  //List which shows all Tags available to be deleted
  private ListView<Tag> deleteTagList = new ListView<>();

  /**
   * Constructs this view.DeleteTagScene
   *
   * @param viewController the controller.ViewController object to take in
   * @param imageFile the model.ImageFile object which was selected from previous Scene
   * @param existingTagsList the List of existing Tags
   * @param isImage check if deleting a blank tag or deleting tags from ImageFiles
   */
  DeleteTagScene(ViewController viewController, ImageFile imageFile, ListView<Tag> existingTagsList,
      boolean isImage) {
    super(viewController, imageFile, existingTagsList);
    this.forImage = isImage;
  }

  /**
   * Creates the Scene which allows User to Add existing Tags
   *
   * @return desired Scene for Deleting Tags
   */
  public Scene createAndReturnScene() {
    // Button to go back to previous Scene
    final Button backButton = new Button(
        "Back"); // Creating and setting Back Button to navigate between Scenes
    backButton.setMinSize(100, 60);
    backButton.setPadding(new Insets(20, 20, 20, 20));

    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    // Button to actually delete the selected tags
    final Button confirmDelete = new Button("Delete Tag(s)");
    confirmDelete.setMinSize(100, 60);
    confirmDelete.setPadding(new Insets(20, 20, 20, 20));

    // Instruction Message for delete Tags
    Text multipleSelectMessage = new Text("To select multiple Tags, hold Command/Shift key");
    multipleSelectMessage.setFont(new Font(15));

    // layout for DeleteTagScene
    VBox deleteTagSceneLayout = new VBox(20);
    deleteTagSceneLayout.setPadding(new Insets(20, 20, 20, 20));
    deleteTagSceneLayout.getChildren().add(backButton);
    deleteTagList.getItems().removeAll(deleteTagList.getItems());
    // Different methods are required if Tag needs to be deleted from Image only or from Database
    // altogether.
    if (forImage) {
      deleteTagList.getItems().addAll(imageFile.getCurrentTags());
    } else {
      deleteTagList.getItems().addAll(centralController.getTagController().getExistingTags());
    }
    deleteTagList.getSelectionModel()
        .setSelectionMode(SelectionMode.MULTIPLE); //Multiple selection of Tags
    deleteTagSceneLayout.getChildren().addAll(multipleSelectMessage,
        new Text("Note: Deleting the Tag from here will delete the tag from all the files " +
            "which contains the tag"), deleteTagList, confirmDelete);

    //Button which deletes Tag from Image/Database
    confirmDelete.setOnAction(e -> {
      try {
        List<Tag> toDelete = deleteTagList.getSelectionModel().getSelectedItems();
        for (Tag aToDelete : toDelete) {
          if (forImage) {
            centralController.dissociateTag(aToDelete, imageFile);
          } else {
            centralController.deleteExistingTag(aToDelete);
          }
        }
      } catch (IOException e1) {
        e1.printStackTrace();
      }
      viewController.goBackToPreviousScene();
    });
    return new Scene(deleteTagSceneLayout, 1280, 720);
  }

  /**
   * Refreshes this Scene and updates the list of all tags after modification made
   */
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
