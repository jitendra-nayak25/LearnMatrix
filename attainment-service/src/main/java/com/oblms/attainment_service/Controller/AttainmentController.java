package com.oblms.attainment_service.Controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oblms.attainment_service.Dto.AssessmentResultDto;
import com.oblms.attainment_service.Dto.AttainmentCalculationDto;
import com.oblms.attainment_service.Dto.AttainmentReportDto;
import com.oblms.attainment_service.Dto.AttainmentRequest;
import com.oblms.attainment_service.Dto.CourseReportDto;
import com.oblms.attainment_service.Dto.DashboardStatsDto;
import com.oblms.attainment_service.Dto.StudentReportDto;
import com.oblms.attainment_service.Entity.Attainment;
import com.oblms.attainment_service.Service.AssessmentAttainmentService;
import com.oblms.attainment_service.Service.AssessmentClientService;
import com.oblms.attainment_service.Service.AttainmentReportService;
import com.oblms.attainment_service.Service.AttainmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/attainments")
@RequiredArgsConstructor
public class AttainmentController {

    private final AttainmentService attainmentServe;
    private final AssessmentClientService assessmentClientService;
    private final AssessmentAttainmentService assessmentAttainmentService;
    private final AttainmentReportService attainmentReportService;
    

    @PostMapping
    public ResponseEntity<Attainment> createAttainment(
            @Valid @RequestBody AttainmentRequest request) {
        Attainment created = attainmentServe.createAttainment(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attainment> getAttainmentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(attainmentServe.getAttainmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Attainment>> getAllAttainments() {
        return ResponseEntity.ok(attainmentServe.getAllAttainments());
    }

    

    @GetMapping("/student/{studentId}/report")
    public ResponseEntity<StudentReportDto> getStudentReport(
            @PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(attainmentServe.getStudentReport(studentId));
    }

    @GetMapping("/course/{courseId}/report")
    public ResponseEntity<CourseReportDto> getCourseReport(
            @PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(attainmentServe.getCourseReport(courseId));
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(attainmentServe.getDashboardStats());
    }

    

    @PostMapping("/calculate/course/{courseId}")
    public ResponseEntity<List<Attainment>> calculateCourse(
            @PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(attainmentServe.calculateCourseAttainment(courseId));
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attainment>> getByStudent(
            @PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(attainmentServe.getByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Attainment>> getByCourse(
            @PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(attainmentServe.getByCourseId(courseId));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<Attainment>> getByStudentAndCourse(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(attainmentServe.getByStudentAndCourse(studentId, courseId));
    }

    @GetMapping("/calculate")
    public ResponseEntity<Double> calculateAttainment(
            @RequestParam("marksObtained") Double marksObtained,
            @RequestParam("maximumMarks") Double maximumMarks) {
        return ResponseEntity.ok(
                attainmentServe.calculateAttainment(marksObtained, maximumMarks)
        );
    }
    
    @GetMapping("/course/{courseId}/co/{coId}")
    public ResponseEntity<List<Attainment>> getByCourseAndCo(
            @PathVariable("courseId") Long courseId,
            @PathVariable("coId") Long coId) {
        return ResponseEntity.ok(attainmentServe.getByCourseAndCo(courseId, coId));
    }
    @GetMapping("/assessment-results/student/{studentId}")
    public ResponseEntity<List<AssessmentResultDto>> getAssessmentResults(
            @PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(assessmentClientService.getResultsByStudent(studentId));
    }
    @GetMapping("/calculate/student/{studentId}/assessment/{assessmentId}")
    public ResponseEntity<AttainmentCalculationDto> calculateAttainment(
            @PathVariable("studentId") Long studentId,
            @PathVariable("assessmentId") Long assessmentId) {
        return ResponseEntity.ok(assessmentAttainmentService.calculate(studentId, assessmentId));
    }
    @GetMapping("/reports/course/{courseId}")
    public ResponseEntity<AttainmentReportDto> getCourseSummaryReport(
            @PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(attainmentReportService.getCourseReport(courseId));
    }
}