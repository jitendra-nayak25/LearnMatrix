package com.oblms.assessment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer maxMarks;

    @Column(nullable = false)
    private Long courseId;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String cos;   // comma-separated, e.g. "CO1,CO2,CO3"

    @Column(length = 20)
    private String status = "DRAFT";

    public Assessment() {
    }

    public Assessment(String title, String type, Integer maxMarks, Long courseId, String description, String cos, String status) {
        this.title = title;
        this.type = type;
        this.maxMarks = maxMarks;
        this.courseId = courseId;
        this.description = description;
        this.cos = cos;
        this.status = status;
    }

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

    public String getCos() {
        return cos;
    }

    public void setCos(String cos) {
        this.cos = cos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}