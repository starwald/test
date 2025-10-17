package com.studentorganizer.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.studentorganizer.model.Estudiante;
import com.studentorganizer.util.LocalDateAdapter;
import com.studentorganizer.util.LocalDateTimeAdapter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PersistenceService {

    private final Gson gson;
    private final String filePath;

    public PersistenceService(String filePath) {
        this.filePath = filePath;
        // Construimos un Gson que sabe cómo manejar nuestras fechas especiales
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting() // Hace que el archivo JSON sea legible para humanos
                .create();
    }

    /**
     * Carga la lista de estudiantes desde el archivo JSON.
     * @return Una lista de Estudiantes, o una lista vacía si el archivo no existe o hay un error.
     */
    public List<Estudiante> loadEstudiantes() {
        try (FileReader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Estudiante>>() {}.getType();
            List<Estudiante> estudiantes = gson.fromJson(reader, listType);
            return estudiantes != null ? estudiantes : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Archivo de datos '" + filePath + "' no encontrado. Se creará uno nuevo al guardar.");
            return new ArrayList<>(); // Si no hay archivo, empezamos con una lista vacía
        }
    }

    /**
     * Guarda la lista completa de estudiantes en el archivo JSON.
     * @param estudiantes La lista de todos los estudiantes a guardar.
     */
    public void saveEstudiantes(List<Estudiante> estudiantes) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(estudiantes, writer);
        } catch (IOException e) {
            System.err.println("Error crítico al guardar los datos en '" + filePath + "'.");
            e.printStackTrace();
        }
    }
}