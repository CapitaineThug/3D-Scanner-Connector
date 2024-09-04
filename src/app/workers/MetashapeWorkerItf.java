package app.workers;

import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

public interface MetashapeWorkerItf {

    String createMetashapeProject(ArrayList<File> photos, String projectName, String projectDirectory) throws IOException;

    String verifMetashapeExe() throws Exception;

}
