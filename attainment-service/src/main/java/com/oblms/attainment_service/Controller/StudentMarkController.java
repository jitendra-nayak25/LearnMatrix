package com.oblms.attainment_service.Controller;

import com.oblms.attainment_service.Dto.StudentMarkRequest;
//import com.oblms.attainment_service.Entity.StudentMark;
import com.oblms.attainment_service.Entity.StudentMarks;
import com.oblms.attainment_service.Repository.StudentMarkRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/student-marks")
@RequiredArgsConstructor
public class StudentMarkController {

    private final StudentMarkRepository repo;

    @PostMapping
    public ResponseEntity<StudentMarks> create(@Valid @RequestBody StudentMarkRequest req) {
        return new ResponseEntity<>(repo.save(toEntity(req)), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<StudentMarks>> bulk(@Valid @RequestBody List<StudentMarkRequest> reqs) {
        List<StudentMarks> entities = new ArrayList<>();
        for (StudentMarkRequest r : reqs) entities.add(toEntity(r));
        return new ResponseEntity<>(repo.saveAll(entities), HttpStatus.CREATED);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentMarks>> byCourse(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(repo.findByCourseId(courseId));
    }

    @GetMapping("/course/{courseId}/student/{studentId}")
    public ResponseEntity<List<StudentMarks>> byCourseAndStudent(
            @PathVariable("courseId") Long courseId, @PathVariable("studentId") Long studentId) {
        return ResponseEntity.ok(repo.findByCourseIdAndStudentId(courseId, studentId));
    }

    private StudentMarks toEntity(StudentMarkRequest r) {
        return StudentMarks.builder()
                .courseId(r.getCourseId())
                .studentId(r.getStudentId())
                .coId(r.getCoId())
                .marksObtained(r.getMarksObtained())
                .maxMarks(r.getMaxMarks())
                .academicYear(r.getAcademicYear())
                .semester(r.getSemester())
                .build();
    }
}