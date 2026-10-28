package com.oblms.attainment_service.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseReportDto {
	private Long courseId;
    private Double attainmentPercentage;
    private String status;                 // ACHIEVED / NEEDS_IMPROVEMENT
    private List<CoAttainmentDto> cos;
}
