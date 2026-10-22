
package com.oblms.course.repository;

import com.oblms.course.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Day 4: Check duplicate course codes
    boolean existsByCoursecode(String coursecode);

    // Day 6: Search by course name
    Page<Course> findByCoursenameContainingIgnoreCase(
            String coursename, Pageable pageable);

    // Day 6: Search by course code
    Page<Course> findByCoursecodeContainingIgnoreCase(
            String coursecode, Pageable pageable);

    // Day 7: Find courses assigned to a faculty member
    List<Course> findByFacultyId(Long facultyId);
}
