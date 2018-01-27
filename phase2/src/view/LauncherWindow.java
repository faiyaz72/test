package view;

import controller.CentralController;
import helperClasses.FillWithThumbnails;
import controller.ViewController;
import model.ImageFile;
import helperClasses.ListImages;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

/*  First and Home Scene of Tagger Application */
public class LauncherWindow implements IsScene {

  private final ViewController viewController;
  private ListView<ImageFile> imagesInDirectoryList = new ListView<>();
  private ListView<ImageFile> allImagesUnderDirectoryList = new ListView<>();
  private File selectedDirectory;
  private Text directoryText = new Text("Chosen Directory : None");

  private CentralController centralController;

  /**
   * Constructs this LauncherWindow
   *
   * @param viewController the controller.ViewController object to take in
   */
  public LauncherWindow(ViewController viewController) {
    this.viewController = viewController;
    this.centralController = viewController.getCentralController();
  }

  /**
   * Creates and returns the home scene of Tagger Application
   *
   * @return The Scene which is the first and main scene of this application
   */
  @Override
  public Scene createAndReturnScene() {
    Scene mainScene;
    // Setting up browse button
    final Button browseButton = new Button("Choose Directory");
    browseButton.setMinSize(100, 40);
    browseButton.setOnAction(
        e -> {
          // opens a window from which user can choose a directory
          final DirectoryChooser directoryChooser = new DirectoryChooser();
          // gets and stores the directory chosen by user
          selectedDirectory = directoryChooser.showDialog(viewController.getWindow());
          // updates list1 and list2 if the user selects a directory
          updateLists();
        });

    // Setting up tag manager button
    final Button tagManagerButton = new Button("Manage All Tags");
    tagManagerButton.setMinSize(100, 40);
    tagManagerButton.setOnAction(
        event ->
            // changes the scene to that of view.MasterTagWindow
            viewController.switchScene(new MasterTagWindow(viewController)));

    // Setting up file manager button
    final Button fileManagerButton = new Button("Manage All Images Modified");
    fileManagerButton.setMinSize(100, 40);
    fileManagerButton.setOnAction(
        event ->
            // changes the scene to that of allImagesModifiedWindow
            viewController.switchScene(
                new AllImagesModifiedScene(viewController)));

    // Setting up first modify chosen file button
    final Button modifyChosenFile1 = new Button("Modify Chosen File");
    setModifyChosenFileButton(modifyChosenFile1, imagesInDirectoryList);

    // Setting up second modify chosen file button
    final Button modifyChosenFile2 = new Button("Modify Chosen File");
    setModifyChosenFileButton(modifyChosenFile2, allImagesUnderDirectoryList);

    final Button searchTags = new Button("Search Existing Tags");
    searchTags.setMinSize(100, 40);

    searchTags.setOnAction(e -> viewController.switchScene(
        new SearchTagScene(viewController)));

    final Button masterLogButton = new Button("View the Log of All files");
    masterLogButton.setMinSize(100, 40);

    masterLogButton
        .setOnAction(e -> viewController.switchScene(new MasterLogScene(viewController)));

    // Setting up the vbox for the center layout
    VBox centerLayout = new VBox(20);
    centerLayout.setPadding(new Insets(20, 20, 20, 20));
    // Add the directory text, and the directory lists of images, and the modify file buttons
    centerLayout
        .getChildren()
        .addAll(
            directoryText,
            new Text("Images in chosen directory:"),
            imagesInDirectoryList,
            modifyChosenFile1,
            new Text("All Images under chosen directory:"),
            allImagesUnderDirectoryList,
            modifyChosenFile2);

    // Setting up buttons at the top of the window
    HBox topLayout = new HBox(20);
    topLayout.setPadding(new Insets(20, 20, 20, 20));
    // Add the browse, tag manager, file manager buttons
    topLayout.getChildren()
        .addAll(browseButton, tagManagerButton, fileManagerButton, searchTags, masterLogButton);
    // Set up border pane and add layouts
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(topLayout);
    borderPane.setCenter(centerLayout);

    // sets dimensions of scene and sets the window's scene
    mainScene = new Scene(borderPane, 1280, 720);
    return mainScene;
  }

  /**
   * Refreshes this Scene and updates all modification made
   */
  @Override
  public void refreshScene() {
    updateLists();
  }

  /**
   * Helper function to refresh and update displayed ImageFile list in this application.
   */
  private void updateLists() {
    // updates list1 and list2 if the user selects a directory
    if (selectedDirectory != null) {
      try {
        directoryText.setText("Chosen Directory : " + selectedDirectory.getAbsolutePath());
        ListImages listImages = new ListImages(selectedDirectory, centralController);
        // Add observers to all the image files
        listImages.addObserversToImageFiles(viewController);
        // Remove all the items that were already present from previous chosen directory
        imagesInDirectoryList.getItems().removeAll(imagesInDirectoryList.getItems());
        allImagesUnderDirectoryList.getItems().removeAll(allImagesUnderDirectoryList.getItems());
        // Add all the images in the chosen directory to list1
        imagesInDirectoryList.getItems().addAll(listImages.getImagesInDirectory());
        // Add thumbnails of the image to list view 1
        imagesInDirectoryList.setCellFactory(param -> new FillWithThumbnails());
        // Add all the images under the directory to list2
        allImagesUnderDirectoryList.getItems().addAll(listImages.getAllImagesUnderDirectory());
        // Add thumbnails of the image to list view 1
        allImagesUnderDirectoryList.setCellFactory(param -> new FillWithThumbnails());

      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }

  }

  /**
   * Sets up a modifyChosenFile button.
   *
   * @param button the button to set up
   * @param directory the directory to get files from
   */
  private void setModifyChosenFileButton(Button button, ListView<ImageFile> directory) {
    button.setOnAction(
        event -> {
          ImageFile selectedFile = directory.getSelectionModel().getSelectedItem();

          // Only respond if user has selected a file
          if (selectedFile != null) {

            centralController.getImageFileController().addImageFile(selectedFile);
            viewController.switchScene(
                new ImageModifierScene(viewController, selectedFile));
          }
        });
  }

}
