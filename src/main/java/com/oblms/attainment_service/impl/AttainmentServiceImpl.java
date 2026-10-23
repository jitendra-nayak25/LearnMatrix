package com.oblms.attainment_service.impl;

import com.oblms.attainment_service.Dto.*;
import com.oblms.attainment_service.Entity.Attainment;
import com.oblms.attainment_service.Entity.CourseOutcome;
//import com.oblms.attainment_service.Entity.StudentMark;
import com.oblms.attainment_service.Entity.StudentMarks;
import com.oblms.attainment_service.Repository.AttainmentRepository;
import com.oblms.attainment_service.Repository.CourseOutcomeRepository;
import com.oblms.attainment_service.Repository.StudentMarkRepository;
import com.oblms.attainment_service.Service.AttainmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttainmentServiceImpl implements AttainmentService {

    private static final double DEFAULT_THRESHOLD = 60.0;

    private final AttainmentRepository attainmentRepo;
    private final CourseOutcomeRepository coRepo;
    private final StudentMarkRepository markRepo;

    

    @Override
    public Attainment createAttainment(AttainmentRequest request) {
        log.info("Creating attainment for courseId={}, coId={}, studentId={}",
                request.getCourseId(), request.getCoId(), request.getStudentId());

        Attainment attainment = Attainment.builder()
                .courseId(request.getCourseId())
                .coId(request.getCoId())
                .studentId(request.getStudentId())
                .attainmentValue(request.getAttainmentValue())
                .targetValue(request.getTargetValue())
                .attainmentLevel(request.getAttainmentLevel())
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .build();

        return attainmentRepo.save(attainment);
    }

    @Override
    public Attainment getAttainmentById(Long id) {
        return attainmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Attainment not found with id: " + id));
    }

    @Override
    public List<Attainment> getAllAttainments() {
        return attainmentRepo.findAll();
    }

   
    @Override
    public StudentReportDto getStudentReport(Long studentId) {

        List<Attainment> rows = attainmentRepo.findByStudentId(studentId);

        if (rows.isEmpty()) {
            return StudentReportDto.builder()
                    .studentId(studentId)
                    .totalCourses(0)
                    .overallAttainment(0.0)
                    .overallStatus("NEEDS_IMPROVEMENT")
                    .courses(new ArrayList<>())
                    .build();
        }

        
        Map<Long, List<Attainment>> byCourse = rows.stream()
                .collect(Collectors.groupingBy(
                        Attainment::getCourseId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<CourseReportDto> courseReports = new ArrayList<>();
        double totalCoursePct = 0.0;

        for (Map.Entry<Long, List<Attainment>> entry : byCourse.entrySet()) {
            Long courseId = entry.getKey();
            List<Attainment> courseRows = entry.getValue();

            
            Map<Long, CourseOutcome> coMeta = coRepo.findByCourseId(courseId).stream()
                    .collect(Collectors.toMap(CourseOutcome::getId, co -> co));

            List<CoAttainmentDto> coDtos = new ArrayList<>();
            double sumCoPct = 0.0;

            for (Attainment a : courseRows) {
                double pct = computePercentage(a.getAttainmentValue(), a.getTargetValue());

                CourseOutcome meta = coMeta.get(a.getCoId());

                coDtos.add(CoAttainmentDto.builder()
                        .coId(a.getCoId())
                        .coCode(meta != null ? meta.getCoCode() : "CO" + a.getCoId())
                        .description(meta != null ? meta.getDescription() : null)
                        .attainmentValue(a.getAttainmentValue())
                        .targetValue(a.getTargetValue())
                        .attainmentPercentage(pct)
                        .status(decideStatus(pct, meta))
                        .academicYear(a.getAcademicYear())
                        .semester(a.getSemester())
                        .build());

                sumCoPct += pct;
            }

            double coursePct = coDtos.isEmpty() ? 0.0 : round2(sumCoPct / coDtos.size());

            courseReports.add(CourseReportDto.builder()
                    .courseId(courseId)
                    .attainmentPercentage(coursePct)
                    .status(decideStatus(coursePct, null))
                    .cos(coDtos)
                    .build());

            totalCoursePct += coursePct;
        }

        double overall = courseReports.isEmpty() ? 0.0 : round2(totalCoursePct / courseReports.size());

        return StudentReportDto.builder()
                .studentId(studentId)
                .totalCourses(courseReports.size())
                .overallAttainment(overall)
                .overallStatus(decideStatus(overall, null))
                .courses(courseReports)
                .build();
    }

    
    @Override
    public CourseReportDto getCourseReport(Long courseId) {

        List<Attainment> rows = attainmentRepo.findByCourseId(courseId);

        Map<Long, CourseOutcome> coMeta = coRepo.findByCourseId(courseId).stream()
                .collect(Collectors.toMap(CourseOutcome::getId, co -> co));

        if (rows.isEmpty()) {
            return CourseReportDto.builder()
                    .courseId(courseId)
                    .attainmentPercentage(0.0)
                    .status("NEEDS_IMPROVEMENT")
                    .cos(new ArrayList<>())
                    .build();
        }

        
        Map<Long, List<Attainment>> byCo = rows.stream()
                .collect(Collectors.groupingBy(
                        Attainment::getCoId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<CoAttainmentDto> coDtos = new ArrayList<>();
        double sumCoPct = 0.0;

        for (Map.Entry<Long, List<Attainment>> entry : byCo.entrySet()) {
            Long coId = entry.getKey();
            List<Attainment> coRows = entry.getValue();

            double avgPct = coRows.stream()
                    .mapToDouble(a -> computePercentage(a.getAttainmentValue(), a.getTargetValue()))
                    .average()
                    .orElse(0.0);

            avgPct = round2(avgPct);

            CourseOutcome meta = coMeta.get(coId);

            coDtos.add(CoAttainmentDto.builder()
                    .coId(coId)
                    .coCode(meta != null ? meta.getCoCode() : "CO" + coId)
                    .description(meta != null ? meta.getDescription() : null)
                    .attainmentPercentage(avgPct)
                    .status(decideStatus(avgPct, meta))
                    .build());

            sumCoPct += avgPct;
        }

        double coursePct = coDtos.isEmpty() ? 0.0 : round2(sumCoPct / coDtos.size());

        return CourseReportDto.builder()
                .courseId(courseId)
                .attainmentPercentage(coursePct)
                .status(decideStatus(coursePct, null))
                .cos(coDtos)
                .build();
    }

    
    @Override
    public DashboardStatsDto getDashboardStats() {

        List<Attainment> all = attainmentRepo.findAll();

        long totalCourses = all.stream().map(Attainment::getCourseId).distinct().count();
        long totalStudents = all.stream().map(Attainment::getStudentId).distinct().count();
        long totalCOs = all.stream().map(Attainment::getCoId).distinct().count();

        double overall = all.stream()
                .mapToDouble(a -> computePercentage(a.getAttainmentValue(), a.getTargetValue()))
                .average()
                .orElse(0.0);
        overall = round2(overall);

        // CO performance
        Map<Long, List<Attainment>> byCo = all.stream()
                .collect(Collectors.groupingBy(Attainment::getCoId));

        List<DashboardStatsDto.CoPerformance> coPerf = new ArrayList<>();
        for (Map.Entry<Long, List<Attainment>> e : byCo.entrySet()) {
            double pct = e.getValue().stream()
                    .mapToDouble(a -> computePercentage(a.getAttainmentValue(), a.getTargetValue()))
                    .average()
                    .orElse(0.0);

            coPerf.add(DashboardStatsDto.CoPerformance.builder()
                    .co("CO" + e.getKey())
                    .percentage(round2(pct))
                    .build());
        }

        
        long achieved = all.stream()
                .filter(a -> computePercentage(a.getAttainmentValue(), a.getTargetValue()) >= DEFAULT_THRESHOLD)
                .count();
        long needs = all.size() - achieved;

        List<DashboardStatsDto.StatusBreakdown> statusList = List.of(
                DashboardStatsDto.StatusBreakdown.builder().status("ACHIEVED").count(achieved).build(),
                DashboardStatsDto.StatusBreakdown.builder().status("NEEDS_IMPROVEMENT").count(needs).build()
        );

        
        Map<String, List<Attainment>> bySem = all.stream()
                .filter(a -> a.getSemester() != null)
                .collect(Collectors.groupingBy(Attainment::getSemester));

        List<DashboardStatsDto.Trend> trends = new ArrayList<>();
        for (Map.Entry<String, List<Attainment>> e : bySem.entrySet()) {
            double pct = e.getValue().stream()
                    .mapToDouble(a -> computePercentage(a.getAttainmentValue(), a.getTargetValue()))
                    .average()
                    .orElse(0.0);

            trends.add(DashboardStatsDto.Trend.builder()
                    .semester(e.getKey())
                    .attainment(round2(pct))
                    .build());
        }

        return DashboardStatsDto.builder()
                .totalCourses(totalCourses)
                .totalStudents(totalStudents)
                .totalAssessments(totalCOs)
                .overallAttainment(overall)
                .coPerformance(coPerf)
                .statusBreakdown(statusList)
                .trend(trends)
                .build();
    }

    

    @Override
    public List<Attainment> calculateCourseAttainment(Long courseId) {

       
        List<StudentMarks> marks = markRepo.findByCourseId(courseId);
        if (marks.isEmpty()) {
            log.warn("No marks found for course {}", courseId);
            return new ArrayList<>();
        }

        
        Map<String, List<StudentMarks>> byStudentCo = marks.stream()
                .filter(m -> m.getCoId() != null)
                .collect(Collectors.groupingBy(m -> m.getStudentId() + "-" + m.getCoId()));

        
        Map<Long, List<Double>> coToStudentPct = new HashMap<>();

        for (Map.Entry<String, List<StudentMarks>> e : byStudentCo.entrySet()) {
            String[] parts = e.getKey().split("-");
            Long coId = Long.parseLong(parts[1]);

            double obtained = e.getValue().stream()
                    .mapToDouble(m -> m.getMarksObtained() == null ? 0.0 : m.getMarksObtained())
                    .sum();
            double max = e.getValue().stream()
                    .mapToDouble(m -> m.getMaxMarks() == null ? 0.0 : m.getMaxMarks())
                    .sum();

            double pct = max > 0 ? (obtained / max) * 100.0 : 0.0;

            coToStudentPct.computeIfAbsent(coId, k -> new ArrayList<>()).add(pct);
        }

        
        List<Attainment> result = new ArrayList<>();

        for (Map.Entry<Long, List<Double>> e : coToStudentPct.entrySet()) {
            Long coId = e.getKey();
            double avgPct = e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            avgPct = round2(avgPct);

            Attainment a = Attainment.builder()
                    .courseId(courseId)
                    .coId(coId)
                    .studentId(0L) // aggregate row
                    .attainmentValue(avgPct)
                    .targetValue(100.0)
                    .attainmentLevel(decideStatus(avgPct, null))
                    .build();

            result.add(attainmentRepo.save(a));
        }

        return result;
    }
    

    @Override
    public List<Attainment> getByStudentId(Long studentId) {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be positive");
        }
        return attainmentRepo.findByStudentId(studentId);
    }

    @Override
    public List<Attainment> getByCourseId(Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Course ID must be positive");
        }
        return attainmentRepo.findByCourseId(courseId);
    }

    @Override
    public List<Attainment> getByStudentAndCourse(Long studentId, Long courseId) {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be positive");
        }
        if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Course ID must be positive");
        }
        return attainmentRepo.findByCourseIdAndStudentId(courseId, studentId);
    }

    @Override
    public double calculateAttainment(Double marksObtained, Double maximumMarks) {
        if (marksObtained == null || maximumMarks == null) {
            throw new IllegalArgumentException("Marks cannot be null");
        }
        if (maximumMarks <= 0) {
            throw new IllegalArgumentException("Maximum marks must be greater than zero");
        }
        if (marksObtained < 0 || marksObtained > maximumMarks) {
            throw new IllegalArgumentException("Marks must be between zero and maximum marks");
        }
        return Math.round((marksObtained / maximumMarks) * 10000.0) / 100.0;
    }
    public List<Attainment> getByCourseAndCo(Long courseId,Long coId){
    	if (courseId == null || courseId <= 0) {
            throw new IllegalArgumentException("Course ID must be positive");
        }
        if (coId == null || coId <= 0) {
            throw new IllegalArgumentException("Course Outcome ID must be positive");
        }
        return attainmentRepo.findByCourseIdAndCoId(courseId, coId);
    }
    // ============================================================
    // HELPERS
    // ============================================================

    private double computePercentage(Double value, Double target) {
        if (value == null || target == null || target == 0) return 0.0;
        return round2((value / target) * 100.0);
    }

    private String decideStatus(double percentage, CourseOutcome co) {
        double threshold = (co != null && co.getThresholdPercentage() != null)
                ? co.getThresholdPercentage()
                : DEFAULT_THRESHOLD;
        return percentage >= threshold ? "ACHIEVED" : "NEEDS_IMPROVEMENT";
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}