
package com.oblms.course.service;

import com.oblms.course.entity.Course;
import com.oblms.course.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        if (courseRepository.existsByCoursecode(
                course.getCoursecode())) {

            throw new IllegalArgumentException(
                    "Course code already exists: "
                            + course.getCoursecode()
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

        // Allow the existing course to keep its own code
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

    // Day 6: Get courses with pagination and sorting
    public Page<Course> getCourses(
            int page, int size, String sortBy) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100"
            );
        }

        if (!"id".equals(sortBy)
                && !"coursename".equals(sortBy)
                && !"coursecode".equals(sortBy)
                && !"credits".equals(sortBy)) {

            throw new IllegalArgumentException(
                    "Unsupported sort field: " + sortBy
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).ascending()
        );

        return courseRepository.findAll(pageable);
    }

    // Day 6: Search courses by name
    public Page<Course> searchByCourseName(
            String name, int page, int size) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Course name cannot be empty"
            );
        }

        validatePageAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return courseRepository
                .findByCoursenameContainingIgnoreCase(
                        name, pageable
                );
    }

    // Day 6: Search courses by course code
    public Page<Course> searchByCourseCode(
            String code, int page, int size) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Course code cannot be empty"
            );
        }

        validatePageAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size);

        return courseRepository
                .findByCoursecodeContainingIgnoreCase(
                        code, pageable
                );
    }

    // Validate pagination parameters
    private void validatePageAndSize(int page, int size) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Invalid page or page size"
            );
        }
    }

    // Day 7: Get courses assigned to a faculty member
    public List<Course> getCoursesByFacultyId(Long facultyId) {

        if (facultyId == null || facultyId <= 0) {
            throw new IllegalArgumentException(
                    "Faculty ID must be a positive number"
            );
        }

        return courseRepository.findByFacultyId(facultyId);
    }
}
