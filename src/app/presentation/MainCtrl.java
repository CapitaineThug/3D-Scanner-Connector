package app.presentation;

import app.workers.Worker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import app.workers.WorkerItf;

/**
 * Contrôleur de la vue principale.
 * 
 * @author ...
 */
public class MainCtrl implements Initializable {
  private WorkerItf wrk;

  public void start() {
    Platform.startup(() -> {
      try {
        Stage mainStage = new Stage();
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(1024);
        mainStage.getIcons().add(new Image("resources/images/icon.png"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        fxmlLoader.setControllerFactory(type -> {
          return this;
        });
        Parent root = (Parent) fxmlLoader.load();
        Scene principalScene = new Scene(root);
        mainStage.setScene(principalScene);
        mainStage.setTitle("Application JAVAFX 21");
        mainStage.show();
      } catch (IOException ex) {
        ex.printStackTrace();
        Platform.exit();
      }
    });
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    wrk = new Worker();

  }

  public void quitter() {
    // faire qq chose avant de quitter
    // wrk.fermerBD();
    // System.out.println("Je vous quitte !");

    // obligatoire pour bien terminer une application JavaFX
    Platform.exit();
  }

}
