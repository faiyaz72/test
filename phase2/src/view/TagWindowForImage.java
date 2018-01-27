package view;

import controller.ViewController;
import model.ImageFile;
import model.Tag;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;

/* Scene responsible for Manage Tags of a single ImageFile */
class TagWindowForImage implements IsScene {

  private ViewController viewController;
  private ImageFile imageFile;

  /**
   * Constructs this view.TagWindowForImage
   *
   * @param viewController the controller.viewController object to take in
   * @param imageFile the model.ImageFile object which was selected from previous Scene
   */
  TagWindowForImage(ViewController viewController, ImageFile imageFile) {
    this.viewController = viewController;
    this.imageFile = imageFile;
  }

  /**
   * Creates and returns the scene allows user to manage tags of this ImageFile
   *
   * @return the scene associated the Tag Manage for Image Window
   */
  @Override
  public Scene createAndReturnScene() {
    Button addTagFromDatabaseButton = new Button("Add from Tag Database");
    Button createNewTagButton = new Button("Create a new Tag");
    Button deleteTagButton = new Button("Delete a Tag From this Image");
    Button backButton = new Button("Close Tag Manager");
    Button backToHomeButton = new Button("Return to Home Scene");
    ListView<Tag> existingTagsList = new ListView<>();

    // Setting button size
    addTagFromDatabaseButton.setMinSize(160, 40);
    createNewTagButton.setMinSize(160, 40);
    deleteTagButton.setMinSize(160, 40);
    backButton.setMinSize(160, 40);
    backToHomeButton.setMinSize(160, 40);

    backButton.setOnAction(e -> viewController.goBackToPreviousScene());
    backToHomeButton.setOnAction(event -> viewController.goToMainScene());

    // Top Layout for parentScene
    HBox topButtons = new HBox(20);
    topButtons.setPadding(new Insets(20, 20, 20, 20));
    topButtons.getChildren().addAll(addTagFromDatabaseButton, createNewTagButton, deleteTagButton);

    // Center Layout for parentScene
    VBox centerLayout = new VBox(20);

    // add image thumbnail
    try {
      centerLayout.getChildren().add(imageFile.getImage(250, 250));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // show the existing tags for this image in a list
    existingTagsList.getItems().removeAll(existingTagsList.getItems());
    existingTagsList.getItems().addAll(imageFile.getCurrentTags());

    Text existingTagsText = new Text("Existing Tags for this Image");
    existingTagsText.setUnderline(true);
    existingTagsText.setFont(Font.font(14));

    centerLayout.getChildren().add(existingTagsText);
    centerLayout.getChildren().addAll(existingTagsList, backButton, backToHomeButton);
    centerLayout.setAlignment(Pos.CENTER);

    // create and change the scene based on which button was pressed
    TagSceneFactory tagSceneFactory = new TagSceneFactory();

    addTagFromDatabaseButton.setOnAction(event -> viewController.switchScene(
        tagSceneFactory.getScene("AddTagFromDatabaseScene", viewController,
            imageFile, existingTagsList)));

    createNewTagButton.setOnAction(event -> viewController.switchScene(
        tagSceneFactory.getScene("CreateNewTagScene", viewController,
            imageFile, existingTagsList)));

    deleteTagButton.setOnAction(event -> viewController.switchScene(
        tagSceneFactory.getScene("DeleteTagSceneForImage", viewController,
            imageFile, existingTagsList)));

    BorderPane borderPane = new BorderPane();
    borderPane.setTop(topButtons);
    borderPane.setCenter(centerLayout);
    borderPane.setPadding(new Insets(0, 0, 10, 0));

    return new Scene(borderPane, 1280, 720);
  }

  /**
   * Refreshes this Scene and updates all modification made
   */
  @Override
  public void refreshScene() {
  }
}
