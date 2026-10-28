package com.oblms.attainment_service.Dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttainmentRequest {
	@NotNull(message = "Course ID is required")
	@Positive(message = "Course ID must be positive")
    private Long courseId;

	@NotNull(message = "Course Outcome ID is required")
	@Positive(message = "Course Outcome ID must be positive")
    private Long coId;

	@NotNull(message = "Student ID is required")
	@Positive(message = "Student ID must be positive")
    private Long studentId;

	@NotNull(message = "Attainment value is required")
	@DecimalMin(value = "0.0", message = "Attainment cannot be below 0")
	@DecimalMax(value = "100.0", message = "Attainment cannot exceed 100")
    private Double attainmentValue;
    private Double targetValue;
    private String attainmentLevel;
    private String academicYear;
    private String semester;

}
