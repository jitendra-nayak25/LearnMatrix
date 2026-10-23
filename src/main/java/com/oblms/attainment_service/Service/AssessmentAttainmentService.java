package com.oblms.attainment_service.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oblms.attainment_service.Dto.AssessmentDto;
import com.oblms.attainment_service.Dto.AssessmentResultDto;
import com.oblms.attainment_service.Dto.AttainmentCalculationDto;

@Service
public class AssessmentAttainmentService {
	private final AssessmentClientService assessmentClientService;

    public AssessmentAttainmentService(AssessmentClientService assessmentClientService) {
        this.assessmentClientService = assessmentClientService;
    }

    public AttainmentCalculationDto calculate(Long studentId, Long assessmentId) {

        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be positive");
        }

        if (assessmentId == null || assessmentId <= 0) {
            throw new IllegalArgumentException("Assessment ID must be positive");
        }

        // 1. Fetch all results for this student from Assessment-Service
        List<AssessmentResultDto> results = assessmentClientService.getResultsByStudent(studentId);

        // 2. Find the result for the specific assessment
        AssessmentResultDto matchingResult = results.stream()
                .filter(result ->
                        result.assessmentId() != null
                        && result.assessmentId().equals(assessmentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Assessment result not found for this student"));

        // 3. Fetch assessment details (to get maxMarks and courseId)
        AssessmentDto assessment = assessmentClientService.getAssessment(assessmentId);

        Double marksObtained = matchingResult.marksObtained();
        Double maximumMarks = assessment.maxMarks();

        // 4. Validate
        if (marksObtained == null || !Double.isFinite(marksObtained)
                || maximumMarks == null
                || !Double.isFinite(maximumMarks)
                || maximumMarks <= 0) {
            throw new IllegalArgumentException("Invalid marks or maximum marks");
        }

        if (marksObtained < 0 || marksObtained > maximumMarks) {
            throw new IllegalArgumentException("Marks must be between zero and maximum marks");
        }

        // 5. Calculate percentage
        double percentage = Math.round((marksObtained / maximumMarks) * 10000.0) / 100.0;

        // 6. Return DTO
        return new AttainmentCalculationDto(
                studentId,
                assessmentId,
                assessment.courseId(),
                marksObtained,
                maximumMarks,
                percentage
        );
    }

}
