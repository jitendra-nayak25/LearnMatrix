package com.oblms.assessment.service;

import com.oblms.assessment.dto.AssessmentRequest;
import com.oblms.assessment.dto.AssessmentResponse;
import com.oblms.assessment.entity.Assessment;
import com.oblms.assessment.exception.ResourceNotFoundException;
import com.oblms.assessment.repository.AssessmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    // ---- Create ----
    public AssessmentResponse createAssessment(AssessmentRequest req) {
        Assessment a = new Assessment(
                req.getTitle(),
                req.getType(),
                req.getMaxMarks(),
                req.getCourseId(),
                req.getDescription(),
                req.getCos() != null ? String.join(",", req.getCos()) : null,
                req.getStatus() != null ? req.getStatus() : "DRAFT"
        );
        return toResponse(assessmentRepository.save(a));
    }

    // ---- Read all ----
    public List<AssessmentResponse> getAllAssessments() {
        return assessmentRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ---- Read one ----
    public AssessmentResponse getAssessmentById(Long id) {
        Assessment a = assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id " + id));
        return toResponse(a);
    }

    // ---- Update - FIXED ----
    public AssessmentResponse updateAssessment(Long id, AssessmentRequest req) {
        Assessment a = assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id " + id));
        a.setTitle(req.getTitle());
        a.setType(req.getType());
        a.setMaxMarks(req.getMaxMarks());
        a.setCourseId(req.getCourseId());
        a.setDescription(req.getDescription());
        a.setCos(req.getCos() != null ? String.join(",", req.getCos()) : null);
        a.setStatus(req.getStatus());
        return toResponse(assessmentRepository.save(a));
    }

    // ---- Delete ----
    public void deleteAssessment(Long id) {
        if (!assessmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assessment not found with id " + id);
        }
        assessmentRepository.deleteById(id);
    }

    // ---- Paged ----
    public Page<AssessmentResponse> getAssessmentsPaged(PageRequest pageRequest) {
        return assessmentRepository.findAll(pageRequest).map(this::toResponse);
    }

    // ---- By course ----
    public List<AssessmentResponse> getByCourseId(Long courseId) {
        return assessmentRepository.findByCourseId(courseId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ---- By type ----
    public Page<AssessmentResponse> getByType(String type, PageRequest pageRequest) {
        return assessmentRepository.findByType(type, pageRequest).map(this::toResponse);
    }

    // ---- Mapper - FIXED ----
    private AssessmentResponse toResponse(Assessment a) {
        List<String> cosList = (a.getCos() != null && !a.getCos().isBlank())
                ? Arrays.asList(a.getCos().split(","))
                : List.of();
        return new AssessmentResponse(
                a.getId(),
                a.getTitle(),
                a.getType(),
                a.getMaxMarks(),
                a.getCourseId(),
                a.getDescription(),
                cosList,
                a.getStatus()
        );
    }
}