package com.oblms.attainment_service.Dto;

public record AssessmentDto(
        Long id,
        String title,
        String type,
        Double maxMarks,
        Long courseId
) {
}