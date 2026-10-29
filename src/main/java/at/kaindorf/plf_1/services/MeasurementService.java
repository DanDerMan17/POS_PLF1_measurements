package at.kaindorf.plf_1.services;

import at.kaindorf.plf_1.DTO.StatistikResponse;
import at.kaindorf.plf_1.entities.Measurement;
import at.kaindorf.plf_1.repositories.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    // Lädt Messungen und entfernt alle mehrfach vorhandenen Tage je Standort.
    private List<Measurement> getValidMeasurements(String standort) {

        standort = standort.trim();

        boolean oesterreich = standort.equalsIgnoreCase("österreich")
                || standort.equalsIgnoreCase("oesterreich");

        List<Measurement> alle = measurementRepository.findAll();
        List<Measurement> gueltige = new ArrayList<>();

        for (Measurement m : alle) {
            if (!oesterreich
                    && !m.getStandort().trim().equalsIgnoreCase(standort)) {
                continue;
            }

            int counter = 0;

            for (Measurement andere : alle) {
                if (m.getStandort().trim().equalsIgnoreCase(
                        andere.getStandort().trim())
                        && m.getDatum().equals(andere.getDatum())) {
                    counter++;
                }
            }

            if (counter == 1) {
                gueltige.add(m);
            }
        }

        return gueltige;
    }

    private double mittelwert(List<Measurement> messungen) {

        if (messungen.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Keine gültigen Messwerte");
        }

        double summe = 0;

        for (Measurement m : messungen) {
            summe += m.getTemperatur();
        }

        return summe / messungen.size();
    }

    public double getMeanvalue(String standort) {
        return mittelwert(getValidMeasurements(standort));
    }

    public double getStandard(String standort) {

        List<Measurement> messungen = getValidMeasurements(standort);

        if (messungen.size() < 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Mindestens zwei gültige Messwerte erforderlich");
        }

        double durchschnitt = mittelwert(messungen);
        double summe = 0;

        for (Measurement m : messungen) {
            double abweichung = m.getTemperatur() - durchschnitt;
            summe += abweichung * abweichung;
        }

        return Math.sqrt(summe / (messungen.size() - 1));
    }

    public StatistikResponse<Long> getValidData(String standort) {

        standort = standort.trim().toLowerCase();
        long anzahl = 0;

        for (Measurement m : getValidMeasurements(standort)) {
            if (m.getDatum().getYear() == 2025) {
                anzahl++;
            }
        }

        if (standort.equals("österreich") || standort.equals("oesterreich")) {
            standort = "Österreich";
        }

        return new StatistikResponse<>(standort, anzahl);
    }
}