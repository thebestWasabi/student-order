package main.dao;

import main.domain.*;
import main.exception.DaoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class StudentOrderDaoImplTest {

    @BeforeAll
    public static void startUp() throws Exception {
        DataBaseInit.startUp();
    }

    @Test
    void saveStudentOrder() throws DaoException {
        StudentOrder studentOrder = buildStudentOrder(10);
        Long id = new StudentOrderDaoImpl().saveStudentOrder(studentOrder);
    }

    @Test
    void getStudentOrders() throws DaoException {
        List<StudentOrder> list = new StudentOrderDaoImpl().getStudentOrders();
    }

    public StudentOrder buildStudentOrder(long id) {
        StudentOrder studentOrder = new StudentOrder();
        studentOrder.setStudentOrderId(id);
        studentOrder.setMarriageCertificateId("" + (123456000) + id);
        studentOrder.setMarriageDate(LocalDate.of(2016, 7, 4));
        RegisterOffice ro = new RegisterOffice(1L, "", "");
        studentOrder.setMarriageOffice(ro);

        Street street = new Street(1L, "First street");

        Address address = new Address("195000", street, "12", "", "142");

        // Муж
        Adult husband = new Adult("Петров", "Виктор", "Сергеевич", LocalDate.of(1968, 8, 24));
        husband.setPassportSeries("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setPassportIssueDate(LocalDate.of(2017, 9, 15));
        PassportOffice po1 = new PassportOffice(1L, "", "");
        husband.setPassportDepartment(po1);
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2L, ""));
        husband.setStudentId("HH12345");

        // Жена
        Adult wife = new Adult("Петрова", "Вероника", "Алексеевна", LocalDate.of(1999, 4, 23));
        wife.setPassportSeries("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setPassportIssueDate(LocalDate.of(2018, 4, 5));
        PassportOffice po2 = new PassportOffice(2L, "", "");
        wife.setPassportDepartment(po2);
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(1L, ""));
        wife.setStudentId("WW12345");

        // Ребенок
        Child child1 = new Child("Петрова", "Ирина", "Викторовна", LocalDate.of(2018, 6, 29));
        child1.setCertificateNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2018, 7, 19));
        RegisterOffice ro2 = new RegisterOffice(2L, "", "");
        child1.setIssueDepartment(ro2);
        child1.setAddress(address);

        // Ребенок
        Child child2 = new Child("Петров", "Евгений", "Викторович", LocalDate.of(2018, 6, 29));
        child2.setCertificateNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2018, 7, 19));
        RegisterOffice ro3 = new RegisterOffice(3L, "", "");
        child2.setIssueDepartment(ro3);
        child2.setAddress(address);

        studentOrder.setHusband(husband);
        studentOrder.setWife(wife);
        studentOrder.addChild(child1);
        studentOrder.addChild(child2);

        return studentOrder;
    }

}