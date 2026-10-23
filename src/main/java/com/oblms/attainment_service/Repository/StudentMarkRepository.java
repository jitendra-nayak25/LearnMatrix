package com.oblms.attainment_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oblms.attainment_service.Entity.StudentMarks;

@Repository
public interface StudentMarkRepository extends JpaRepository<StudentMarks,Long> {
	 List<StudentMarks> findByCourseId(Long courseId);
	    List<StudentMarks> findByCourseIdAndStudentId(Long courseId, Long studentId);
	    List<StudentMarks> findByStudentId(Long studentId);
}
