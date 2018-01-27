package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.ImageFile;
import model.Tag;

/**
 * Class responsible for calling the methods in TagController and ImageFileController
 */
public class CentralController {

  private final TagController tagController;
  private final ImageFileController imageFileController;

  /**
   * Constructs a new CentralController object
   *
   * @param imageFileController ImageFileController object to be stored.
   * @param tagController TagController object to be stored.
   */
  public CentralController(ImageFileController imageFileController, TagController tagController) {
    this.imageFileController = imageFileController;
    this.tagController = tagController;
  }

  /**
   * Revert and rename image file to old names according to its log.
   *
   * @param imageFile the model.ImageFile to be name reverted.
   * @param oldTags a list of old tags the imageFile had at one time.
   */
  public void revertImageFileToTags(ImageFile imageFile, List<Tag> oldTags) {

    // the list of old tags imageFile will be reverted back to
    List<Tag> currentTags = new ArrayList<>(imageFile.getCurrentTags());

    // remove current tags in imageFile
    for (Tag currentTag : currentTags) {
      tagController.removeFileFromTag(currentTag, imageFile);
      imageFileController.removeTagFromFile(imageFile, currentTag);
    }

    // add old tags back in imageFile
    for (Tag oldTag : oldTags) {
      tagController.addFileToTag(oldTag, imageFile);
      imageFileController.addTagToImageFile(imageFile, oldTag);
    }

    // rename imageFile according to its updated tags
    imageFileController.renameImageFile(imageFile);
  }


  /**
   * Adds a tag to an imageFile and the imageFile to the tag
   *
   * @param tag the model.Tag to be added.
   * @param imageFile the model.imageFile to add the tag to.
   * @throws IOException if unable to add the tag to the imageFile.
   */
  public void addTag(Tag tag, ImageFile imageFile) throws IOException {
    tagController.addFileToTag(tag, imageFile);
    imageFileController.addTagToImageFile(imageFile, tag);
  }

  /**
   * Dissociates a tag from the imageFile, and the imageFile from the tag
   *
   * @param tag model.Tag to be dissociated.
   * @param imageFile the model.ImageFile to dissociate the tag from.
   * @throws IOException if unable to dissociate tag.
   */
  public void dissociateTag(Tag tag, ImageFile imageFile) throws IOException {
    // remove all associated files from this  tag
    tagController.removeFileFromTag(tag, imageFile);
    // remove this tag from all associated files
    imageFileController.removeTagFromFile(imageFile, tag);
  }

  /**
   * Deletes an existing tag and removes the tag from all the files
   *
   * @param tag the model.Tag to be deleted.
   * @throws IOException if unable to delete the tag.
   */
  public void deleteExistingTag(Tag tag) throws IOException {
    imageFileController.removeTagFromAllFiles(tag); // deel
    tagController.deleteTag(tag);
  }

  /**
   * Creates a tag and adds it to an image file.
   *
   * @param tagName the name of the tag.
   * @param image the image file to add tag to.
   * @throws IOException if unable to add tag to imageFile.
   */
  public void createTagWithImageFile(String tagName, ImageFile image) throws IOException {
    tagController.createTagWithFile(tagName, image);
    Tag temp = tagController.getTagWithName(tagName);
    imageFileController.addTagToImageFile(image, temp);

  }

  /**
   * Returns the tagController of this CentralController object
   *
   * @return tagController of this CentralController
   */
  public TagController getTagController() {
    return tagController;
  }

  /**
   * Returns the imageFileController of this CentralController object
   *
   * @return imageFileController of this CentralController
   */
  public ImageFileController getImageFileController() {
    return imageFileController;
  }
}
