package controller;

import view.IsScene;
import view.LauncherWindow;
import javafx.stage.Stage;

import java.util.*;

/* Class responsible for controlling the GUI */
public class ViewController implements Observer {

  private Stage window;
  private IsScene firstScene;
  private Stack<IsScene> scenesSeen;
  private IsScene currentScene;

  private CentralController centralController;

  public ViewController(CentralController centralController) {

    this.centralController = centralController;
    this.scenesSeen = new Stack<>();
  }

  /**
   * Starts the program by displaying the window with launcherWindow set as the first scene
   *
   * @param primaryStage the Stage on to display to the user
   */
  public void startActivity(Stage primaryStage) {

    this.window = primaryStage;

    this.window.setTitle("TAGGER");

    firstScene = new LauncherWindow(this);

    this.window.setScene(firstScene.createAndReturnScene());
    scenesSeen.add(firstScene);

    this.window.show();
  }

  /**
   * Changes the Scene to newScene
   *
   * @param newScene the newScene to display
   */
  public void switchScene(IsScene newScene) {

    window.setScene(newScene.createAndReturnScene());
    scenesSeen.push(newScene);
    currentScene = newScene;
  }

  /**
   * Changes the Scene to the one before the current Scene
   */
  public void goBackToPreviousScene() {

    scenesSeen.pop();
    currentScene = scenesSeen.peek();

    this.window.setScene(currentScene.createAndReturnScene());
  }

  /**
   * Changes the scene to the first scene which is LauncherWindow by default
   */
  public void goToMainScene() {

    scenesSeen.clear();
    this.window.setScene(firstScene.createAndReturnScene());
    scenesSeen.add(firstScene);
  }

  public CentralController getCentralController() {
    return centralController;
  }

  @Override
  public void update(Observable o, Object arg) {

    for (IsScene scene : scenesSeen) {

      scene.refreshScene();
    }
  }

  public Stage getWindow() {
    return window;
  }
}
