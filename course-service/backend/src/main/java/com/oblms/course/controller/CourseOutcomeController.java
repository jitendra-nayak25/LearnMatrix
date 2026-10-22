
package com.oblms.course.controller;

import com.oblms.course.entity.CourseOutcome;
import com.oblms.course.service.CourseOutcomeService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/course-outcomes")
public class CourseOutcomeController {

    private final CourseOutcomeService courseOutcomeService;

    public CourseOutcomeController(
            CourseOutcomeService courseOutcomeService) {
        this.courseOutcomeService = courseOutcomeService;
    }

    @PostMapping
    public CourseOutcome createCourseOutcome(
           @Valid @RequestBody CourseOutcome courseOutcome) {
        return courseOutcomeService.createCourseOutcome(courseOutcome);
    }

    @GetMapping
    public List<CourseOutcome> getAllCourseOutcomes() {
        return courseOutcomeService.getAllCourseOutcomes();
    }

    @GetMapping("/{id}")
    public CourseOutcome getCourseOutcomeById(
            @PathVariable Long id) {
        return courseOutcomeService.getCourseOutcomeById(id)
                .orElseThrow(() ->
                        new RuntimeException("Course Outcome not found"));
    }

    @GetMapping("/course/{courseId}")
    public List<CourseOutcome> getCourseOutcomesByCourse(
            @PathVariable Long courseId) {
        return courseOutcomeService.getCourseOutcomesByCourse(courseId);
    }

    @PutMapping("/{id}")
    public CourseOutcome updateCourseOutcome(
            @PathVariable Long id,
            @Valid @RequestBody CourseOutcome courseOutcome) {
        return courseOutcomeService.updateCourseOutcome(id, courseOutcome);
    }

    @DeleteMapping("/{id}")
    public String deleteCourseOutcome(@PathVariable Long id) {
        courseOutcomeService.deleteCourseOutcome(id);
        return "Course Outcome deleted successfully";
    }
}
