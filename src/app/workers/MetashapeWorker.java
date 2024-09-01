package app.workers;

import java.io.File;
import java.util.ArrayList;
import com.agisoft.metashape.*;

public class MetashapeWorker {

    // Attributs

    // Constructeur
    public MetashapeWorker() {

    }

    /**
     * Crée le fichier de projet et y importe les photos
     */
    public void createMetashapeProject(ArrayList<File> photos, String projectName, String projectDirectory) {

        // Construire le chemin du projet
        String projectPath = projectDirectory + File.separator + projectName + File.separator + projectName + ".psx";

        // Vérifier et créer le répertoire du projet si nécessaire
        File projectDir = new File(projectDirectory + File.separator + projectName);
        if (!projectDir.exists()) {
            if (!projectDir.mkdirs()) {
                System.out.println("Erreur : Impossible de créer le répertoire du projet.");
                return;
            }
        }

        // Créer le projet
        Document document = new Document();
        document.save(projectPath, null);

        // Ajouter un chunk au document
        Chunk chunk = document.addChunk();

        // Ajouter les photos
        String[] photosPath = new String[photos.size()];
        for (int i = 0; i < photos.size(); i++) {
            photosPath[i] = photos.get(i).getAbsolutePath();
            System.out.println("Photo ajoutée : " + photosPath[i]);
        }
        chunk.addPhotos(photosPath, null);

        // Sauvegarder le projet
        try {
            document.save(document.getPath(), null);
            System.out.println("Projet sauvegardé à : " + document.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la sauvegarde du projet.");
        }
    }
}
