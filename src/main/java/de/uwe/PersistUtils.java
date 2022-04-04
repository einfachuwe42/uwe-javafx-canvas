package de.uwe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersistUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        //mapper.registerModule(new JavaTimeModule());
    }

    public static <T> T loadJsonObject(Class<T> clazz){
        T data = null;
        try {
            File file = getFileFromClass(clazz);
            if(!file.exists()){
                saveJsonObject(data, clazz);
            }
            data = mapper.readValue(file, clazz);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public static void saveJsonObject(Object object, Class<?> clazz){
        try {
            String string = mapper.writeValueAsString(object);
            File file = getFileFromClass(clazz);
            Files.writeString(file.toPath(), string, StandardCharsets.UTF_8);
            System.out.println("Datei "+file.getPath()+" geschrieben!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static File getFileFromClass(Class<?> clazz){
        final String className = clazz.getTypeName();
        final String name = className.substring(className.lastIndexOf(".")+1);
        File file = new File(name.concat(".json"));
        System.out.println("filename "+file.getPath());
        return file;
    }

    public static void writeSticksToFile(String fileName, List<String> lines) {

        try{
            File fileOut = new File(fileName);
            FileOutputStream fos = new FileOutputStream(fileOut);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static List<String> readSticksFromFile(String fileName) {

        List<String> result = new ArrayList<>();

        try{
            File fileIn = new File(fileName);
            FileInputStream fos = new FileInputStream(fileIn);
            Scanner scanner = new Scanner(fos);
            while (scanner.hasNext()){
                result.add(scanner.nextLine());
            }

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return result;
    }


}
