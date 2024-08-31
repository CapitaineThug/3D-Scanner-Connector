package app.workers;

import java.io.File;
import java.util.List;

/**
 * Cette interface définit les services "métier" de l'application.
 *
 * @author ...
 */
public interface WorkerItf {

    List<File> listDirectoryFiles(String path);    
    
}
