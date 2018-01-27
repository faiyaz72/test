package view;

import javafx.scene.Scene;

/*Interface which every class in the GUI must implement */
public interface IsScene {

  /**
   * Creates and returns a scene to be used by the GUI
   *
   * @return the scene associated with this object
   */
  Scene createAndReturnScene();

  // method to refresh the scene every time an image has been updated and GUI needs to be updated
  void refreshScene();
}
