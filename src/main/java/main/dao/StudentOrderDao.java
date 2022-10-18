package main.dao;

import main.domain.StudentOrder;
import main.exception.DaoException;

import java.util.List;

public interface StudentOrderDao {

    Long saveStudentOrder(StudentOrder so) throws DaoException;

    List<StudentOrder> getStudentOrders() throws DaoException;
}
