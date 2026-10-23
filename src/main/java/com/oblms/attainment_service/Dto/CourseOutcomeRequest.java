package com.oblms.attainment_service.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseOutcomeRequest {
	 @NotNull
	    private Long courseId;

	    @NotBlank
	    private String coCode;              // "CO1"

	    private String description;
	    private Double targetPercentage;
	    private Double thresholdPercentage;

}
