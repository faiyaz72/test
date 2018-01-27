package main;

import controller.CentralController;
import controller.ImageFileController;
import controller.TagController;
import controller.ViewController;
import model.DataBase;
import model.ImageFile;
import javafx.stage.Stage;

import java.io.IOException;


public class Application extends javafx.application.Application {


  private DataBase dataBase = new DataBase("DataBase.dat");

  private ImageFileController imageFileController = new ImageFileController(
      dataBase.getAllImageFiles());

  private TagController tagController = new TagController(dataBase.getAllTags());

  private CentralController centralController = new CentralController(imageFileController,
      tagController);

  private ViewController viewController = new ViewController(centralController);


  public static void main(String[] args) {

    launch(args);


  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    // updates the tag controllers information of the link between tags and imageFiles
    tagController.updateTagImageMap(dataBase.getAllImageFiles());

    // add observers to all the imageFiles
    for (ImageFile imageFile : centralController.getImageFileController().getAllImageFilesList()) {

      imageFile.addObserver(viewController);

    }

    // launch the window
    viewController.startActivity(primaryStage);

    // saves the application when its closed
    viewController.getWindow().setOnCloseRequest(event -> {
      try {
        dataBase.saveToFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

  }
}

