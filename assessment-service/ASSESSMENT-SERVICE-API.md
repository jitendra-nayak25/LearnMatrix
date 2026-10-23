# Assessment Service API Documentation

**Service Name:** Assessment-Service
**Port:** 8083
**Database:** oblms_assessment
**Authentication:** All endpoints require a valid JWT in the `Authorization` header (`Bearer <token>`).

---

## 1. Assessment APIs

### Create an Assessment
*   **Endpoint:** `POST /assessments`
*   **Body:**
    ```json
    {
      "title": "Mid Semester Exam",
      "type": "EXAM",
      "maxMarks": 50,
      "courseId": 101,
      "description": "Covers chapters 1-5",
      "status": "DRAFT",
      "cos": ["CO1", "CO2"]
    }