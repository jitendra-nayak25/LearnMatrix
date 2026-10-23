package com.oblms.assessment.controller;

import com.oblms.assessment.entity.AssessmentResult;
import com.oblms.assessment.service.AssessmentResultService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assessment-results")
public class AssessmentResultController {

    private final AssessmentResultService resultService;

    public AssessmentResultController(AssessmentResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping
    public AssessmentResult createResult(@Valid @RequestBody AssessmentResult result) {
        return resultService.createResult(result);
    }

    @GetMapping
    public List<AssessmentResult> getAllResults() {
        return resultService.getAllResults();
    }

    // ADDED: Get by ID
    @GetMapping("/{id}")
    public AssessmentResult getResultById(@PathVariable Long id) {
        return resultService.getResultById(id)
                .orElseThrow(() -> new RuntimeException("Assessment result not found with id: " + id));
    }

    @GetMapping("/student/{studentId}")
    public List<AssessmentResult> getResultsByStudent(@PathVariable Long studentId) {
        return resultService.getResultsByStudent(studentId);
    }

    @GetMapping("/assessment/{assessmentId}")
    public List<AssessmentResult> getResultsByAssessment(@PathVariable Long assessmentId) {
        return resultService.getResultsByAssessment(assessmentId);
    }

    // ADDED: Update Result
    @PutMapping("/{id}")
    public AssessmentResult updateResult(@PathVariable Long id, @Valid @RequestBody AssessmentResult result) {
        return resultService.updateResult(id, result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}