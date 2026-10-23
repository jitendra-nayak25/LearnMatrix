package com.oblms.assessment.repository;

import com.oblms.assessment.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    List<AssessmentResult> findByStudentId(Long studentId);
    List<AssessmentResult> findByAssessmentId(Long assessmentId);
}