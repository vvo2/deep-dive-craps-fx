package edu.cnm.deepdive.craps;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{

  private static final String RESOURCE_BUNDLE_PATH = "resources/strings";
  private static final String FXML_PATH = "resources/main.fxml";
  public static final String WINDOW_TITLE_KEY = "windowTitle";
  public static final String ICON_PATH = "resources/icon.png";

  private ClassLoader classLoader; //good at finding things as class path
  private ResourceBundle bundle;
  private FXMLLoader fxmlLoader;
  private Controller controller;

  @Override
  public void start(Stage stage) throws Exception { //like onCreate in android
    setupLoaders();
    setupStage(stage, loadLayout()); //start stage and families of actor
  }

  private void setupLoaders(){ // object to prep to load
    classLoader = getClass().getClassLoader(); //use the same class to get classloader
    bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_PATH);
    fxmlLoader = new FXMLLoader(classLoader.getResource(FXML_PATH), bundle); //setup loader object
  }

  private Parent loadLayout() throws IOException {
    Parent root = fxmlLoader.load(); // load the FXML, load all root ei parent and child (Vbox etc
    controller = fxmlLoader.getController(); // load controller found while loading FXML
    //TODO somthing with controller
    return root; // return the entire family (Vbox etc)
  }

  private void setupStage(Stage stage, Parent root) { //setup the all loaded root
    Scene scene = new Scene(root); // families of actor to stage
    stage.setTitle(bundle.getString(WINDOW_TITLE_KEY)); //title of the stage
    stage.getIcons().add(new Image(classLoader.getResourceAsStream(ICON_PATH)));//new image and pipe to pass icon
    stage.setResizable(false);
    stage.setScene(scene); //has the whole family and put it on the scene
    stage.show(); //open the curtain
  }

  public static void main(String[] args) {
    launch(args);
  }
}
