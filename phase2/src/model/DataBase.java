package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/* Class responsible for storing information about saved files, saving files and reading files */
public class DataBase implements Serializable {

  private List<Tag> allTags;
  private List<ImageFile> allImageFiles;
  private String pathToReadAndSaveTo;


  /**
   * Constructs a DataBase object with a filepath to store itself.
   *
   * @param databaseStoragePath the filepath to store this DataBase object.
   */
  public DataBase(String databaseStoragePath) {
    this.pathToReadAndSaveTo = databaseStoragePath;
    allTags = new ArrayList<>();
    allImageFiles = new ArrayList<>();
    File databaseData = new File(databaseStoragePath);

    try {
      if (databaseData.exists()) {

        this.allTags = readFromFile().getAllTags();
        this.allImageFiles = readFromFile().getAllImageFiles();
      } else {
        // Create new save files and if data does'nt exist
        databaseData.createNewFile();
      }
    } catch (ClassNotFoundException | IOException error) {
      error.printStackTrace();
    }
  }

  /**
   * Reads the saved files for tag manager and image files.
   *
   * @return the Database object which saved the files for tag manager and image files.
   * @throws ClassNotFoundException Throws an exception if cannot load the image files from the
   * string names in file
   */
  private DataBase readFromFile() throws ClassNotFoundException {

    try {
      InputStream file = new FileInputStream(pathToReadAndSaveTo);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);
      // Storing it in a variable so that we can close the stream before returning
      DataBase dataBaseToReturn = (DataBase) input.readObject();
      input.close();
      return dataBaseToReturn;
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * Saves the data for image files and tag manager.
   *
   * @throws IOException Throws an exception if directory cannot be found or read
   */
  public void saveToFile() throws IOException {

    OutputStream file = new FileOutputStream(pathToReadAndSaveTo);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);
    // serialize this object
    output.writeObject(this);

    output.close();
  }


  public List<Tag> getAllTags() {
    return allTags;
  }


  public List<ImageFile> getAllImageFiles() {
    return allImageFiles;
  }
}
