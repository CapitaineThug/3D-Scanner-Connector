package app.workers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MetashapeWorker implements MetashapeWorkerItf {

    @Override
    /**
     * Créée le projet Metashape ainsi que la structure de fichiers et importe le
     * projet
     * 
     * @return null, ou résultat console de l'exécution de la commande si elle a pu
     *         être lancée
     */
    public String createMetashapeProject(ArrayList<File> photos, String projectName, String projectsRoot)
            throws IOException {

        // Variables
        String result = null;
        String exeName = "metashape.exe";
        String scriptPath = new File("script" + File.separator + "CreateProjectFiles.py").getPath();
        String projectDirectory = projectsRoot + File.separator + projectName;
        String projectImagesPath = projectDirectory + File.separator + "IMG";

        // Créer les répertoires si inexistants
        File projectPath = new File(projectDirectory);
        if (!projectPath.exists()) {
            projectPath.mkdir();
        }
        projectPath = new File(projectImagesPath);
        if (!projectPath.exists()) {
            projectPath.mkdir();
        }

        // Copier les Photos
        if (photos != null && !photos.isEmpty()) {
            for (File file : photos) {
                file.renameTo(new File(projectImagesPath + File.separator + file.getName()));
            }
        }

        // Lancer la création du projet
        ProcessBuilder metashapeBuilder = new ProcessBuilder(exeName, "-r", scriptPath, projectImagesPath,
                projectDirectory,
                projectName);
        Process metashape = metashapeBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(metashape.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (result == null) {
                result = "";
            }
            result = result + " " + line;
        }

        // Lancer le programme
        Process project = new ProcessBuilder(exeName, projectDirectory + File.separator + projectName + ".psx").start();

        // Retour
        return result;
    }

    @Override
    /**
     * Vérifie l'existence de Metashape en demandant sa version.
     * 
     * @return null, ou la sortie de consile de Metashape si le processus a pu être
     *         exécuté
     */
    public String verifMetashapeExe() throws Exception {
        // Variables
        String exeName = "metashape.exe";
        String result = null;

        // Vérifier l'existence de Metashape
        Process metashapeVersion = new ProcessBuilder(exeName, "--version").start();

        // Affiche la sortie
        BufferedReader reader = new BufferedReader(new InputStreamReader(metashapeVersion.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (result == null) {
                result = "";
            }
            result = result + " " + line;
        }
        // Retour du résultat
        return result;

    }
}
