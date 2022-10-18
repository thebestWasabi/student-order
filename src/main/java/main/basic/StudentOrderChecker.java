package main.basic;

import main.answer.AnswerChildren;
import main.register.AnswerCityRegister;
import main.answer.AnswerStudent;
import main.answer.AnswerWedding;
import main.domain.StudentOrder;
import main.mail.MailSender;
import main.validator.ChildrenValidator;
import main.validator.CityRegisterValidator;
import main.validator.StudentValidator;
import main.validator.WeddingValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderChecker {

    private CityRegisterValidator cityRegisterValidator;
    private WeddingValidator weddingValidator;
    private ChildrenValidator childrenValidator;
    private StudentValidator studentValidator;
    private MailSender mailSender;

    public StudentOrderChecker() {
        cityRegisterValidator = new CityRegisterValidator();
        weddingValidator = new WeddingValidator();
        childrenValidator = new ChildrenValidator();
        studentValidator = new StudentValidator();
        mailSender = new MailSender();
    }

    public static void main(String[] args) {
        StudentOrderChecker soChecker = new StudentOrderChecker();
        soChecker.checkAll();
    }

    public void checkAll() {
        List<StudentOrder> studentOrderList = readStudentOrders();
        for (StudentOrder studentOrder : studentOrderList) {
            checkOneOrder(studentOrder);
        }
    }

    public List<StudentOrder> readStudentOrders() {
        List<StudentOrder> studentOrderList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            StudentOrder studentOrder = SaveStudentOrder.buildStudentOrder(i);
            studentOrderList.add(studentOrder);
        }
        return studentOrderList;
    }

    public void checkOneOrder(StudentOrder studentOrder) {
        AnswerCityRegister answerCityRegister = checkCityRegister(studentOrder);

//        AnswerWedding answerWedding = checkWedding(studentOrder);
//        AnswerChildren answerChildren = checkChildren(studentOrder);
//        AnswerStudent answerStudent = checkStudent(studentOrder);

//        sendMailStudentOrder(studentOrder);
    }

    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        return cityRegisterValidator.checkCityRegister(studentOrder);
    }

    public AnswerWedding checkWedding(StudentOrder studentOrder) {
        return weddingValidator.checkWedding(studentOrder);
    }

    public AnswerChildren checkChildren(StudentOrder studentOrder) {
        return childrenValidator.checkChildren(studentOrder);
    }

    public AnswerStudent checkStudent(StudentOrder studentOrder) {
        return studentValidator.checkStudent(studentOrder);
    }

    public void sendMailStudentOrder(StudentOrder studentOrder) {
        mailSender.sendMailStudentOrder(studentOrder);
    }
}