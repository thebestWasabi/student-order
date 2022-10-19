package main.domain;

import java.time.LocalDate;

public class Adult extends Person {

    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private PassportOffice passportDepartment;
    private University university;
    private String studentId;

    public Adult() {
    }

    public Adult(String surName, String givenName, String patronymic, LocalDate dateOfBirth) {
        super(surName, givenName, patronymic, dateOfBirth);
    }

    @Override
    public String toString() {
        return "Adult{" +
                "passportSeries='" + passportSeries + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", passportIssueDate=" + passportIssueDate +
                ", passportDepartment=" + passportDepartment +
                ", university=" + university +
                ", studentId='" + studentId + '\'' +
                "} " + super.toString();
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(LocalDate passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    public PassportOffice getPassportDepartment() {
        return passportDepartment;
    }

    public void setPassportDepartment(PassportOffice passportDepartment) {
        this.passportDepartment = passportDepartment;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
