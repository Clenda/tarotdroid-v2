package org.nla.tarotdroid.core;

import android.os.Environment;

import org.nla.tarotdroid.core.dal.IDalService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

public class ImportDatabaseHelper {

    private final IDalService dalService;

    public ImportDatabaseHelper(IDalService dalService) {
        this.dalService = dalService;
    }

    public void importFile(final String importFilePath) throws Exception {
        File sdcard = Environment.getExternalStorageDirectory();
        File tarotDroidDir = new File(sdcard.getAbsolutePath(), "TarotDroid");
        if (!tarotDroidDir.exists()) {
            tarotDroidDir.mkdir();
        }
        File exportFile = new File(tarotDroidDir, "export_backup.xml");
        exportFile.createNewFile();
        PrintStream printStream = new PrintStream(exportFile);
        printStream.print(dalService.exportDatabaseContent());
        printStream.close();

        File importFile = new File(importFilePath);
        StringBuilder xmlContent = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(importFile));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            xmlContent.append(line);
        }
        reader.close();

        dalService.importDatabaseContent(xmlContent.toString());
    }
}
