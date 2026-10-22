
package com.oblms.course.service;

import com.oblms.course.entity.CourseOutcome;
import com.oblms.course.repository.CourseOutcomeRepository;
import com.oblms.course.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseOutcomeService {

    private final CourseOutcomeRepository courseOutcomeRepository;
    private final CourseRepository courseRepository;

    public CourseOutcomeService(
            CourseOutcomeRepository courseOutcomeRepository,
            CourseRepository courseRepository) {

        this.courseOutcomeRepository = courseOutcomeRepository;
        this.courseRepository = courseRepository;
    }

    public CourseOutcome createCourseOutcome(
            CourseOutcome courseOutcome) {

        validateCourseExists(courseOutcome.getCourseId());

        return courseOutcomeRepository.save(courseOutcome);
    }

    public List<CourseOutcome> getAllCourseOutcomes() {
        return courseOutcomeRepository.findAll();
    }

    public Optional<CourseOutcome> getCourseOutcomeById(Long id) {
        return courseOutcomeRepository.findById(id);
    }

    public List<CourseOutcome> getCourseOutcomesByCourse(Long courseId) {
        return courseOutcomeRepository.findByCourseId(courseId);
    }

    public CourseOutcome updateCourseOutcome(
            Long id, CourseOutcome courseOutcome) {

        CourseOutcome existingOutcome =
                courseOutcomeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Course Outcome not found"));

        validateCourseExists(courseOutcome.getCourseId());

        existingOutcome.setCourseId(courseOutcome.getCourseId());
        existingOutcome.setOutcomeCode(courseOutcome.getOutcomeCode());
        existingOutcome.setDescription(courseOutcome.getDescription());

        return courseOutcomeRepository.save(existingOutcome);
    }

    public void deleteCourseOutcome(Long id) {
        if (!courseOutcomeRepository.existsById(id)) {
            throw new RuntimeException("Course Outcome not found");
        }

        courseOutcomeRepository.deleteById(id);
    }

    private void validateCourseExists(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException(
                    "Course does not exist: " + courseId);
        }
    }
}
