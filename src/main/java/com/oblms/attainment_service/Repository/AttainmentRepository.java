package com.oblms.attainment_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oblms.attainment_service.Entity.Attainment;

@Repository
public interface AttainmentRepository extends JpaRepository<Attainment,Long> {
	List<Attainment> findByCourseId(Long courseId);

    List<Attainment> findByStudentId(Long studentId);

    List<Attainment> findByCoId(Long coId);
    List<Attainment> findByCourseIdAndStudentId(Long courseId, Long studentId);
    List<Attainment> findByCourseIdAndAcademicYearAndSemester(
        Long courseId, String academicYear, String semester);
    List<Attainment> findByCourseIdAndCoId(Long courseId, Long coId);
}
