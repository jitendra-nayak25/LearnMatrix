
package com.oblms.course.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "course_outcomes")
public class CourseOutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be positive")
    @Column(nullable = false)
    private Long courseId;

    @NotBlank(message = "Outcome code is required")
    @Size(max = 50, message = "Outcome code must not exceed 50 characters")
    @Column(nullable = false)
    private String outcomeCode;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Column(nullable = false)
    private String description;

    public CourseOutcome() {
    }

    public CourseOutcome(Long courseId, String outcomeCode,
                         String description) {
        this.courseId = courseId;
        this.outcomeCode = outcomeCode;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getOutcomeCode() {
        return outcomeCode;
    }

    public void setOutcomeCode(String outcomeCode) {
        this.outcomeCode = outcomeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
