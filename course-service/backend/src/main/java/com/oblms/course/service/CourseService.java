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

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourse(){
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id){
        return courseRepository.findById(id);
    }

    public Course updateCourse(Long id, Course course) {

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not Found"));

        existingCourse.setCoursecode(course.getCoursecode());
        existingCourse.setCoursename(course.getCoursename());
        existingCourse.setCredits(course.getCredits());
        existingCourse.setFacultyId(course.getFacultyId());

        return courseRepository.save(existingCourse);
    }
    public void deleteCourse(Long id){
        if(!courseRepository.existsById(id)){
            throw new RuntimeException("Course not Found");
        }
        courseRepository.deleteById(id);
    }
}
