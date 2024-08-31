package app.workers;

import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Worker implements WorkerItf {

    // Attributs
    private ArrayList<File> listPhotos;

    // Constructeur
    public Worker() {
        this.listPhotos = new ArrayList<>();

    }

    @Override
    /**
     * Lis tous les fichiers d'un répertoire.
     * 
     * @return une liste vide si aucun fichier n'est trouvé
     */
    public ArrayList<File> listDirectoryFiles(String path) {
        // Extensions valides
        ArrayList<String> okExt = new ArrayList();
        okExt.add("jpeg");
        okExt.add("jpg");
        okExt.add("png");
        okExt.add("dng");
        okExt.add("tiff");
        okExt.add("nef");
        okExt.add("raw");
        okExt.add("webp");
        File[] filesTable;
        ArrayList<File> files = new ArrayList<>();

        File folder = new File(path);
        // Lister tous les fichiers
        filesTable = folder.listFiles();
        if (filesTable != null) {
            if (filesTable.length > 0) {
                for (int i = 0; i < filesTable.length; i++) {
                    // Tri par extension
                    String[] exts = filesTable[i].getName().split("\\.");
                    String lastExt = exts[exts.length - 1];
                    if (okExt.contains(lastExt.toLowerCase())) {
                        files.add(filesTable[i]);
                    }
                }
            }
        }
        // Retour
        return files;
    }
}