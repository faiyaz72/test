package controller;

import model.ImageFile;
import model.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Delegates and manages image files */
public class ImageFileController {

  private List<ImageFile> allImageFilesList;

  /**
   * Constructs a new controller.ImageFileController object
   *
   * @param imageFileList the list of all image files
   */
  public ImageFileController(List<ImageFile> imageFileList) {
    this.allImageFilesList = imageFileList;
  }

  /**
   * Returns true if the list of all image files, allImageFilesList, contains this image file.
   *
   * @param imageFile the model.ImageFile object which will be checked
   * @return true if allImageFilesList contains imageFile
   */
  public boolean hasImageFile(ImageFile imageFile) {
    return allImageFilesList.contains(imageFile);
  }

  /**
   * Returns the index of this image file from the list of all image files, allImageFilesList.
   *
   * @param imageFile the model.ImageFile object which will be checked for index
   * @return the index of imageFile in allImageFilesList
   */
  public ImageFile getImageFile(ImageFile imageFile) {
    return allImageFilesList.get(allImageFilesList.indexOf(imageFile));
  }

  /**
   * Renames this imageFile with the names of all its current tags
   *
   * @param imageFile the model.ImageFile object which will be renamed
   */
  void renameImageFile(ImageFile imageFile) {
    imageFile.renameImage();
  }

  /**
   * Add this imageFile to the list of all image files
   *
   * @param imageFile the model.ImageFile object which will be added to allImageFilesList
   */
  public void addImageFile(ImageFile imageFile) {
    if (!hasImageFile(imageFile)) {
      allImageFilesList.add(imageFile);
    }
  }

  /**
   * Add the given tag to this imageFile
   *
   * @param imageFile the model.ImageFile object which tag will be added to
   * @param tag the model.Tag Object which needs will be added to imageFile
   */
  public void addTagToImageFile(ImageFile imageFile, Tag tag) {
    imageFile.addTag(tag);  //Add tag to imageFile
    imageFile.renameImage(); //Rename imageFile with added new tag.
  }

  /**
   * Remove tag from this imageFile
   *
   * @param imageFile the model.ImageFile object which will be modified
   * @param tag the model.Tag Object which to be removed from imageFile
   */
  public void removeTagFromFile(ImageFile imageFile, Tag tag) {
    imageFile.removeTag(tag); //Remove tag
    imageFile.renameImage(); //Rename imageFile after tag is removed
  }

  /**
   * Remove this tag from all ImageFiles that contained this tag
   *
   * @param tag the model.Tag Object which to be removed from all its associated imageFiles
   */
  public void removeTagFromAllFiles(Tag tag) {
    for (ImageFile imageFile : allImageFilesList) {
      imageFile.removeTag(tag);
      imageFile.renameImage();
    }
  }

  /**
   * Returns the list of all existing ImageFiles
   *
   * @return the list of all ImageFiles, allImageFilesList.
   */
  public List<ImageFile> getAllImageFilesList() {
    return allImageFilesList;
  }

  /**
   * Returns all a map with a date as a key and the name of the imageFile at that date for all
   * imageFiles
   *
   * @return the HashMap allLogMap between each imageFile and all its tag rename history.
   */
  public Map<String, String> getAllLog() {
    Map<String, String> allLogMap = new HashMap<>();
    for (ImageFile imageFile : allImageFilesList) {
      for (String key : imageFile.getTagHistory().keySet()) {
        allLogMap.put(key, imageFile.getOriginalNameWithTags(imageFile.getTagHistory().get(key)));
      }
    }
    return allLogMap;
  }
}
