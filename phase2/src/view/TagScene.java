package view;

import controller.CentralController;
import controller.ViewController;
import model.ImageFile;
import model.Tag;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

/* Superclass for Scenes that deal with the addition, deletion and creation of new Tags*/
public class TagScene implements IsScene {

  //the controller.viewController object, setting
  protected ViewController viewController;
  //the controller.centralController object, which responsible for communicating between GUI and
  // model package
  protected CentralController centralController;
  protected ImageFile imageFile;
  ListView<Tag> existingTagsList;

  /**
   * Constructs the TagScene
   *
   * @param imageFile ImageFile that needs to be modified
   * @param existingTagsList List of existing Tags in the database, that can be added and deleted.
   */
  TagScene(ViewController viewController, ImageFile imageFile, ListView<Tag> existingTagsList) {
    this.viewController = viewController;
    this.centralController = viewController.getCentralController();
    this.imageFile = imageFile;
    this.existingTagsList = existingTagsList;
  }

  /**
   * Creates and returns a scene to be used by the GUI
   *
   * @return null
   */
  @Override
  public Scene createAndReturnScene() {
    return null;
  }

  /**
   * Refreshes this Scene after modification made
   */
  @Override
  public void refreshScene() {
  }
}
