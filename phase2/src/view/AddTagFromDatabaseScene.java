package view;

import controller.ViewController;
import model.ImageFile;
import model.Tag;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

/* Scene Responsible for adding a Tag that is already in the database */
class AddTagFromDatabaseScene extends TagScene {

  // New list showing all the available Tags in database
  private ListView<Tag> addTagList = new ListView<>();

  /**
   * Constructs the Scene to add from existing tags with view controller, selected image file, and
   * the list of all existing tags.
   *
   * @param viewController the controller.ViewController object to take in
   * @param imageFile the model.ImageFile object which was selected from previous Scene
   * @param existingTagsList the List of existing Tags
   */
  AddTagFromDatabaseScene(ViewController viewController, ImageFile imageFile,
      ListView<Tag> existingTagsList) {
    super(viewController, imageFile, existingTagsList);
  }

  /**
   * Creates the Scene which allows User to Add existing Tags
   *
   * @return desired Scene for Add existing Tags
   */
  public Scene createAndReturnScene() {

    // Creating and setting Back Button to navigate between Scenes
    final Button backButton = new Button(
        "Back");
    backButton.setMinSize(100, 60);
    backButton.setPadding(new Insets(20, 20, 20, 20));

    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    // Creating and setting Add Tag to Image Button
    final Button AddTagToImage = new Button(
        "Add Tag(s) to Image");
    AddTagToImage.setMinSize(100, 60);
    AddTagToImage.setPadding(new Insets(20, 20, 20, 20));

    // Message for Select Multiple Tags to be added
    Text multipleSelectMessage = new Text("To select multiple Tags, hold Command/Shift key");
    multipleSelectMessage.setFont(new Font(15));

    //Layout for this Scene
    VBox AddTagLayout = new VBox(20);
    AddTagLayout.setPadding(new Insets(20, 20, 20, 20));
    AddTagLayout.getChildren().add(backButton);
    addTagList.getItems().removeAll(addTagList.getItems());
    addTagList.getItems().addAll(centralController.getTagController().getExistingTags());
    addTagList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    AddTagLayout.getChildren().add(multipleSelectMessage);
    AddTagLayout.getChildren().add(addTagList);
    AddTagLayout.getChildren().add(AddTagToImage);
    AddTagLayout.setAlignment(Pos.CENTER);

    //Button when clicked adds all selected Tags to the image
    AddTagToImage.setOnAction(
        event -> {
          try {
            List<Tag> tagToAdd = addTagList.getSelectionModel().getSelectedItems();
            for (Tag aTagToAdd : tagToAdd) {
              centralController.addTag(aTagToAdd, imageFile);
            }
          } catch (IOException e1) {
            e1.printStackTrace();
          }

          viewController.goBackToPreviousScene();
        });
    return new Scene(AddTagLayout, 1280, 720);
  }

  /**
   * Refreshes this Scene and updates the list of Tags for this ImageFile after adding new tags
   */
  @Override
  public void refreshScene() {
    existingTagsList.getItems().removeAll(existingTagsList.getItems());
    existingTagsList.getItems().addAll(imageFile.getCurrentTags());
  }
}
