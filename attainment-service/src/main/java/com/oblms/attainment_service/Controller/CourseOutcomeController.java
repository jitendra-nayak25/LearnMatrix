package com.oblms.attainment_service.Controller;

import com.oblms.attainment_service.Dto.CourseOutcomeRequest;
import com.oblms.attainment_service.Entity.CourseOutcome;
import com.oblms.attainment_service.Repository.CourseOutcomeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-outcomes")
@RequiredArgsConstructor
public class CourseOutcomeController {

    private final CourseOutcomeRepository coRepo;

    @PostMapping
    public ResponseEntity<CourseOutcome> create(@Valid @RequestBody CourseOutcomeRequest req) {
        CourseOutcome co = CourseOutcome.builder()
                .courseId(req.getCourseId())
                .coCode(req.getCoCode())
                .description(req.getDescription())
                .targetPercentage(req.getTargetPercentage())
                .thresholdPercentage(req.getThresholdPercentage())
                .build();
        return new ResponseEntity<>(coRepo.save(co), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseOutcome>> getAll() {
        return ResponseEntity.ok(coRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseOutcome> getById(@PathVariable Long id) {
        return ResponseEntity.ok(coRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("CO not found: " + id)));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseOutcome>> getByCourse(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(coRepo.findByCourseId(courseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        coRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}