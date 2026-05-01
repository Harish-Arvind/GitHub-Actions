package fr.takima.training.simpleapi.dto;

import fr.takima.training.simpleapi.entity.Department;

public class StudentDto {
    private String firstname;
    private String lastname;
    private Department department;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
