
package com.oblms.course.controller;

import com.oblms.course.entity.Course;
import com.oblms.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public Course createCourse(@Valid @RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourse();
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found with ID: " + id
                        ));
    }

    @PutMapping("/{id}")
    public Course updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "Course Deleted Successfully";
    }

    // Day 6: Pagination and sorting
    @GetMapping("/page")
    public Page<Course> getCoursesPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        return courseService.getCourses(page, size, sortBy);
    }

    // Day 6: Search by course name
    @GetMapping("/search/name")
    public Page<Course> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return courseService.searchByCourseName(name, page, size);
    }

    // Day 6: Search by course code
    @GetMapping("/search/code")
    public Page<Course> searchByCode(
            @RequestParam String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return courseService.searchByCourseCode(code, page, size);
    }


    // Day 7: Retrieve courses by faculty ID
    @GetMapping("/faculty/{facultyId}")
    public List<Course> getCoursesByFacultyId(
            @PathVariable Long facultyId) {

        return courseService.getCoursesByFacultyId(facultyId);
    }

}
