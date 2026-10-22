
package com.oblms.course.repository;

import com.oblms.course.entity.CourseOutcome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseOutcomeRepository extends JpaRepository<CourseOutcome, Long> {

    List<CourseOutcome> findByCourseId(Long courseId);

    boolean existsByCourseIdAndOutcomeCode(Long courseId, String outcomeCode);
}
