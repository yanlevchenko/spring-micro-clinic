package com.example.patientservice.service;

import com.example.patientservice.client.OrderConsumer;
import com.example.patientservice.data.Order;
import com.example.patientservice.dto.PatientIdWrapper;
import com.example.patientservice.dto.PatientRequest;
import com.example.patientservice.mapper.PatientMapper;
import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final OrderConsumer orderConsumer;

    public Patient createPatient(PatientRequest patientRequest) {
        Patient patient = patientMapper.patientRequestToPatient(patientRequest);

        return patientRepository.save(patient);
    }

    public Patient updatePatient(String patientId, PatientRequest patientRequest) {
        Patient foundPatient = patientRepository
                .findById(patientId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Patient for updating not found");
                });

        patientMapper.updatePatientFromRequest(foundPatient, patientRequest);

        return patientRepository.save(foundPatient);
    }

    public void deactivatePatient(String patientId) {
        Patient foundPatient = patientRepository
                .findById(patientId)
                .orElseThrow(() -> {
                            throw new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Patient for deleting not found");
                        }
                );

        patientRepository.deleteById(foundPatient.getPatientId());
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Map<JSONObject, List<Order>> getPatientListWithTheirActiveOrders(
            PatientIdWrapper patientIdsWrapper) {
        Map<String, List<Order>> activeOrders =
                orderConsumer.getOrdersByPatientIdsAndPatientState(patientIdsWrapper, "ACTIVE")
                        .stream()
                        .collect(Collectors.groupingBy(Order::getPatientId));

        Map<JSONObject, List<Order>> patientsWithTheirOrders = new HashMap<>();
        for (Patient patient : patientRepository.findByPatientIdIn(patientIdsWrapper.getPatientIds())) {
            if (activeOrders.get(patient.getPatientId()) != null) {
                patientsWithTheirOrders.put(
                        new JSONObject(patient),
                        activeOrders.get(patient.getPatientId())
                );
            }
        }
        return patientsWithTheirOrders;
    }
}
