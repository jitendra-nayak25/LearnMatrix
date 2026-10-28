package com.oblms.attainment_service.Service;

import java.util.List;

import com.oblms.attainment_service.Dto.AttainmentRequest;
import com.oblms.attainment_service.Dto.CourseReportDto;
import com.oblms.attainment_service.Dto.DashboardStatsDto;
import com.oblms.attainment_service.Dto.StudentReportDto;
import com.oblms.attainment_service.Entity.Attainment;

public interface AttainmentService {
	Attainment createAttainment(AttainmentRequest request);

    Attainment getAttainmentById(Long id);

    List<Attainment> getAllAttainments();
     StudentReportDto getStudentReport(Long studentId);

     CourseReportDto getCourseReport(Long courseId);

     DashboardStatsDto getDashboardStats();

     List<Attainment> calculateCourseAttainment(Long courseId);
     
     
     List<Attainment> getByStudentId(Long studentId);
     List<Attainment> getByCourseId(Long courseId);
     List<Attainment> getByStudentAndCourse(Long studentId, Long courseId);
     double calculateAttainment(Double marksObtained, Double maximumMarks);
     List<Attainment> getByCourseAndCo(Long courseId, Long coId);
}
