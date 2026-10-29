package at.kaindorf.plf_1.services;

import at.kaindorf.plf_1.entities.Measurement;
import at.kaindorf.plf_1.repositories.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class InitDB implements ApplicationRunner {

    private final JsonMapper jsonMapper = new JsonMapper();

    private final MeasurementRepository measurementRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (measurementRepository.count() > 0) {
            log.info("Messwerte bereits vorhanden – Import übersprungen.");
            return;
        } // KEINE DUPLIKATE

        List<String> standorte = List.of(
                "bregenz", "eisenstadt", "graz",
                "innsbruck", "klagenfurt", "linz",
                "salzburg", "stpoelten", "wien"
        );

        List<Measurement> measurements;

        int counter = 0;

        for (String s : standorte) {

            String path = "/data/" + s + ".json";

            try {
                InputStream jsonStream = InitDB.class.getResourceAsStream(path);

                if (jsonStream == null) {
                    throw new RuntimeException("Resource not found: " + path);
                }

                measurements = jsonMapper
                        .readerForListOf(Measurement.class)
                        .readValue(jsonStream); // da sind nur die Daten von JSON drin

                for (Measurement measurement : measurements) {
                    measurement.setStandort(s); // standort setzen!
                }

                measurementRepository.saveAll(measurements);
                counter = counter + measurements.size();


            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("LOG --> Loaded " + counter + " measurements");

    }
}
