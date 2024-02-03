package com.example.project;

import java.time.LocalDate;

public class MedicalProcedure {
    private int medicalProcedureID;
    private LocalDate date;
    private String note;
    private int patientID;
    private int serviceID;

    public MedicalProcedure(int medicalProcedureID, LocalDate date, String note, int patientID, int serviceID) {
        this.medicalProcedureID = medicalProcedureID;
        this.date = date;
        this.note = note;
        this.patientID = patientID;
        this.serviceID = serviceID;
    }

    // Додайте гетери, сетери та інші необхідні методи
}
