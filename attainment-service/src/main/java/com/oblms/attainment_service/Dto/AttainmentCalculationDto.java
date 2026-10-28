package com.oblms.attainment_service.Dto;

public record AttainmentCalculationDto(
Long studentId,
Long assessmentId,
Long courseId,
Double marksObtained,
Double maximumMarks,
Double attainmentPercentage
){

}
