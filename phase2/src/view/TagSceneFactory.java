package view;

import controller.ViewController;
import model.ImageFile;
import model.Tag;
import javafx.scene.control.ListView;

/* A TagSceneFactory which creates and return the desired Scene related to Tags */
class TagSceneFactory {

  /**
   * Creates and returns the desired Scene
   *
   * @param SceneName SceneName to create
   * @param imageFile ImageFile to be modified
   * @param existingTagsList existing list of tags to be inputted.
   * @return Scene with the desired specifications.
   */
  IsScene getScene(String SceneName, ViewController viewController,
      ImageFile imageFile, ListView<Tag> existingTagsList) {
    if (SceneName == null) {
      return null;
    } else if (SceneName.equals("AddTagFromDatabaseScene")) {
      return new AddTagFromDatabaseScene(viewController, imageFile, existingTagsList);
    } else if (SceneName.equals("CreateNewTagScene")) {
      return new CreateNewTagScene(viewController, imageFile, existingTagsList, true);
    } else if (SceneName.equals("DeleteTagSceneForImage")) {
      return new DeleteTagScene(viewController, imageFile, existingTagsList, true);
    } else if (SceneName.equals("DeleteTagSceneMaster")) {
      return new DeleteTagScene(viewController, imageFile, existingTagsList, false);
    } else if (SceneName.equals("CreateNewTagMasterScene")) {
      return new CreateNewTagScene(viewController, imageFile, existingTagsList, false);
    } else if (SceneName.equals("RenameTagSceneMaster")) {
      return new RenameTagScene(viewController, imageFile, existingTagsList);
    } else if (SceneName.equals("ShowFilesWithSelectedTag")) {
      return new ShowFilesWithSelectedTagScene(viewController, imageFile, existingTagsList);
    } else {
      return null;
    }
  }
}
