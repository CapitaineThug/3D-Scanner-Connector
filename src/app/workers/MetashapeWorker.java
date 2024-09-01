package app.workers;

import java.io.File;
import java.util.ArrayList;
import com.agisoft.metashape.*;
import com.agisoft.metashape.tasks.*;

public class MetashapeWorker {

    // Attributs

    // Constructeur
    public MetashapeWorker() {

    }

    /**
     * Créée le fichier de projet et y importe les photos
     */
    public void createMetashapeProject(ArrayList<File> photos, String projectName, String projectDirectory) {

        // Variables
        String[] photosPath = new String[photos.size()];
        String projectPath = projectDirectory + File.pathSeparator + projectName + File.pathSeparator + projectName
                + ".psx";
        // Initiliser Metashape
        Metashape metashape = new Metashape();

        // Créer le projet
        Document document = new Document();
        document.save(projectPath, null);

        // Ajouter les chunks
        Chunk chunk = document.addChunk();

        // Ajouter les photos
        for (int i = 0; i < photos.size(); i++) {
            photosPath[i] = photos.get(i).getPath();
            System.out.println(photos.get(i).getPath());
        }
        chunk.addPhotos(photosPath, null);

        // Sauvegarder le projet
        document.save(document.getPath(), null);
    }

}
