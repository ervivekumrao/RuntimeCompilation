import umrao.compile.OnTheFlyCompiler;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("*********** Main class run started. ***********");
        new OnTheFlyCompiler().compile();
        System.out.println("*********** Main class run ended. *************");

        //Delete generated Java and Class files from out directory. Comment below line to see files
        delete(new File("out"));
    }

    static void delete(File file) {
        if (file.isDirectory()) {
            File[] innerFiles = file.listFiles();
            if (innerFiles != null) {
                for (File c : innerFiles) {
                    delete(c);
                }
            }
        }
        if (!file.delete()) {
            System.out.println("Failed to delete file: " + file);
        }
    }
}