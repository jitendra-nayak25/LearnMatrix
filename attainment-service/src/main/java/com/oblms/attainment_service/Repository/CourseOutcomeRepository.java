package com.oblms.attainment_service.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oblms.attainment_service.Entity.CourseOutcome;

@Repository
public interface CourseOutcomeRepository extends JpaRepository<CourseOutcome,Long> {
	 List<CourseOutcome> findByCourseId(Long courseId);
	    Optional<CourseOutcome> findByCourseIdAndCoCode(Long courseId, String coCode);
}
