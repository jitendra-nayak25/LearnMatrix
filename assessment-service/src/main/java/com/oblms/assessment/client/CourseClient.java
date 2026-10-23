package com.oblms.assessment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "${course.service.url}")
public interface CourseClient {
    @GetMapping("/courses/{id}")
    Object getCourse(@PathVariable Long id);
}