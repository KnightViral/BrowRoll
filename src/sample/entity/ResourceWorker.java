package sample.entity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceWorker {

    public static List<String> getDirectories(String path) {
            File file = new File(path);
            String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
            if (directories != null)
                return Arrays.asList(directories);
        return new ArrayList<>();
    }

    public static List<File> getFiles(String path, String directory){
        ArrayList<File> res = new ArrayList<>();
            File dir = new File(path + directory);
            dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    File file = new File(dir, name);
                    if (file.isFile()) {
                        res.add(file);
                        return true;
                    }
                    return false;
                }
            });
            return res;
    }
}
