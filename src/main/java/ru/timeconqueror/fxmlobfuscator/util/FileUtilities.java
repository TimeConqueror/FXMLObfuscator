package ru.timeconqueror.fxmlobfuscator.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.function.BiConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtilities {
    public static void doForZip(BiConsumer<ZipFile, ZipEntry> action, File archive) {
        try {
            ZipFile zip = new ZipFile(archive);
            Enumeration entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                action.accept(zip, entry);
            }
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addFileToArchive(String contentInFile, File zipArchive, String inZipFilePath) throws IOException {
        try (FileSystem fs = FileSystems.newFileSystem(zipArchive.toPath(), null)) {
            try (InputStream stream = new ByteArrayInputStream(contentInFile.getBytes(StandardCharsets.UTF_8))) {
                Files.copy(stream, fs.getPath(inZipFilePath), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
