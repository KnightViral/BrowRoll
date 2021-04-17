package sample.entity;

import javafx.scene.control.TableView;
import sample.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SaveLoadWizard {

    static final String FILE_NAME = "BrowRoll";

    public static void save(TableView<WheelPoint> table, String winner) {
        System.out.println("Saving");
        List<WheelPointSave> list = new ArrayList<>();
        for (WheelPoint item : table.getItems()) {
            list.add(item.save());
        }
        RollSave rollSave = new RollSave(winner, list);

        try {
            Files.createDirectories(Paths.get(Utils.getLocalAppData() + "\\" + FILE_NAME + "\\"));
            File longTermSave = new File(Utils.getFileNameWithDate(FILE_NAME) + ".txt");
            FileOutputStream fos = new FileOutputStream(Utils.getLocalAppData() + "\\" + FILE_NAME + "\\" + longTermSave.getName());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(rollSave);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            //todo exception
            e.printStackTrace();
        }
    }

    public static RollSave load(String fileName) {
        System.out.println("Loading");
        RollSave rollSave = null;
        try {
            FileInputStream fis = new FileInputStream(Utils.getLocalAppData() + "\\" + FILE_NAME + "\\" + fileName);
            ObjectInputStream oin = new ObjectInputStream(fis);
            rollSave = (RollSave) oin.readObject();
            fis.close();
            oin.close();
        } catch (IOException | ClassNotFoundException e) {
            //todo exception
            e.printStackTrace();
        }
        return rollSave;
    }

    public static String getFileName() {
        return FILE_NAME;
    }
}
