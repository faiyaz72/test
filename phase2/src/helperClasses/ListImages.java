package helperClasses;

import controller.CentralController;
import model.ImageFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;

/* Class to help in getting all the images in a directory and converting them into imageFile objects */
public class ListImages {

  private File directory;
  private ArrayList<ImageFile> allImagesUnderDirectory;
  private ArrayList<ImageFile> imagesInDirectory;
  private CentralController centralController;

  /**
   * Constructs this helperClasses.ListImages object
   *
   * @param directoryOfInterest the directory from which all images will be extracted
   * @throws IOException Throws an exception if directory cannot be found or read
   */
  public ListImages(File directoryOfInterest, CentralController centralController)
      throws IOException {

    this.centralController = centralController;

    directory = directoryOfInterest;
    imagesInDirectory = new ArrayList<>();
    allImagesUnderDirectory = new ArrayList<>();

    // updates the imagesInDirectory list
    fillImagesInDirectory();

    // updates the allImagesUnderDirectory list
    fillAllImagesUnderDirectory(directory);
  }

  /**
   * Returns all images inside the directory including sub directories
   *
   * @return Returns all images inside the directory including sub directories
   * @throws IOException Throws an exception if directory cannot be found or read
   */
  public ArrayList<ImageFile> getAllImagesUnderDirectory() throws IOException {
    return allImagesUnderDirectory;
  }

  /**
   * Returns all images in the directory not considering subdirectories
   *
   * @return Returns all images in the directory not considering subdirectories
   * @throws IOException Throws an exception if directory cannot be found or read
   */
  public ArrayList<ImageFile> getImagesInDirectory() throws IOException {

    return imagesInDirectory;
  }

  // idea from https://stackoverflow.com/questions/9643228/test-if-file-is-an-image

  /**
   * Returns true if the File file is an image
   *
   * @param file the File to be probed
   * @return Returns true if the File file is an image
   * @throws IOException Throws an exception if directory cannot be found or read
   */
  private boolean isImage(File file) throws IOException {
    if (!file.isFile()) {
      return false;
    } else if (ImageIO.read(file) == null) // if cannot be read by imageIO, then not image
    {
      return false;
    }
    return true;
  }

  /**
   * Updates imagesInDirectory with all the imageFiles in directory
   *
   * @throws IOException Throws an exception if directory cannot be found or read
   */
  private void fillImagesInDirectory() throws IOException {
    // get the list of all files (including directories) in the directory of interest
    File[] fileArray = directory.listFiles();
    assert fileArray != null;
    for (File file : fileArray) {
      // the file is an image, then add it
      if (isImage(file)) {
        ImageFile imageFile = new ImageFile(file.getAbsolutePath());
        // if the image is already saved, reuse it
        if (centralController.getImageFileController().hasImageFile(imageFile)) {
          imagesInDirectory.add(centralController.getImageFileController().getImageFile(imageFile));
        } else {
          imagesInDirectory.add(imageFile);
        }
      }
    }
  }

  /**
   * Updates allImagesUnderDirectory with all the imageFiles under directory including sub
   * directories
   *
   * @throws IOException Throws an exception if directory cannot be found or read
   */
  private void fillAllImagesUnderDirectory(File directory) throws IOException {
    // get the list of all files (including directories) in the directory of interest
    File[] fileArray = directory.listFiles();
    assert fileArray != null;

    for (File file : fileArray) {
      // if the file is an image, then add it
      if (isImage(file)) {
        ImageFile imageFile = new ImageFile(file.getAbsolutePath());
        // if the image is already saved, reuse it
        if (centralController.getImageFileController().hasImageFile(imageFile)) {
          allImagesUnderDirectory
              .add(centralController.getImageFileController().getImageFile(imageFile));
        } else if (imagesInDirectory.contains(imageFile)) {
          allImagesUnderDirectory.add(imagesInDirectory.get(imagesInDirectory.indexOf(imageFile)));
        } else {
          allImagesUnderDirectory.add(imageFile);
        }
      } else if (file.isDirectory()) {
        fillAllImagesUnderDirectory(file);
      }
    }
  }

  /**
   * Adds this observer to all the image files
   *
   * @param observer the Observer object to inform changes in all image files.
   */
  public void addObserversToImageFiles(Observer observer) {
    for (ImageFile imageFile : allImagesUnderDirectory) {
      imageFile.addObserver(observer);
    }
  }

}
