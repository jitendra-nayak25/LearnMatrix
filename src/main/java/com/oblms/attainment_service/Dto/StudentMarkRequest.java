package com.oblms.attainment_service.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentMarkRequest {
	@NotNull
    private Long courseId;

    @NotNull
    private Long studentId;

    private Long coId;
    private Double marksObtained;
    private Double maxMarks;
    private String academicYear;
    private String semester;
}
