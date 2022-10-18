package main.basic;

import main.dao.StudentOrderDao;
import main.dao.StudentOrderDaoImpl;
import main.domain.*;

import java.time.LocalDate;
import java.util.List;


public class SaveStudentOrder {


    public static void main(String[] args) throws Exception {

//        List<Street> d = new DictionaryDaoImpl().findStreets("про");
//        for (Street s : d) {
//            System.out.println(s.getStreetName());
//        }
//        List<PassportOffice> po = new DictionaryDaoImpl().findPassportOffices("010020000000");
//        for (PassportOffice p : po) {
//            System.out.println(p.getOfficeName());
//        }
//        List<RegisterOffice> ro = new DictionaryDaoImpl().findRegisterOffice("010010000000");
//        for (RegisterOffice r : ro) {
//            System.out.println(r.getOfficeName());
//        }
//        List<CountryArea> ca1 = new DictionaryDaoImpl().findAreas("");
//        for (CountryArea c : ca1) {
//            System.out.println(c.getAreaId() + ": " + c.getAreaName());
//        }
//        List<CountryArea> ca2 = new DictionaryDaoImpl().findAreas("020000000000");
//        for (CountryArea c : ca2) {
//            System.out.println(c.getAreaId() + ": " + c.getAreaName());
//        }
//        List<CountryArea> ca3 = new DictionaryDaoImpl().findAreas("020010000000");
//        for (CountryArea c : ca3) {
//            System.out.println(c.getAreaId() + ": " + c.getAreaName());
//        }
//        List<CountryArea> ca4 = new DictionaryDaoImpl().findAreas("020010010000");
//        for (CountryArea c : ca4) {
//            System.out.println(c.getAreaId() + ": " + c.getAreaName());
//        }

//        StudentOrder s = buildStudentOrder(10);
        StudentOrderDao dao = new StudentOrderDaoImpl();
//        Long id = dao.saveStudentOrder(s);
//        System.out.println(id);

        List<StudentOrder> soList = dao.getStudentOrders();
        for (StudentOrder studentOrder : soList) {
            System.out.println(studentOrder.getStudentOrderId());
        }
    }


    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        System.out.print("Сохранение студенчиской заявки");
        return answer;
    }


    static StudentOrder buildStudentOrder(long id) {

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
