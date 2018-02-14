package edu.cnm.deepdive.craps;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Roll {

  @FXML
  private ImageView die0;
  @FXML
  private ImageView die1;
  @FXML
  private Text total;

  public ImageView getDie0() {
    return die0;
  }

  public ImageView getDie1() {
    return die1;
  }

  public Text getTotal() {
    return total;
  }
}
