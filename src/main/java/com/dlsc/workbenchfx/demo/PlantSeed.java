package com.dlsc.workbenchfx.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cg.core.util.Log;

class PlantSeed{
    public static Set<String> listFilesUsingFileWalk(String dir, int depth) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
            return stream
                .filter(file -> !Files.isDirectory(file))
                .map(Path::getFileName)
                .map(Path::toString)
                .collect(Collectors.toSet());
        }
    }
    public static void main(String[] args) throws Exception {
        Set<String> tmp = listFilesUsingFileWalk("/home/chen/",2);
        Log.info(tmp);
    }
}
