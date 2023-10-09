package domain;

import java.io.File;

public class FileWithId {

    private int id;
    private File file;

    public FileWithId(int id, File file){
        this.id = id;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
