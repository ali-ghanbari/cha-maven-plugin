package edu.utdallas.cha.commons;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

public class Util {
    public static Collection<File> listClassFiles(File classPath){
        if (!classPath.isDirectory()) {
            return Collections.emptyList();
        }
        return FileUtils.listFiles(classPath, new String[]{"class"}, true);
    }
}