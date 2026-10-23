package com.oblms.assessment.dto;

import java.util.List;

public class AssessmentResponse {

    private Long id;
    private String title;
    private String type;
    private Integer maxMarks;
    private Long courseId;
    private String description;
    private List<String> cos;
    private String status;



    public AssessmentResponse(Long id, String title, String type, Integer maxMarks, Long courseId ,String description, List<String> cos, String status) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.maxMarks = maxMarks;
        this.courseId = courseId;
        this.description = description;
        this.cos = cos;
        this.status = status;
    }

    // getters + setters (same pattern as above)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Integer maxMarks) { this.maxMarks = maxMarks; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCos() {
        return cos;
    }

    public void setCos(List<String> cos) {
        this.cos = cos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}