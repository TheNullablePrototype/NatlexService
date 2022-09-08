package com.prototype.natlexservice.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {

    public static byte[] readFile(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteIfExists(Path path) {
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
