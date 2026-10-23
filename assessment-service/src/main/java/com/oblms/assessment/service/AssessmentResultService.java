package com.oblms.assessment.service;

import com.oblms.assessment.entity.Assessment;
import com.oblms.assessment.entity.AssessmentResult;
import com.oblms.assessment.exception.ResourceNotFoundException; // <-- Make sure to import this
import com.oblms.assessment.repository.AssessmentRepository;
import com.oblms.assessment.repository.AssessmentResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AssessmentResultService {

    private final AssessmentResultRepository resultRepository;
    private final AssessmentRepository assessmentRepository;

    public AssessmentResultService(
            AssessmentResultRepository resultRepository,
            AssessmentRepository assessmentRepository) {

        this.resultRepository = resultRepository;
        this.assessmentRepository = assessmentRepository;
    }

    public AssessmentResult createResult(AssessmentResult result) {
        validateResult(result);
        return resultRepository.save(result);
    }

    public List<AssessmentResult> getAllResults() {
        return resultRepository.findAll();
    }

    public Optional<AssessmentResult> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    public List<AssessmentResult> getResultsByStudent(Long studentId) {
        return resultRepository.findByStudentId(studentId);
    }

    public List<AssessmentResult> getResultsByAssessment(Long assessmentId) {
        return resultRepository.findByAssessmentId(assessmentId);
    }

    @Transactional
    public AssessmentResult updateResult(Long id, AssessmentResult result) {
        AssessmentResult existingResult = resultRepository.findById(id)
                // FIXED: Changed to ResourceNotFoundException
                .orElseThrow(() -> new ResourceNotFoundException("Assessment result not found with id " + id));

        validateResult(result);

        existingResult.setStudentId(result.getStudentId());
        existingResult.setAssessmentId(result.getAssessmentId());
        existingResult.setMarksObtained(result.getMarksObtained());

        return resultRepository.save(existingResult);
    }

    @Transactional
    public void deleteResult(Long id) {
        if (!resultRepository.existsById(id)) {
            // FIXED: Changed to ResourceNotFoundException
            throw new ResourceNotFoundException("Assessment result not found with id " + id);
        }
        resultRepository.deleteById(id);
    }

    // --- BUSINESS VALIDATION LOGIC ---
    private void validateResult(AssessmentResult result) {
        if (result.getStudentId() == null || result.getStudentId() <= 0) {
            throw new IllegalArgumentException("A valid student ID is required");
        }

        if (result.getAssessmentId() == null || result.getAssessmentId() <= 0) {
            throw new IllegalArgumentException("A valid assessment ID is required");
        }

        Double marks = result.getMarksObtained();
        if (marks == null || !Double.isFinite(marks) || marks < 0) {
            throw new IllegalArgumentException("Marks must be a valid non-negative number");
        }

        // Look up the assessment to check its max marks
        Assessment assessment = assessmentRepository.findById(result.getAssessmentId())
                .orElseThrow(() -> new IllegalArgumentException("Referenced assessment does not exist"));

        if (marks > assessment.getMaxMarks()) {
            throw new IllegalArgumentException("Marks cannot exceed maximum marks: " + assessment.getMaxMarks());
        }
    }
}