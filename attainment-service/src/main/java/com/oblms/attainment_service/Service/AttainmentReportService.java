package com.oblms.attainment_service.Service;

import com.oblms.attainment_service.Dto.AttainmentReportDto;
import com.oblms.attainment_service.Entity.Attainment;
import com.oblms.attainment_service.Repository.AttainmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttainmentReportService {

    private final AttainmentRepository attainmentRepository;

    public AttainmentReportService(AttainmentRepository attainmentRepository) {
        this.attainmentRepository = attainmentRepository;
    }

    public AttainmentReportDto getCourseReport(Long courseId) {

        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Course ID must be positive");
        }

        List<Attainment> records = attainmentRepository.findByCourseId(courseId);

        if (records.isEmpty()) {
            throw new IllegalArgumentException("No attainment records found for this course");
        }

        // Filter out null values to prevent NullPointerException
        List<Double> validValues = records.stream()
                .map(Attainment::getAttainmentValue)
                .filter(val -> val != null)
                .toList();

        if (validValues.isEmpty()) {
            throw new IllegalArgumentException("No valid attainment values found");
        }

        double average = validValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double highest = validValues.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double lowest = validValues.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);

        return new AttainmentReportDto(
                courseId,
                validValues.size(),
                roundToTwoDecimals(average),
                roundToTwoDecimals(highest),
                roundToTwoDecimals(lowest)
        );
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}