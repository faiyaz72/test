package view;

import controller.CentralController;
import controller.ViewController;
import model.Tag;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/* Scene of Manager All Tag Window */
class MasterTagWindow implements IsScene {

  private ViewController viewController;
  private CentralController centralController;

  private Button parentBackButton = new Button("Back");
  private Button backButton = new Button("Back");
  private Button createTag = new Button("Create Tag");
  private Button renameTag = new Button("Rename Tag");
  private Button deleteTag = new Button("Delete Tag");
  private Button showFiles = new Button("Display all Files containing chosen Tag");
  private ListView<Tag> allTagList = new ListView<>();

  /**
   * Constructs this MasterTagWindow
   *
   * @param viewController the controller.ViewController object to take in
   */
  MasterTagWindow(ViewController viewController) {
    this.viewController = viewController;
    this.centralController = viewController.getCentralController();
  }

  /**
   * Creates and returns the scene allows user to manage all Tag Objects
   *
   * @return the scene associated the Manage All Tag Window
   */
  public Scene createAndReturnScene() {
    // The Scene of Manage All Tag Window
    Scene masterTagScene;

    // Buttons on Top of the Manage All Tag Window
    createTag.setMinSize(100, 40);
    renameTag.setMinSize(100, 40);
    deleteTag.setMinSize(100, 40);
    showFiles.setMinSize(100, 40);

    // NEW FEATURE in phase2: Search Bar, also on Top of Manage All Tag Window
    TextField searchTagName = new TextField(); //TextField for user to enter searching information
    searchTagName.setPromptText("Enter search text");
    Button searchTag = new Button("Search in Tags");

    // General Back Button to come back to Manage All Tag Window
    backButton.setMinSize(100, 40);
    //Parent Back Button to Scene prior to Manage All Tag Window
    parentBackButton.setMinSize(100, 40);

    // Top menu bar layout: Function Buttons
    HBox topLayout = new HBox(20);
    topLayout.setPadding(new Insets(20, 20, 20, 20));
    topLayout.getChildren()
        .addAll(createTag, renameTag, deleteTag, showFiles, searchTagName, searchTag);

    // Middle Layout: list of all tags in database
    VBox midLayout = new VBox(20);
    midLayout.setPadding(new Insets(20, 20, 20, 20));
    Text TagListText = new Text("All Tags in Database : ");
    TagListText.setFont(new Font(14));
    midLayout.getChildren().add(TagListText);
    allTagList.getItems().removeAll(allTagList.getItems());
    allTagList.getItems().addAll(centralController.getTagController().getExistingTags());
    midLayout.getChildren().add(allTagList);
    midLayout.getChildren().add(parentBackButton);

    // Combined top and middle layout
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(topLayout);
    borderPane.setCenter(midLayout);

    // Parent Back button, which goes to parentScene
    parentBackButton.setOnAction(event -> viewController.goBackToPreviousScene());

    // Create Button, which goes to the window of sceneCreateTag
    TagSceneFactory tagSceneFactory = new TagSceneFactory();

    createTag.setOnAction(e -> viewController.switchScene(tagSceneFactory.getScene(
        "CreateNewTagMasterScene", viewController, null, allTagList)));

    // Rename Button, which goes to the window of sceneRenameTag
    renameTag.setOnAction(e -> {
      Tag chosenTag = allTagList.getSelectionModel().getSelectedItem();
      if (chosenTag != null) {
        viewController.switchScene(tagSceneFactory.getScene("RenameTagSceneMaster", viewController,
            null, allTagList));

      } else {
        AlertBox.display("Select Tag", "Select a Tag first!");
      }
    });

    //Delete Button, which goes to the window of deleteTagScene
    deleteTag.setOnAction(e -> viewController.switchScene(tagSceneFactory.getScene(
        "DeleteTagSceneMaster", viewController, null, allTagList)));

    //Display All File contains chosen Button, which goes to the window of sceneShowFile
    showFiles.setOnAction(e -> {
      Tag chosenTag = allTagList.getSelectionModel().getSelectedItem();
      if (chosenTag != null) {
        viewController.switchScene(tagSceneFactory.getScene("ShowFilesWithSelectedTag",
            viewController, null, allTagList));
      } else {
        AlertBox.display("Select Tag", "Select a Tag first!");
      }

    });

    // Set up search Button
    searchTag.setOnAction(e -> {
      allTagList.getItems().removeAll(allTagList.getItems());
      allTagList.getItems()
          .addAll(centralController.getTagController().searchTag(searchTagName.getText()));
    });

    masterTagScene = new Scene(borderPane, 1280, 720);
    return masterTagScene;
  }

  /**
   * Refreshes this Scene and updates all modification made
   */
  @Override
  public void refreshScene() {

  }

}
