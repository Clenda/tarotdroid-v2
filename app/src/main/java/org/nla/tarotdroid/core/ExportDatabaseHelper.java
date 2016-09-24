package org.nla.tarotdroid.core;

import android.os.Environment;

import org.nla.tarotdroid.core.dal.IDalService;

import java.io.File;
import java.io.PrintStream;

public class ExportDatabaseHelper {

    private final IDalService dalService;
    private String fileName;

    public ExportDatabaseHelper(final IDalService dalService) {
        this.dalService = dalService;
    }

    public String exportDatabase() throws Exception {
        String contentToExport = dalService.exportDatabaseContent();

        if (contentToExport != null) {
            File sdcard = Environment.getExternalStorageDirectory();
            File tarotDroidDir = new File(sdcard.getAbsolutePath(), "TarotDroid");
            if (!tarotDroidDir.exists()) {
                tarotDroidDir.mkdir();
            }
            File exportFile = new File(tarotDroidDir, "export.xml");
            exportFile.createNewFile();

            PrintStream printStream = new PrintStream(exportFile);
            printStream.print(contentToExport);
            printStream.close();
            fileName = exportFile.getAbsolutePath();
        }

        return contentToExport;
    }

    public String getFileName() {
        return fileName;
    }
}