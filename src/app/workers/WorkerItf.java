package app.workers;

import java.io.File;
import java.util.ArrayList;

/**
 * Cette interface définit les services "métier" de l'application.
 *
 * @author ...
 */
public interface WorkerItf {

    ArrayList<File> listDirectoryFiles(String path);    
    
}
