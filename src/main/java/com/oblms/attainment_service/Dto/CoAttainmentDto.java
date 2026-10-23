package com.oblms.attainment_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoAttainmentDto {
	private Long coId;
    private String coCode;                 // "CO1"
    private String description;
    private Double attainmentValue;        // raw attained value (optional)
    private Double targetValue;            // raw target (optional)
    private Double attainmentPercentage;   // 0..100
    private String status;                 // ACHIEVED / NEEDS_IMPROVEMENT
    private String academicYear;
    private String semester;
}
