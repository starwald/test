package com.studentorganizer.gui;

import com.studentorganizer.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class CalendarFrame extends JFrame {
    private Estudiante estudiante;
    private YearMonth currentMonth;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    
    public CalendarFrame(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.currentMonth = YearMonth.now();
        
        initializeComponents();
        setupUI();
        updateCalendar();
    }
    
    private void initializeComponents() {
        setTitle("Calendario - " + estudiante.getNombre());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2));
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 20));
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Panel superior con navegación
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(102, 126, 234));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JButton prevButton = new JButton("◀ Anterior");
        JButton nextButton = new JButton("Siguiente ▶");
        
        prevButton.setBackground(Color.WHITE);
        prevButton.setForeground(new Color(102, 126, 234));
        nextButton.setBackground(Color.WHITE);
        nextButton.setForeground(new Color(102, 126, 234));
        
        prevButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });
        
        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });
        
        monthLabel.setForeground(Color.WHITE);
        
        topPanel.add(prevButton, BorderLayout.WEST);
        topPanel.add(monthLabel, BorderLayout.CENTER);
        topPanel.add(nextButton, BorderLayout.EAST);
        
        // Panel del calendario
        JPanel calendarContainer = new JPanel(new BorderLayout());
        calendarContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Encabezados de días
        JPanel headerPanel = new JPanel(new GridLayout(1, 7));
        String[] dayNames = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (String day : dayNames) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
            dayLabel.setBorder(BorderFactory.createRaisedBevelBorder());
            dayLabel.setBackground(new Color(248, 249, 250));
            dayLabel.setOpaque(true);
            headerPanel.add(dayLabel);
        }
        
        calendarContainer.add(headerPanel, BorderLayout.NORTH);
        calendarContainer.add(calendarPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(calendarContainer, BorderLayout.CENTER);
    }
    
    private void updateCalendar() {
        calendarPanel.removeAll();
        
        // Actualizar etiqueta del mes
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));
        monthLabel.setText(monthName + " " + currentMonth.getYear());
        
        // Obtener primer día del mes
        LocalDate firstDay = currentMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // 0 = domingo
        
        // Espacios vacíos antes del primer día
        for (int i = 0; i < dayOfWeek; i++) {
            calendarPanel.add(new JPanel());
        }
        
        // Días del mes
        int daysInMonth = currentMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            JPanel dayPanel = createDayPanel(date);
            calendarPanel.add(dayPanel);
        }
        
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
    
    private JPanel createDayPanel(LocalDate date) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(100, 100));
        
        // Número del día
        JLabel dayLabel = new JLabel(String.valueOf(date.getDayOfMonth()), SwingConstants.CENTER);
        dayLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Resaltar día actual
        if (date.equals(LocalDate.now())) {
            dayLabel.setForeground(Color.WHITE);
            panel.setBackground(new Color(102, 126, 234));
        }
        
        panel.add(dayLabel);
        
        // Obtener tareas para este día
        List<Tarea> tareasDelDia = estudiante.getAgenda().verEventosDelDia(date);
        
        // Mostrar indicadores de tareas
        for (int i = 0; i < Math.min(tareasDelDia.size(), 3); i++) {
            Tarea tarea = tareasDelDia.get(i);
            JLabel taskLabel = new JLabel("• " + tarea.getNombre().substring(0, Math.min(10, tarea.getNombre().length())));
            taskLabel.setFont(new Font("Arial", Font.PLAIN, 9));
            
            // Color según prioridad
            switch (tarea.getPrioridad()) {
                case ALTA:
                    taskLabel.setForeground(new Color(220, 53, 69));
                    break;
                case MEDIA:
                    taskLabel.setForeground(new Color(255, 193, 7));
                    break;
                case BAJA:
                    taskLabel.setForeground(new Color(40, 167, 69));
                    break;
            }
            
            panel.add(taskLabel);
        }
        
        // Mostrar "..." si hay más tareas
        if (tareasDelDia.size() > 3) {
            JLabel moreLabel = new JLabel("...", SwingConstants.CENTER);
            moreLabel.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(moreLabel);
        }
        
        return panel;
    }
}