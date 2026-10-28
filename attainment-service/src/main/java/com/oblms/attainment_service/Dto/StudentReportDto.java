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
public class StudentReportDto {
	 private Long studentId;
	    private Integer totalCourses;
	    private Double overallAttainment;
	    private String overallStatus;          // ACHIEVED / NEEDS_IMPROVEMENT
	    private List<CourseReportDto> courses;
}
