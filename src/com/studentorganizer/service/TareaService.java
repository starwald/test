package com.studentorganizer.service;

import com.studentorganizer.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TareaService {
    private Estudiante estudiante;
    
    public TareaService(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public void crearTarea(String nombre, String descripcion, LocalDate fechaEntrega, 
                          Prioridad prioridad, Curso curso) {
        Tarea nuevaTarea = new Tarea(nombre, descripcion, fechaEntrega, prioridad, curso);
        estudiante.agregarTarea(nuevaTarea);
    }
    
    public List<Tarea> obtenerTareasPorPrioridad(Prioridad prioridad) {
        return estudiante.getListaDeTareas().stream()
                .filter(tarea -> tarea.getPrioridad() == prioridad)
                .collect(Collectors.toList());
    }
    
    public List<Tarea> obtenerTareasPorCurso(Curso curso) {
        return estudiante.getListaDeTareas().stream()
                .filter(tarea -> tarea.getCurso().equals(curso))
                .collect(Collectors.toList());
    }
    
    public List<Tarea> obtenerTareasVencidas() {
        LocalDate hoy = LocalDate.now();
        return estudiante.getListaDeTareas().stream()
                .filter(tarea -> tarea.getFechaEntrega().isBefore(hoy) && 
                               tarea.getEstado() == Estado.PENDIENTE)
                .collect(Collectors.toList());
    }
}