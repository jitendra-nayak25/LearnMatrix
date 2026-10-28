package com.oblms.attainment_service.Dto;

public record AssessmentResultDto(
        Long id,
        Long studentId,
        Long assessmentId,
        Double marksObtained
) {
}