package com.example.patientservice.dto;

import com.example.patientservice.data.PatientState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate createDateTimeGmt;
    private LocalDate updateDateTimeGmt;
    private PatientState patientState;
}
