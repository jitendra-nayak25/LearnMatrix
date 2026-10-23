package com.oblms.attainment_service.Dto;

public record AttainmentReportDto(
        Long courseId,
        long totalRecords,
        double averageAttainment,
        double highestAttainment,
        double lowestAttainment
) {
}