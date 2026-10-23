package com.oblms.attainment_service.Service;

import com.oblms.attainment_service.Dto.AssessmentDto;
import com.oblms.attainment_service.Dto.AssessmentResultDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class AssessmentClientService {

    private final RestClient assessmentRestClient;

    public AssessmentClientService(RestClient assessmentRestClient) {
        this.assessmentRestClient = assessmentRestClient;
    }

    public List<AssessmentResultDto> getResultsByStudent(Long studentId) {
        AssessmentResultDto[] results =
                assessmentRestClient.get()
                        .uri("/assessment-results/student/{studentId}", studentId)
                        .retrieve()
                        .body(AssessmentResultDto[].class);

        return results == null ? List.of() : Arrays.asList(results);
    }

    public AssessmentDto getAssessment(Long assessmentId) {
        return assessmentRestClient.get()
                .uri("/assessments/{id}", assessmentId)
                .retrieve()
                .body(AssessmentDto.class);
    }
}
