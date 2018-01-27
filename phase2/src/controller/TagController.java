package controller;

import model.ImageFile;
import model.Tag;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Delegates the managing of individual Tags */
public class TagController implements Serializable {

  private List<Tag> tagList;

  private Map<Tag, List<ImageFile>> tagImageMap;

  /**
   * Constructs a new controller.TagController object
   *
   * @param tagList the List of all tags.
   */
  public TagController(List<Tag> tagList) {

    this.tagList = tagList;
    this.tagImageMap = new HashMap<>();
  }

  /**
   * Updates the tagImageMap so that TagController has up to date information about the tags
   * associated with the image List
   *
   * @param imageFilesStored the image files from which information about tag association is to be
   * known
   */
  public void updateTagImageMap(List<ImageFile> imageFilesStored) {

    for (Tag tag : tagList) {

      List<ImageFile> imageFilesForThisTag = new ArrayList<>();

      for (ImageFile imageFile : imageFilesStored) {

        if (imageFile.hasTag(tag)) {

          imageFilesForThisTag.add(imageFile);
          imageFile.replaceTagWith(tag);
        }
      }

      tagImageMap.put(tag, imageFilesForThisTag);
    }
  }

  /**
   * Updates tagImage map to include the association between the Tag and ImageFile provided
   *
   * @param tag model.Tag Object which needs to be modified
   * @param fileToAdd model.ImageFile that needs to be added to tag
   */
  public void addFileToTag(Tag tag, ImageFile fileToAdd) {

    if (this.hasTag(tag)) {

      tagImageMap.get(getTag(tag)).add(fileToAdd);
    } else {

      tagList.add(tag);

      List<ImageFile> imageFilesToAdd = new ArrayList<>();
      imageFilesToAdd.add(fileToAdd);

      tagImageMap.put(tag, imageFilesToAdd);
    }
  }

  /**
   * Removes mapping association between the tag and the image file if this file has this tag.
   *
   * @param tag model.Tag object which needs to be checked
   * @param fileToRemove model.ImageFile that needs to be removed from tag
   */
  public void removeFileFromTag(Tag tag, ImageFile fileToRemove) {
    if (this.hasTag(tag)) {
      tagImageMap.get(getTag(tag)).remove(fileToRemove);
    }
  }

  /**
   * Creates a blank new tag with name tagName.
   *
   * @param tagName Name of new model.Tag that needs to be created
   */
  public void createTag(String tagName) {
    if (!(checkTag(tagName))) {
      Tag newTag = new Tag(tagName);
      tagList.add((newTag));
      tagImageMap.put(newTag, new ArrayList<>());
    }
  }

  /**
   * Creates a blank new tag with name tagName, and associates the selected ImageFile with this new
   * tag.
   *
   * @param tagName Name of new model.Tag that needs to be created
   * @param fileToAdd model.ImageFile that needs to be associated with tagName
   */
  public void createTagWithFile(String tagName, ImageFile fileToAdd) {

    Tag possibleNewTag = new Tag(tagName);
    if (this.hasTag(possibleNewTag)) {
      tagImageMap.get(getTag(possibleNewTag)).add(fileToAdd);
    } else {

      List<ImageFile> imageFilesToAdd = new ArrayList<>();
      imageFilesToAdd.add(fileToAdd);
      tagImageMap.put(possibleNewTag, imageFilesToAdd);
      tagList.add(possibleNewTag);
    }
  }

  /**
   * Checks if there is a tag with name tagName in current tag database.
   *
   * @param tagName Name of model.Tag that needs to be checked
   * @return true if model.Tag already exists in tagList
   */
  public boolean checkTag(String tagName) {
    for (Tag tag : tagList) {
      if (tagName.equals(tag.toString())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Renames this tag to the given new name.
   *
   * @param tag model.Tag Object which needs to be modified
   * @param newName new String name of tag
   */
  public void renameTag(Tag tag, String newName) {
    // Rename tag to newName
    if (this.hasTag(tag)) {
      getTag(tag).changeTagName(newName);
    }
    // Rename all imageFiles with old tag name to newName
    for (ImageFile imageFile : tagImageMap.get(tag)) {
      imageFile.renameImage();
    }
  }

  /**
   * Deletes this tag from database, and remove this tag from all associated image files.
   *
   * @param tag model.Tag object which needs to be deleted.
   */
  public void deleteTag(Tag tag) {
    if (this.hasTag(tag)) {
      tagList.remove(tag);
      tagImageMap.remove(tag);
    }
  }

  /**
   * Checks if tagList contains tag
   *
   * @param tag model.Tag Object which needs to be checked
   * @return true if tagList contains tag
   */
  private boolean hasTag(Tag tag) {
    return tagList.contains(tag);
  }

  /**
   * Returns a list of all image files associated with this tag.
   *
   * @param tag model.Tag object which needs to be checked
   * @return List of model.ImageFile which is associated with tag
   */
  public List<ImageFile> showFilesWithTag(Tag tag) {
    if (this.hasTag(tag)) {
      return tagImageMap.get(tag);
    } else {
      return null;
    }
  }

  /**
   * Returns the tag with String name tagName.
   *
   * @param tagName Name of model.Tag that needs to be checked
   * @return The tag with name tagName.
   */
  public Tag getTagWithName(String tagName) {
    for (Tag tag : tagList) {
      if (tagName.equals(tag.toString())) {
        return tag;
      }
    }
    return null;
  }

  /**
   * Returns a list of tags at least partially match the String tagNameToSearch
   *
   * @param tagNameToSearch String tagNameToSearch which will be searched against all tag names in
   * tagList
   * @return A list of tags that their tag name contains tagNameToSearch
   */
  public List<Tag> searchTag(String tagNameToSearch) {
    List<Tag> tagListToReturn = new ArrayList<>();
    for (Tag tag : tagList) {
      if (tag.toString().toLowerCase().contains(tagNameToSearch.toLowerCase())) {
        tagListToReturn.add(tag);
      }
    }
    return tagListToReturn;
  }

  /**
   * Returns the index of tag
   *
   * @param tag model.Tag Object which needs to be checked
   * @return the index of tag in tagList.
   */
  private Tag getTag(Tag tag) {
    return tagList.get(tagList.indexOf(tag));
  }

  /**
   * Returns the list of all existing tags
   *
   * @return List of all tags in database.
   */
  public List<Tag> getExistingTags() {
    return tagList;
  }
}
