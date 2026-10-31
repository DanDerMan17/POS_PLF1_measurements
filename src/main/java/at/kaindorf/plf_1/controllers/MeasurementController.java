package at.kaindorf.plf_1.controllers;

import at.kaindorf.plf_1.DTO.StatistikResponse;
import at.kaindorf.plf_1.entities.Measurement;
import at.kaindorf.plf_1.services.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    // ALLE Measurements check
    @GetMapping("allMeasurements")
    List<Measurement> getAllMeasurements() {
        return measurementService.getAllMeasurements();
    }

    // 2)
    @GetMapping("meanvalue/{bundesland}")
    public double getMeanvalue(@PathVariable("bundesland") String bundesland) {
        try {
            return measurementService.getMeanvalue(bundesland);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    // 3)
    @GetMapping("standard/{standort}")
    public double getStandard(@PathVariable("standort") String standort) {
        try {
            return measurementService.getStandard(standort);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    // 4)
    @GetMapping("validData/{standort}")
    public StatistikResponse<Long> getValidData(@PathVariable("standort") String standort) {
        String name = standort.trim().toLowerCase(java.util.Locale.ROOT);
        if (name.equals("österreich") || name.equals("oesterreich")) {
            name = "Österreich";
        }
        return new StatistikResponse<>(name, measurementService.getValidData(standort));
    }

}
