package com.oblms.attainment_service.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attainments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attainment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotNull(message = "Course ID is required")
	@Positive(message = "Course ID must be positive")
    @Column(name = "course_id", nullable = false)
    private Long courseId;

	@NotNull(message = "Course Outcome ID is required")
	@Positive(message = "Course Outcome ID must be positive")
    @Column(name = "co_id", nullable = false)
    private Long coId;

	@NotNull(message = "Student ID is required")
	@Positive(message = "Student ID must be positive")
    @Column(name = "student_id", nullable = false)
    private Long studentId;

	@NotNull(message = "Attainment value is required")
	@DecimalMin(value = "0.0", message = "Attainment cannot be below 0")
	@DecimalMax(value = "100.0", message = "Attainment cannot exceed 100")
    @Column(name = "attainment_value")
    private Double attainmentValue;

    @Column(name = "target_value")
    private Double targetValue;

    @Column(name = "attainment_level", length = 50)
    private String attainmentLevel;

    @Column(name = "academic_year", length = 20)
    private String academicYear;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
