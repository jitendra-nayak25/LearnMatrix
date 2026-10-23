package com.oblms.authservice;

import com.oblms.authservice.config.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private String studentToken;
    private String facultyToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        studentToken = jwtService.generateToken("student@test.local", "STUDENT");
        facultyToken = jwtService.generateToken("faculty@test.local", "FACULTY");
        adminToken = jwtService.generateToken("admin@test.local", "ADMIN");
    }

    @Test
    void studentCanAccessStudentCourses() throws Exception {
        mockMvc.perform(get("/student/courses")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk());
    }

    @Test
    void studentSeesItsOwnIdentityFromToken() throws Exception {
        mockMvc.perform(get("/student/me")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("student@test.local"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    void facultyIsRejectedOnStudentRoutes() throws Exception {
        mockMvc.perform(get("/student/courses")
                        .header("Authorization", "Bearer " + facultyToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminIsRejectedOnStudentRoutes() throws Exception {
        mockMvc.perform(get("/student/courses")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void studentIsRejectedOnFacultyRoutes() throws Exception {
        mockMvc.perform(get("/faculty/courses")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void studentIsRejectedOnAdminUsers() throws Exception {
        mockMvc.perform(get("/auth/users")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isForbidden());
    }
}
