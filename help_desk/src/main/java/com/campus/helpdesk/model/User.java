package com.campus.helpdesk.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "university_id")
    private String universityId;

    @Column(name = "faculty_id")
    private Integer facultyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id", insertable = false, updatable = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Appointment> studentAppointments = new ArrayList<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Ticket> submittedTickets = new ArrayList<>();

    @OneToMany(mappedBy = "assignedStaff", fetch = FetchType.LAZY)
    private List<Ticket> assignedTickets = new ArrayList<>();

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "role")
    private String role;

    @Column(name = "account_status")
    private String accountStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_first_login")
    private boolean firstLogin = true;


    public User() {
    }

    public User(int userId, String fullName, String email, String universityId, int facultyId, String passwordHash, String role, String accountStatus) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.universityId = universityId;
        this.facultyId = facultyId;
        this.passwordHash = passwordHash;
        this.role = role;
        this.accountStatus = accountStatus;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUniversityId() { return universityId; }
    public void setUniversityId(String universityId) { this.universityId = universityId; }

    public Integer getFacultyId() { return facultyId; }
    public void setFacultyId(Integer facultyId) { this.facultyId = facultyId; }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public List<Appointment> getStudentAppointments() { return studentAppointments; }
    public void setStudentAppointments(List<Appointment> studentAppointments) { this.studentAppointments = studentAppointments; }

    public List<Ticket> getSubmittedTickets() { return submittedTickets; }
    public void setSubmittedTickets(List<Ticket> submittedTickets) { this.submittedTickets = submittedTickets; }

    public List<Ticket> getAssignedTickets() { return assignedTickets; }
    public void setAssignedTickets(List<Ticket> assignedTickets) { this.assignedTickets = assignedTickets; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public boolean isFirstLogin() { return firstLogin; }
    public void setFirstLogin(boolean firstLogin) { this.firstLogin = firstLogin; }
}