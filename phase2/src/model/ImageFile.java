package model;

import helperClasses.DesktopAPI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/* An imageFile object to be modified by the user */
public class ImageFile extends Observable implements Serializable {

  private String imageName;
  private String originalName;
  private String imagePath;
  private String imageDirectory;
  private File thisImageFile;
  private List<Tag> currentTags;
  private Map<String, List<Tag>> tagHistory;

  /**
   * Constructs a new imageFile from the file at the specified path
   *
   * @param initialPath the path of the file from which imageFile is to be made
   */
  public ImageFile(String initialPath) {
    this.imagePath = initialPath;
    thisImageFile = new File(initialPath);
    imageName = thisImageFile.getName();

    // Extract the name of the file exclusive of the extension
    int lastDot = imageName.lastIndexOf(".");
    originalName = imageName.substring(0, lastDot);

    // gets the directory of the file
    imageDirectory = thisImageFile.getParent();

    currentTags = new ArrayList<>();
    tagHistory = new HashMap<>();

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    // tag history will have no tags associated at the time when first created
    tagHistory.put(dtf.format(now), new ArrayList<>());

  }

  // Copied from https://examples.javacodegeeks.com/desktop-java/imageio/determine-format-of-an-image/

  /**
   * Extracts the extension fie. the image format from the file
   *
   * @return String the format of the image
   * @throws IOException throws exception if the file does not exist or cannot be read
   */
  private String getImageFormat() throws IOException {

    // create an image input stream from the specified file
    ImageInputStream imageInputStream;
    imageInputStream = ImageIO.createImageInputStream(thisImageFile);

    // get all currently registered readers that recognize the image format
    Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
    if (!iter.hasNext()) {
      throw new RuntimeException("No readers found!");
    }

    ImageReader reader = iter.next(); // get the first reader
    imageInputStream.close(); // close stream
    return "." + reader.getFormatName();
  }

  /**
   * Opens the directory in which this imageFile is contained
   */
  public void openImageDirectory() {
    // Use third party Desktop API that will take into account the specific operating system.
    new DesktopAPI().open(thisImageFile.getParentFile());
  }

  /**
   * Moves this image file to the new directory
   *
   * @param newDirectory the path of the new directory in which this imageFile is to be moved
   * @throws IOException throws exception if the file does not exist or cannot be read
   */
  public void moveImage(File newDirectory) throws IOException {
    this.imageDirectory = newDirectory.getAbsolutePath();
    // set the new path to the directory / the name of th image + the extension
    this.imagePath =
        this.imageDirectory + File.separatorChar + this.imageName + this.getImageFormat();

    thisImageFile.renameTo(new File(imagePath));
    thisImageFile = new File(imagePath);
    setChanged();
    notifyObservers();
  }

  /**
   * renames this imageFile with the names of all its current tags
   */
  public void renameImage() {
    StringBuilder newName = new StringBuilder(originalName);
    for (Tag tag : currentTags) {
      newName.append(" ").append(tag.getTagName());
    }
    imageName = newName.toString();

    // change the path of this imageFile to reflect the new name
    try {
      imagePath = imageDirectory + File.separatorChar + imageName + this.getImageFormat();
    } catch (IOException e) {
      e.printStackTrace();
    }
    thisImageFile.renameTo(new File(imagePath));

    // change the reference of thisImageFile as the rename method does not automatically do so
    thisImageFile = new File(imagePath);
    setChanged();
    notifyObservers();
  }

  /**
   * Adds a model.Tag object to this imageFile
   *
   * @param tagToAdd model.Tag to be added to the list of current tags
   */
  public void addTag(Tag tagToAdd) {
    // check if already contains the tag
    if (!currentTags.contains(tagToAdd)) {
      currentTags.add(tagToAdd);
      // get the time stamp and change the tag history accordingly
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();

      tagHistory.put(dtf.format(now), new ArrayList<>(currentTags));
    }
  }

  /**
   * Removes the model.Tag object from this imageFile
   *
   * @param tag the model.Tag object to be removed
   */
  public void removeTag(Tag tag) {
    if (currentTags.contains(tag)) {
      currentTags.remove(tag);
      // get the time stamp and change the tag history accordingly
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      tagHistory.put(dtf.format(now), new ArrayList<>(currentTags));
    }
  }

  /**
   * Returns a string representation of a list of all the tags associated with the ImageFile.
   *
   * @param tags the model.Tag object to get tag name from.
   * @return the string representation to display all tags of the ImageFile
   */
  public String getOriginalNameWithTags(List<Tag> tags) {
    StringBuilder stringBuilder = new StringBuilder(originalName);
    for (Tag tag : tags) {
      stringBuilder.append(" ").append(tag.getTagName());
    }
    return stringBuilder.toString();
  }

  // Source : https://docs.oracle.com/javase/8/javafx/api/javafx/scene/image/ImageView.html

  /**
   * Returns an ImageView object with width displayWidth and height displayHeight
   *
   * @param displayWidth the width of the ImageView
   * @param displayHeight the height of the ImageView
   * @return Returns an ImageView object with width displayWidth and height displayHeight
   * @throws FileNotFoundException Throws exception if file cannot be found
   */
  public ImageView getImage(int displayWidth, int displayHeight) throws FileNotFoundException {
    Image image = new Image(new FileInputStream(imagePath));

    ImageView imageView = new ImageView();
    imageView.setImage(image);

    // re-sizes the image to have width of displayWidth and height displayHeight while
    // preserving the ratio and using higher quality filtering method; this ImageView is also
    // cached to improve performance

    imageView.setFitWidth(displayWidth);
    imageView.setFitHeight(displayHeight);

    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);
    imageView.setCache(true);

    return imageView;
  }

  /**
   * Returns true if they refer to the same object or if the image files have the same path.
   *
   * @param o the reference object with which to compare.
   * @return true if this refers to the same object as o, or if they are both ImageFiles and have
   * same file path.
   */

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    // two image files are equal if they have the same path
    ImageFile image = (ImageFile) o;
    return imagePath != null ? imagePath.equals(image.imagePath) : image.imagePath == null;
  }

  /**
   * Returns a hash code value of the imagePath for this ImageFile object
   *
   * @return a hash code value of the imagePath for this ImageFile object.
   */
  @Override
  public int hashCode() {
    return imagePath != null ? imagePath.hashCode() : 0;
  }

  /**
   * Returns the tag history of this Tag Object
   *
   * @return the map of tag history of this Tag Object
   */
  public Map<String, List<Tag>> getTagHistory() {
    return tagHistory;
  }

  /**
   * Returns the file name of this ImageFile object
   *
   * @return the String file name include extension of this ImageFile object
   */
  public String getImageName() {
    return imageName;
  }

  /**
   * Returns all tags current assigned to this ImageFile object
   *
   * @return a list of all the tags associated with the ImageFile object
   */
  public List<Tag> getCurrentTags() {
    return currentTags;
  }

  /**
   * Returns the file path of this ImageFile object
   *
   * @return the imagePath of the ImageFile object.
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Returns true if tag already exists in the current tag list of this ImageFile object
   *
   * @param tag the model.Tag object to be checked
   * @return if the currentTags of this ImageFile contains tag
   */
  public boolean hasTag(Tag tag) {
    return currentTags.contains(tag);
  }

  /**
   * Replaces and update ImageFile with a newer version of the same tag.
   *
   * @param tag the model.Tag object to be replaced and updated
   */
  public void replaceTagWith(Tag tag) {
    // remove the older version of same tag
    currentTags.remove(tag);
    // add newer version
    currentTags.add(tag);
  }

  /**
   * Returns the original file name of the ImageFile before using this Tagger.
   *
   * @return the original file name of this ImageFile object prior to be renamed with tags.
   */
  public String getOriginalName() {
    return originalName;
  }
}
