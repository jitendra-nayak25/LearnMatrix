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
@Table(name = "student_marks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentMarks {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "course_id", nullable = false)
	    private Long courseId;

	    @Column(name = "student_id", nullable = false)
	    private Long studentId;

	    @Column(name = "co_id")
	    private Long coId;                     // which CO this mark contributes to

	    @Column(name = "marks_obtained")
	    private Double marksObtained;

	    @Column(name = "max_marks")
	    private Double maxMarks;

	    @Column(name = "academic_year", length = 20)
	    private String academicYear;

	    @Column(name = "semester", length = 20)
	    private String semester;

	    @Column(name = "created_at")
	    private LocalDateTime createdAt;

	    @PrePersist
	    public void onCreate() {
	        this.createdAt = LocalDateTime.now();
	    }
}
