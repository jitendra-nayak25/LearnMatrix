
package com.oblms.course.service;

import com.oblms.course.entity.Course;
import com.oblms.course.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Create a course and prevent duplicate course codes
    public Course createCourse(Course course) {

        if (courseRepository.existsByCoursecode(course.getCoursecode())) {
            throw new IllegalArgumentException(
                    "Course code already exists: " + course.getCoursecode()
            );
        }

        return courseRepository.save(course);
    }

    // Get all courses
    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    // Get a course by ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // Update a course and prevent duplicate course codes
    public Course updateCourse(Long id, Course course) {

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Course not found with ID: " + id
                ));

        String newCourseCode = course.getCoursecode();

        // Allow the existing course to keep its own code,
        // but reject a code belonging to another course.
        if (!existingCourse.getCoursecode().equals(newCourseCode)
                && courseRepository.existsByCoursecode(newCourseCode)) {

            throw new IllegalArgumentException(
                    "Course code already exists: " + newCourseCode
            );
        }

        existingCourse.setCoursecode(newCourseCode);
        existingCourse.setCoursename(course.getCoursename());
        existingCourse.setCredits(course.getCredits());
        existingCourse.setFacultyId(course.getFacultyId());

        return courseRepository.save(existingCourse);
    }

    // Delete a course
    public void deleteCourse(Long id) {

        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Course not found with ID: " + id
            );
        }

        courseRepository.deleteById(id);
    }
}
