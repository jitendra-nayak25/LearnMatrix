
# Course-Service API Documentation

## Base URL
http://localhost:8082

## 1. Course APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | /courses | Create a course |
| GET | /courses | Get all courses |
| GET | /courses/{id} | Get a course by ID |
| PUT | /courses/{id} | Update a course |
| DELETE | /courses/{id} | Delete a course |
| GET | /courses/faculty/{facultyId} | Get courses by faculty ID |
| GET | /courses/page?page=0&size=5 | Get paginated courses |
| GET | /courses/search/name?name=Java | Search by course name |
| GET | /courses/search/code?code=CUCS | Search by course code |

## 2. Course Outcome APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | /course-outcomes | Create a course outcome |
| GET | /course-outcomes | Get all course outcomes |
| GET | /course-outcomes/{id} | Get an outcome by ID |
| PUT | /course-outcomes/{id} | Update a course outcome |
| DELETE | /course-outcomes/{id} | Delete a course outcome |
| GET | /course-outcomes/course/{courseId} | Get outcomes by course ID |

## 3. Validation

- Duplicate course codes are rejected.
- Faculty IDs must be positive numbers.
- Course Outcome records must reference an existing course.
- Duplicate Course Outcome codes within the same course are rejected.
- Invalid course IDs and invalid request parameters return appropriate errors.

## 4. Technology Stack

- Java 25
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL 8
- REST APIs
- Jakarta Bean Validation

## 5. Example Requests

### Create a course
POST /courses

### Get courses assigned to faculty 1
GET /courses/faculty/1

### Get outcomes for course 1
GET /course-outcomes/course/1

## 6. Integration Contract

### Course-Service

- Base URL: http://localhost:8082
- Course resource: /courses
- Course Outcome resource: /course-outcomes

### Course Data Contract

| Field | Type | Verified Example |
|---|---|---|
| id | Number | 1 |
| coursecode | String | CUTM101 |
| coursename | String | Advanced Java |
| credits | Number | 4 |
| facultyId | Number | 2 |

### Course Outcome Data Contract

| Field | Type | Verified Example |
|---|---|---|
| id | Number | 4 |
| courseId | Number | 1 |
| outcomeCode | String | CO402 |
| description | String | Apply software engineering principles |

### Integration Notes

- Assessment-Service can use the Course ID as the course reference when associating an assessment with a course.
- Attainment-Service may use Course Outcome records for outcome-level calculations.
- The courseId field identifies the associated Course in a Course Outcome record.
- Course IDs and Course Outcome IDs must be obtained from Course-Service responses.
- Other services should handle unsuccessful HTTP responses and missing records.
- Course-Service rejects Course Outcome creation when the referenced course does not exist, returning HTTP 400.
- Direct inter-service validation, authentication, JWT, and API Gateway routing remain pending until implemented and tested.

### Verification Results

Verification date: 2026-10-22

| Endpoint or Test | Result |
|---|---|
| GET /courses | Passed — four courses returned |
| GET /courses/1 | Passed — Course ID 1 returned |
| GET /course-outcomes | Passed — outcome records returned |
| GET /course-outcomes/course/1 | Passed — outcome linked to Course ID 1 returned |
| GET /courses/99999 | Passed — HTTP 404 for missing course |
| Create outcome with nonexistent courseId | Passed — HTTP 400 |
| GET /courses/page?page=0&size=500 | Passed — HTTP 400 for invalid page size |

Verified database counts: 4 courses and 1 Course Outcome.

Note: These results document observed behavior. Confirm individual status codes in Postman if a formal status-code audit is required.
