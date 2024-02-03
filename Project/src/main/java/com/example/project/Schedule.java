package com.example.project;


import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private int scheduleID;
    private LocalDate date;
    private LocalTime time;
    private String problemDescription;
    private int patientID;
    private int dentistID;

    public Schedule(int scheduleID, LocalDate date, LocalTime time, String problemDescription, int patientID, int dentistID) {
        this.scheduleID = scheduleID;
        this.date = date;
        this.time = time;
        this.problemDescription = problemDescription;
        this.patientID = patientID;
        this.dentistID = dentistID;
    }
    // Додайте гетери, сетери та інші необхідні методи

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getDentistID() {
        return dentistID;
    }

    public void setDentistID(int dentistID) {
        this.dentistID = dentistID;
    }
}
