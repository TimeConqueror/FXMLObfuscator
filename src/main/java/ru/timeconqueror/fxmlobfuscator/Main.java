package ru.timeconqueror.fxmlobfuscator;

import org.apache.commons.io.IOUtils;
import ru.timeconqueror.fxmlobfuscator.mapping.Mappings;
import ru.timeconqueror.fxmlobfuscator.mapping.ProGuardMappingsParser;
import ru.timeconqueror.fxmlobfuscator.obfuscating.CSSObfuscator;
import ru.timeconqueror.fxmlobfuscator.obfuscating.FXMLObfuscator;
import ru.timeconqueror.fxmlobfuscator.obfuscating.Obfuscator;
import ru.timeconqueror.fxmlobfuscator.util.FileUtilities;
import ru.timeconqueror.fxmlobfuscator.util.ContentFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if(args.length != 2){
            System.err.println("The program accept TWO args, such as:\n" +
                    "1. path_to_file_to_be_replaced\n" +
                    "2. path_to_mappings_file\n" +
                    "WARNING: If you have the path with spaces, replace them with %20");
            return;
        }

        String fileInPath = args[0].replace("%20", " ");
        String mappingsPath = args[1].replace("%20", " ");

        ArrayList<ContentFile> fxmls = new ArrayList<>();
        ArrayList<ContentFile> cssFiles = new ArrayList<>();

        File mappingsFile = new File(mappingsPath);
        File fileIn = new File(fileInPath);
        if(!mappingsFile.exists()){
            System.out.println("File " + mappingsFile.getAbsolutePath() + "doesn't exist");
        } else if(!fileIn.exists()){
            System.out.println("File " + fileIn.getAbsolutePath() + "doesn't exist");
        }

        Mappings mappings = new ProGuardMappingsParser().parse(mappingsFile);

        FileUtilities.doForZip((zipFile, zipEntry) -> {
            if (zipEntry != null && !zipEntry.isDirectory()) {
                String name = zipEntry.getName();
                if (name.endsWith(".fxml")) {
                    try (InputStream stream = zipFile.getInputStream(zipEntry)) {
                        fxmls.add(new ContentFile(name, IOUtils.toString(stream, StandardCharsets.UTF_8)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (name.endsWith(".css")){
                    try (InputStream stream = zipFile.getInputStream(zipEntry)) {
                        cssFiles.add(new ContentFile(name, IOUtils.toString(stream, StandardCharsets.UTF_8)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, fileIn);

        Obfuscator fxmlObf = new FXMLObfuscator(mappings);
        obfAndWriteToJar(fxmls, fileIn, fxmlObf);

        Obfuscator cssObf = new CSSObfuscator(mappings);
        obfAndWriteToJar(cssFiles, fileIn, cssObf);

        System.out.println("Program was successfully finished.");
    }

    private static void obfAndWriteToJar(ArrayList<ContentFile> files, File archiveIn, Obfuscator obfuscator) {
        for (ContentFile file : files) {
            System.out.println(file.getName() + " file obfuscation started.");
            obfuscator.obfuscate(file);
            System.out.println(file.getName() + " file obfuscation finished.");
            System.out.println(file.getName() + " started copying back to jar.");
            try {
                FileUtilities.addFileToArchive(file.getContent(), archiveIn, file.getName());
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            System.out.println(file.getName() + " successfully finished copying back to jar.");
        }
    }
}
