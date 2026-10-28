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
public class DashboardStatsDto {
	private long totalCourses;
    private long totalStudents;
    private long totalAssessments;      // put totalCoCount or similar for now
    private double overallAttainment;

    private List<CoPerformance> coPerformance;
    private List<StatusBreakdown> statusBreakdown;
    private List<Trend> trend;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CoPerformance {
        private String co;
        private double percentage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusBreakdown {
        private String status;         // ACHIEVED / NEEDS_IMPROVEMENT
        private long count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Trend {
        private String semester;
        private double attainment;
    }
}
