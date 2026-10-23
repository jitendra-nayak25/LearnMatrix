package com.oblms.assessment.controller;

import com.oblms.assessment.dto.AssessmentRequest;
import com.oblms.assessment.dto.AssessmentResponse;
import com.oblms.assessment.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    // Create Assessment
    @PostMapping
    public AssessmentResponse create(@RequestBody @Valid AssessmentRequest req) {
        return assessmentService.createAssessment(req);
    }

    // Get all Assessments
    @GetMapping
    public List<AssessmentResponse> getAll() {
        return assessmentService.getAllAssessments();
    }

    // Get Assessment by ID
    @GetMapping("/{id}")
    public AssessmentResponse getById(@PathVariable Long id) {
        return assessmentService.getAssessmentById(id);
    }

    // Update Assessment
    @PutMapping("/{id}")
    public AssessmentResponse update(@PathVariable Long id,
                                     @RequestBody @Valid AssessmentRequest req) {
        return assessmentService.updateAssessment(id, req);
    }

    // Delete Assessment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assessmentService.deleteAssessment(id);
        return ResponseEntity.noContent().build();
    }

    // Get Assessments with Pagination and Sorting
    @GetMapping("/paged")
    public Page<AssessmentResponse> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {

        return assessmentService.getAssessmentsPaged(
                PageRequest.of(page, size, Sort.by(sort))
        );
    }

    // Get Assessments by Course ID
    @GetMapping("/course/{courseId}")
    public List<AssessmentResponse> byCourse(@PathVariable Long courseId) {
        return assessmentService.getByCourseId(courseId);
    }

    // Get Assessments by Type with Pagination
    @GetMapping("/type/{type}")
    public Page<AssessmentResponse> byType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return assessmentService.getByType(
                type,
                PageRequest.of(page, size)
        );
    }
}