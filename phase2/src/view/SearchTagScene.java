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

/* NEW FEATURE: Scene of Search Existing Tag */
class SearchTagScene implements IsScene {

  private CentralController centralController;
  private ViewController viewController;
  private Button backButton = new Button("Back");
  private Button searchTag = new Button("Search in Tags");
  private Button showFiles = new Button("Display all Files containing chosen Tag");
  private ListView<Tag> searchedTagList = new ListView<>();

  /**
   * Constructs this view.SearchTagScene
   *
   * @param viewController the controller.viewController object to take in
   */
  SearchTagScene(ViewController viewController) {
    this.viewController = viewController;
    this.centralController = viewController.getCentralController();
  }

  /**
   * Creates and returns a scene associated with the Search All Tags
   *
   * @return The scene associated the Search All Tags
   */
  public Scene createAndReturnScene() {
    //Display Files contains chosen Tag Button
    showFiles.setMinSize(100, 40);
    //Back to Search Window Button
    backButton.setMinSize(100, 40);

    //Top Layout
    HBox searchLayout = new HBox(20);
    searchLayout.setPadding(new Insets(20, 20, 20, 20));
    TextField searchTagName = new TextField(); //TextField for user to enter searching information
    searchTagName.setPromptText("Enter search text");
    searchLayout.getChildren().add(searchTagName);
    searchLayout.getChildren().add(searchTag); //Search Button

    // Middle Layout
    VBox midLayout = new VBox(20);
    midLayout.setPadding(new Insets(20, 20, 20, 20));
    midLayout.getChildren().add(searchedTagList);

    // Bottom menu bar layout
    HBox bottomLayout = new HBox(20);
    bottomLayout.setPadding(new Insets(20, 20, 20, 20));
    bottomLayout.getChildren().add(backButton);
    bottomLayout.getChildren().add(showFiles);

    // Combined Layout for scene
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(searchLayout);
    borderPane.setCenter(midLayout);
    borderPane.setBottom(bottomLayout);

    backButton.setOnAction(event -> viewController.goBackToPreviousScene());

    TagSceneFactory tagSceneFactory = new TagSceneFactory();

    // Actions From Display Files Button
    showFiles.setOnAction(e -> {
      Tag chosenTag = searchedTagList.getSelectionModel().getSelectedItem();
      if (chosenTag != null) {
        viewController
            .switchScene(tagSceneFactory.getScene("ShowFilesWithSelectedTag", viewController,
                null, searchedTagList));
      } else {
        AlertBox.display("Select Tag", "Select a Tag first!");
      }
    });

    //Actions From Search Button
    searchTag.setOnAction(e -> {
      searchedTagList.getItems().removeAll(searchedTagList.getItems());
      searchedTagList.getItems()
          .addAll(centralController.getTagController().searchTag(searchTagName.getText()));
    });
    return new Scene(borderPane, 1280, 720);
  }

  /**
   * Refreshes this Scene and updates all modification made
   */
  @Override
  public void refreshScene() {
  }
}
