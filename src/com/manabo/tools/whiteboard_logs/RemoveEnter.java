package com.manabo.tools.whiteboard_logs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class RemoveEnter {

    private RemoveEnter(String suffix, ArrayList<String> filenames) {
        for (String filename : filenames) {
            File file = new File(filename);
            if (!file.exists() || !file.isFile()) {
                System.err.println("File not found. " + file.toString());
                return;
            }
        }
        for (String filename : filenames) {
            removeEnter(filename, filename + "." + suffix);
        }
    }

    private void removeEnter(String inFilename, String outFilename) {
        File file = new File(inFilename);
        if (!file.exists()) {
            System.err.println("File not found. " + inFilename);
            return;
        }
        System.out.println("process " + inFilename);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);

            FileOutputStream fos = new FileOutputStream(outFilename);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            try (BufferedReader br = new BufferedReader(isr); BufferedWriter bw = new BufferedWriter(osw)) {
                String line = null;
                String lastLine = "";
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("[")) {
                        bw.write(lastLine);
                        bw.newLine();
                        lastLine = line;
                        continue;
                    }
                    lastLine += "\\n";
                    lastLine += line;
                }
                bw.write(lastLine);
                bw.newLine();
                bw.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("USAGE:");
            System.out.println("RemoveEnter [output suffix] [in filename]...");
            return;
        }
        ArrayList<String> list = new ArrayList<String>();
        for (String filename : args) {
            list.add(filename);
        }
        list.remove(0);
        new RemoveEnter(args[0], list);
    }
}
