
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

    // Create a course outcome
    public CourseOutcome createCourseOutcome(
            CourseOutcome courseOutcome) {

        validateCourseExists(courseOutcome.getCourseId());

        validateDuplicateOutcomeCode(
                courseOutcome.getCourseId(),
                courseOutcome.getOutcomeCode()
        );

        return courseOutcomeRepository.save(courseOutcome);
    }

    // Get all course outcomes
    public List<CourseOutcome> getAllCourseOutcomes() {
        return courseOutcomeRepository.findAll();
    }

    // Get a course outcome by ID
    public Optional<CourseOutcome> getCourseOutcomeById(Long id) {
        return courseOutcomeRepository.findById(id);
    }

    // Get outcomes belonging to a particular course
    public List<CourseOutcome> getCourseOutcomesByCourse(Long courseId) {
        return courseOutcomeRepository.findByCourseId(courseId);
    }

    // Update a course outcome
    public CourseOutcome updateCourseOutcome(
            Long id, CourseOutcome courseOutcome) {

        CourseOutcome existingOutcome =
                courseOutcomeRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Course Outcome not found with ID: " + id
                                ));

        validateCourseExists(courseOutcome.getCourseId());

        // Check for duplicate outcome code, excluding the current outcome.
        boolean duplicateExists =
                courseOutcomeRepository.existsByCourseIdAndOutcomeCode(
                        courseOutcome.getCourseId(),
                        courseOutcome.getOutcomeCode()
                );

        if (duplicateExists
                && (!existingOutcome.getCourseId().equals(
                courseOutcome.getCourseId())
                || !existingOutcome.getOutcomeCode().equals(
                courseOutcome.getOutcomeCode()))) {

            throw new IllegalArgumentException(
                    "Outcome code already exists for this course: "
                            + courseOutcome.getOutcomeCode()
            );
        }

        existingOutcome.setCourseId(courseOutcome.getCourseId());
        existingOutcome.setOutcomeCode(courseOutcome.getOutcomeCode());
        existingOutcome.setDescription(courseOutcome.getDescription());

        return courseOutcomeRepository.save(existingOutcome);
    }

    // Delete a course outcome
    public void deleteCourseOutcome(Long id) {

        if (!courseOutcomeRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Course Outcome not found with ID: " + id
            );
        }

        courseOutcomeRepository.deleteById(id);
    }

    // Validate that the associated course exists
    private void validateCourseExists(Long courseId) {

        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException(
                    "Course does not exist: " + courseId
            );
        }
    }

    // Prevent duplicate outcome codes within the same course
    private void validateDuplicateOutcomeCode(
            Long courseId, String outcomeCode) {

        if (courseOutcomeRepository.existsByCourseIdAndOutcomeCode(
                courseId, outcomeCode)) {

            throw new IllegalArgumentException(
                    "Outcome code already exists for this course: "
                            + outcomeCode
            );
        }
    }
}
