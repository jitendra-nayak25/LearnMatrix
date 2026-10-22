
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
