package edu.cnm.deepdive.craps;

import edu.cnm.deepdive.craps.model.Craps;
import edu.cnm.deepdive.craps.model.Craps.State;
import edu.cnm.deepdive.craps.model.Game;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Controller {

  private static final String DIE_FACE_FORMAT = "resources/face_%d.png";
  private static final String ROLL_FXML_PATH = "resources/roll.fxml";
  public static final String WIN_CSS_CLASS = "win";
  public static final String LOSS_CSS_CLASS = "loss";

  @FXML private Button step;
  @FXML private ToggleButton run;
  @FXML private Button reset;
  @FXML private Text tally;
  @FXML private ListView rollsList;

  private String tallyFormat;
  private Game game;
  private Object talkingStick;
  private boolean running = false;
  private Runner runner = null;
  private Updater updater = null;
  private ObservableList<HBox> rolls = FXCollections.observableArrayList();
  private Image[] faces;
  private ClassLoader classLoader;
  private URL rollLocation;

  public void initialize() {
    //the class that loaded me i want use the same class
    classLoader = getClass().getClassLoader();
    rollLocation = classLoader.getResource(ROLL_FXML_PATH);
    faces = new Image[6];
    for (int i = 0; i < faces.length; i++) {
      faces[i] = new Image(classLoader.getResourceAsStream(String.format(DIE_FACE_FORMAT, i + 1)));
    }
    updater = new Updater();
    talkingStick = new Object();
    tallyFormat = tally.getText();
    rollsList.setItems(rolls);
    game = new Game();
    game.reset();
    updateTally();
    updateList();

  }

  private void updateTally(){
    long wins = game.getWins();
    long plays = game.getWins() + game.getLosses();
    double percentage = (plays > 0) ? (100.0 * wins / plays) : 0;
    tally.setText(String.format(tallyFormat, wins, plays, percentage));
  }

  private void updateList() {
    try {
      List<int[]> data = game.getCraps().getRolls();
      Craps.State state = game.getCraps().getState();
      rolls.clear();
      for (int[] dice : data) {
        FXMLLoader fxmlLoader = new FXMLLoader(rollLocation);
        HBox display = fxmlLoader.load();
        Roll roll = fxmlLoader.getController();
        roll.getDie0().setImage(faces[dice[0] - 1]);
        roll.getDie1().setImage(faces[dice[1] - 1]);
        roll.getTotal().setText(Integer.toString(dice[0] + dice[1]));
        display.getStyleClass().add((state == State.WIN) ? WIN_CSS_CLASS : LOSS_CSS_CLASS);
        rolls.add(display);
      }

    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @FXML private void step(ActionEvent event) {
    game.play();
    updateTally();
    updateList();
  }

  @FXML private void reset(ActionEvent event){
    game.reset();
    updateTally();
    updateList();
  }

  @FXML private  void run(ActionEvent event){
    if (run.isSelected()) {
      step.setDisable(true);
      reset.setDisable(true);
      runner = new Runner();
      running = true;
      runner.start();
      updater.start();
    } else {
      updater.stop();
      running = false;
      runner = null;
      reset.setDisable(false);
      step.setDisable(false);
    }
  }

  private class Runner extends Thread {

    @Override
    public void run() {
     while (running){
      synchronized (talkingStick) {
        game.play();
      }
     }
    }
  }

  private class Updater extends AnimationTimer {

    @Override
    public void handle(long now) {
      synchronized (talkingStick){
        updateTally();
        updateList();
      }
    }
  }

}
