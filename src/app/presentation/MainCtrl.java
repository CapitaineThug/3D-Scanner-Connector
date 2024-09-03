package app.presentation;

import app.helpers.DateTimeLib;
import app.workers.MetashapeWorker;
import app.workers.MetashapeWorkerItf;
import app.workers.Worker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import app.workers.WorkerItf;
import java.util.ArrayList;
import java.io.File;
import java.util.Date;

/**
 * Contrôleur de la vue principale.
 * 
 * @author ...
 */
public class MainCtrl implements Initializable {
  private WorkerItf wrk;
  private MetashapeWorkerItf metashapeWrk;
  @FXML
  private Text Txt_ProjectPath;
  @FXML
  private Button bt_LaunchCompute;
  @FXML
  private Button bt_UpdatePictures;
  @FXML
  private CheckBox cb_AutoPictureUpdate;
  @FXML
  private MenuItem mni_Close;
  @FXML
  private MenuItem mni_StartCompuse;
  @FXML
  private ScrollPane scp_Log;
  @FXML
  private TextField tf_PhotosPath;
  @FXML
  private TextField tf_ProjectName;
  @FXML
  private TextField tf_ProjectRoot;
  @FXML
  private VBox vb_Config;
  @FXML
  private VBox vb_ImageView;
  @FXML
  private VBox vb_Log;

  public void start() {
    Platform.startup(() -> {
      try {
        Stage mainStage = new Stage();
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(1024);
        mainStage.getIcons().add(new Image("resources/images/icon02.png"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        fxmlLoader.setControllerFactory(type -> {
          return this;
        });
        Parent root = (Parent) fxmlLoader.load();
        Scene principalScene = new Scene(root);
        mainStage.setScene(principalScene);
        mainStage.setTitle("3D Scanner Connector");
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
    metashapeWrk = new MetashapeWorker();
  }

  public void quitter() {
    Platform.exit();
  }

  /**
   * Mettre à jour toutes les images de la liste
   * 
   * @param pictures
   */
  public void updatePictures(String photoPath) {
    ArrayList<File> pictures = wrk.listDirectoryFiles(photoPath);
    vb_ImageView.getChildren().clear();
    if (pictures.size() > 0) {
      for (File file : pictures) {
        // HBox par image
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-background-color: #e0e0e0");

        // Setup l'image
        ImageView img = new ImageView(new Image(file.toURI().toString(), 50, 50, true, true));
        img.setFitWidth(64);
        img.setFitHeight(64);

        // Setup le texte
        Text txt = new Text(file.getName());

        // Ajouter le tout
        hbox.getChildren().add(img);
        hbox.getChildren().add(txt);
        vb_ImageView.getChildren().add(hbox);
      }
      // Log
      addLog("Images du répertoire '" + photoPath + "' scannées et affichées (" + pictures.size() + " éléments)");
    } else {
      addLog("Aucune images détectées dans '" + photoPath + "'");
    }
  }

  public void addLog(String log) {
    Date date = new Date();
    String print = "[ " + DateTimeLib.dateTimeToString(date) + "] : " + log;
    vb_Log.getChildren().add(new Text(print));
    scp_Log.setVvalue(1.0);

  }

  /**
   * Mets à jour le nom complet du répertoire de projet
   */
  public void updateProjectPath() {
    Txt_ProjectPath.setText(tf_ProjectRoot.getText() + "\\" + tf_ProjectName.getText());
  }

  @FXML
  void actionAbout(ActionEvent event) {

  }

  @FXML
  void actionQuitter(ActionEvent event) {
    quitter();

  }

  @FXML
  /**
   * Définire la location des photos prises par la caméra
   * 
   * @param event
   */
  void actionSetPhotoPath(ActionEvent event) {
    String photoPath = tf_PhotosPath.getText();
    updatePictures(photoPath);

  }

  @FXML
  void actionSetProjectName(ActionEvent event) {
    String projectName = tf_ProjectName.getText();
    updateProjectPath();
    addLog("Nom de projet définit à '" + projectName + "'");

  }

  @FXML
  void actionSetProjectRoot(ActionEvent event) {
    String projectRoot = tf_ProjectRoot.getText();
    updateProjectPath();
    addLog("Location des projets Metashape définie à '" + projectRoot + "'");
  }

  @FXML
  void actionStartCompute(ActionEvent event) {

  }

  @FXML
  void action_ToggleAutoUpdatePicture(ActionEvent event) {
    if (cb_AutoPictureUpdate.isSelected()) {
      updatePictures(tf_PhotosPath.getText());
    }
  }

  @FXML
  void actionUpdatePictures(ActionEvent event) {
    updatePictures(tf_PhotosPath.getText());
  }

  @FXML
  void actonLaunchCompute(ActionEvent event) {
    ArrayList<File> pictures = wrk.listDirectoryFiles(tf_PhotosPath.getText());
    String projectName = tf_ProjectName.getText();
    String projectRoot = tf_ProjectRoot.getText();
    if (pictures != null) {
      if (!pictures.isEmpty()) {
        if (wrk.isPathExisting(projectRoot)) {
          if (wrk.isNameWriteable(projectName)) {
            try {
              // Vérifier l'existence de Metashape
              String metashapeVersion = metashapeWrk.verifMetashapeExe();

              if (metashapeVersion != null) {
                addLog(metashapeVersion);
                // Lancer la création du projet
                metashapeWrk.createMetashapeProject(pictures, projectName, projectRoot);
              } else {
                addLog("ERROR METASHAPE, MANAGED BY EXCEPTION NORMALLY...");
              }
            } catch (Exception ex) {
              addLog(ex.toString());
            }

          } else {
            addLog("Le nom de projet doit être renseigné et valide !");
          }
        } else {
          addLog("Le chemin racine des projets n'est pas valide !");
        }
      } else {
        addLog("Le scan requis des images pour être lancé");
      }
    } else {
      addLog("Erreur de lecture des photos");
    }

  }
}
