package com.example.patientservice.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "patients")
public class Patient implements Serializable {

    @Id
    @GenericGenerator(
            name = "patient_id_gen",
            strategy = "com.example.patientservice.model.generator.PatientIdGenerator"
    )
    @GeneratedValue(generator = "patient_id_gen")
    @Column(name = "patient_id")
    private String patientId;

    @JsonIgnore
    @Column(name = "first_name")
    private String firstName;

    @JsonIgnore
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "create_date_time_gmt")
    private LocalDate createDateTimeGmt;

    @Column(name = "update_date_time_gmt")
    private LocalDate updateDateTimeGmt;

    @Column(name = "patient_state")
    private String patientState;

    @JsonGetter(value = "fullName")
    public String getFullName() {
        return (firstName + " " + lastName);
    }

}
