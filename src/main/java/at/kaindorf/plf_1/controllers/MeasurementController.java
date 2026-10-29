package at.kaindorf.plf_1.controllers;

import at.kaindorf.plf_1.DTO.StatistikResponse;
import at.kaindorf.plf_1.entities.Measurement;
import at.kaindorf.plf_1.services.MeasurementService;
import lombok.RequiredArgsConstructor;
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
        return measurementService.getMeanvalue(bundesland);
    }

    // 3)
    @GetMapping("standard/{standort}")
    public double getStandard(@PathVariable("standort") String standort) {
        return measurementService.getStandard(standort);
    }

    // 4)
    @GetMapping("validData/{standort}")
    public StatistikResponse<Long> getValidData(@PathVariable("standort") String standort) {
        return measurementService.getValidData(standort);
    }

}
