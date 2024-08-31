package app.workers;

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
    public List<File> listDirectoryFiles(String path) {
        return null;
    }
}