package com.oblms.attainment_service.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_outcomes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseOutcome {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "co_code", nullable = false, length = 20)
    private String coCode;                 // "CO1", "CO2"

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "target_percentage")
    private Double targetPercentage;       // 60.0

    @Column(name = "threshold_percentage")
    private Double thresholdPercentage;    // 60.0 — >= means ACHIEVED

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
