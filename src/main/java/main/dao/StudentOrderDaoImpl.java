package main.dao;

import main.config.Config;
import main.domain.*;
import main.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentOrderDaoImpl implements StudentOrderDao {

    private static final Logger logger = LoggerFactory.getLogger(StudentOrderDaoImpl.class);

    private static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
            "student_order_status, student_order_date, h_sur_name, " +
            "h_given_name, h_patronymic, h_date_of_birth, h_passport_series, " +
            "h_passport_number, h_passport_date, h_passport_office_id, h_post_index, " +
            "h_street_code, h_building, h_extension, h_apartment, h_university_id, h_student_number, " +
            "w_sur_name, w_given_name, w_patronymic, w_date_of_birth, w_passport_series, " +
            "w_passport_number, w_passport_date, w_passport_office_id, w_post_index, " +
            "w_street_code, w_building, w_extension, w_apartment, w_university_id, w_student_number, " +
            "certificate_id, register_office_id, marriage_date)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_CHILD = "INSERT INTO jc_student_child(" +
            "student_order_id, ch_sur_name, ch_given_name, ch_patronymic, " +
            "ch_date_of_birth, ch_certificate_number, ch_certificate_date, ch_register_office_id, " +
            "ch_post_index, ch_street_code, ch_building, ch_extension, ch_apartment)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SELECT_ORDERS = "SELECT so.*, ro.r_office_area_id, ro.r_office_name, " +
            "po_h.p_office_area_id as h_p_office_area_id, po_h.p_office_name as h_p_office_name, " +
            "po_w.p_office_area_id as w_p_office_area_id, po_w.p_office_name as w_p_office_name " +
            "FROM jc_student_order so " +
            "INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
            "INNER JOIN jc_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
            "INNER JOIN jc_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
            "WHERE student_order_status = ? ORDER BY student_order_date LIMIT ?";

    public static final String SELECT_CHILD = "SELECT soch.*, ro.r_office_area_id, ro.r_office_name " +
            "FROM jc_student_child soch " +
            "INNER JOIN jc_register_office ro ON ro.r_office_id = soch.ch_register_office_id " +
            "WHERE soch.student_order_id IN ";

    private static final String SELECT_ORDERS_FULL = "SELECT so.*, ro.r_office_area_id, ro.r_office_name," +
            "po_h.p_office_area_id as h_p_office_area_id, po_h.p_office_name as h_p_office_name, " +
            "po_w.p_office_area_id as w_p_office_area_id, po_w.p_office_name as w_p_office_name," +
            "soch.*, ro_ch.r_office_area_id, ro_ch.r_office_name " +
            "FROM jc_student_order so " +
            "INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
            "INNER JOIN jc_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
            "INNER JOIN jc_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
            "INNER JOIN jc_student_child soch ON soch.student_order_id = so.student_order_id " +
            "INNER JOIN jc_register_office ro_ch ON ro_ch.r_office_id = soch.ch_register_office_id " +
            "WHERE student_order_status = ? ORDER BY so.student_order_id LIMIT ?";


    private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();
    }

    @Override
    public Long saveStudentOrder(StudentOrder studentOrder) throws DaoException {
        long result = -1L;

        logger.debug("SO:{}", studentOrder);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"})) {

            connection.setAutoCommit(false);
            try {
                // Header
                statement.setInt(1, StudentOrderStatus.START.ordinal());
                statement.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                // Husband and Wife
                setParamsForAdult(statement, 3, studentOrder.getHusband());
                setParamsForAdult(statement, 18, studentOrder.getWife());
                // Marriage
                statement.setString(33, studentOrder.getMarriageCertificateId());
                statement.setLong(34, studentOrder.getMarriageOffice().getOfficeId());
                statement.setDate(35, java.sql.Date.valueOf(studentOrder.getMarriageDate()));

                statement.executeUpdate();

                ResultSet gKeysRs = statement.getGeneratedKeys();
                if (gKeysRs.next()) {
                    result = gKeysRs.getLong(1);
                }
                gKeysRs.close();

                saveChildren(connection, studentOrder, result);
                connection.commit();
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    private void saveChildren(Connection connection, StudentOrder studentOrder, Long studentOrderId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_CHILD)) {
            for (Child child : studentOrder.getChildren()) {
                statement.setLong(1, studentOrderId);
                setParamsForChild(statement, child);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private static void setParamsForAdult(PreparedStatement statement, int start, Adult adult) throws SQLException {
        setParamsForPerson(statement, start, adult);
        statement.setString(start + 4, adult.getPassportSeries());
        statement.setString(start + 5, adult.getPassportNumber());
        statement.setDate(start + 6, Date.valueOf(adult.getPassportIssueDate()));
        statement.setLong(start + 7, adult.getPassportDepartment().getOfficeId());
        setParamsForAddress(statement, start + 8, adult);
        statement.setLong(start + 13, adult.getUniversity().getUniversityId());
        statement.setString(start + 14, adult.getStudentId());
    }

    private void setParamsForChild(PreparedStatement statement, Child child) throws SQLException {
        setParamsForPerson(statement, 2, child);
        statement.setString(6, child.getCertificateNumber());
        statement.setDate(7, java.sql.Date.valueOf(child.getIssueDate()));
        statement.setLong(8, child.getIssueDepartment().getOfficeId());
        setParamsForAddress(statement, 9, child);
    }

    private static void setParamsForPerson(PreparedStatement statement, int start, Person person) throws SQLException {
        statement.setString(start, person.getSurName());
        statement.setString(start + 1, person.getGivenName());
        statement.setString(start + 2, person.getPatronymic());
        statement.setDate(start + 3, Date.valueOf(person.getDateOfBirth()));
    }

    private static void setParamsForAddress(PreparedStatement statement, int start, Person person) throws SQLException {
        Address adult_address = person.getAddress();
        statement.setString(start, adult_address.getPostCode());
        statement.setLong(start + 1, adult_address.getStreet().getStreetCode());
        statement.setString(start + 2, adult_address.getBuilding());
        statement.setString(start + 3, adult_address.getExtension());
        statement.setString(start + 4, adult_address.getApartment());
    }

    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
//        return getStudentOrdersOneSelect();
        return getStudentOrdersTwoSelect();
    }

    private List<StudentOrder> getStudentOrdersOneSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDERS_FULL)) {
            Map<Long, StudentOrder> maps = new HashMap<>();
            statement.setInt(1, StudentOrderStatus.START.ordinal());
            int limit = Integer.parseInt(Config.getProperty(Config.DB_LIMIT));
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            int counter = 0;
            while (resultSet.next()) {
                Long studentOrderId = resultSet.getLong("student_order_id");
                if (!maps.containsKey(studentOrderId)) {
                    StudentOrder studentOrder = getFullStudentOrder(resultSet);

                    result.add(studentOrder);
                    maps.put(studentOrderId, studentOrder);
                }
                StudentOrder studentOrder = maps.get(studentOrderId);
                studentOrder.addChild(fillChild(resultSet));
                counter++;
            }
            if (counter >= limit) {
                result.remove(result.size() - 1);
            }
            resultSet.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    private List<StudentOrder> getStudentOrdersTwoSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ORDERS)) {
            statement.setInt(1, StudentOrderStatus.START.ordinal());
            statement.setInt(2, Integer.parseInt(Config.getProperty(Config.DB_LIMIT)));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                StudentOrder studentOrder = getFullStudentOrder(resultSet);

                result.add(studentOrder);
            }
            findChildren(connection, result);
            resultSet.close();
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    private StudentOrder getFullStudentOrder(ResultSet resultSet) throws SQLException {
        StudentOrder studentOrder = new StudentOrder();

        fillStudentOrder(resultSet, studentOrder);
        fillMarriage(resultSet, studentOrder);

        studentOrder.setHusband(fillAdult(resultSet, "h_"));
        studentOrder.setWife(fillAdult(resultSet, "w_"));
        return studentOrder;
    }

    private void fillStudentOrder(ResultSet rs, StudentOrder so) throws SQLException {
        so.setStudentOrderId(rs.getLong("student_order_id"));
        so.setStudentOrderStatus(StudentOrderStatus.fromValue(rs.getInt("student_order_status")));
        so.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());
    }

    private void fillMarriage(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setMarriageCertificateId(resultSet.getString("certificate_id"));
        studentOrder.setMarriageDate(resultSet.getDate("marriage_date").toLocalDate());

        Long roId = resultSet.getLong("register_office_id");
        String areaId = resultSet.getString("r_office_area_id");
        String name = resultSet.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, areaId, name);
        studentOrder.setMarriageOffice(ro);
    }

    private Adult fillAdult(ResultSet rs, String prefix) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(rs.getString(prefix + "sur_name"));
        adult.setGivenName(rs.getString(prefix + "given_name"));
        adult.setPatronymic(rs.getString(prefix + "patronymic"));
        adult.setDateOfBirth(rs.getDate(prefix + "date_of_birth").toLocalDate());
        adult.setPassportSeries(rs.getString(prefix + "passport_series"));
        adult.setPassportNumber(rs.getString(prefix + "passport_number"));
        adult.setPassportIssueDate(rs.getDate(prefix + "passport_date").toLocalDate());

        Long poId = rs.getLong(prefix + "passport_office_id");
        String poAreaId = rs.getString(prefix + "p_office_area_id");
        String poName = rs.getString(prefix + "p_office_name");
        PassportOffice po = new PassportOffice(poId, poAreaId, poName);
        adult.setPassportDepartment(po);

        Address address = new Address();
        address.setPostCode(rs.getString(prefix + "post_index"));
        Street street = new Street(rs.getLong(prefix + "street_code"), "");
        address.setStreet(street);
        address.setBuilding(rs.getString(prefix + "building"));
        address.setExtension(rs.getString(prefix + "extension"));
        address.setApartment(rs.getString(prefix + "apartment"));
        adult.setAddress(address);

        University university = new University(rs.getLong(prefix + "university_id"), "");
        adult.setUniversity(university);
        adult.setStudentId(rs.getString(prefix + "student_number"));

        return adult;
    }

    private void findChildren(Connection connection, List<StudentOrder> result) throws SQLException {
        String collect = "(" + result.stream().map(so -> String.valueOf(so.getStudentOrderId()))
                .collect(Collectors.joining(",")) + ")";

        Map<Long, StudentOrder> maps = result.stream().collect(Collectors
                .toMap(studentOrder -> studentOrder.getStudentOrderId(), studentOrder -> studentOrder));

        try (PreparedStatement statement = connection.prepareStatement(SELECT_CHILD + collect)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Child ch = fillChild(resultSet);
                StudentOrder studentOrder = maps.get(resultSet.getLong("student_order_id"));
                studentOrder.addChild(ch);
            }
        }
    }

    private Child fillChild(ResultSet rs) throws SQLException {
        String surName = rs.getString("ch_sur_name");
        String givenName = rs.getString("ch_given_name");
        String patronymic = rs.getString("ch_patronymic");
        LocalDate dateOfBirth = rs.getDate("ch_date_of_birth").toLocalDate();

        Child child = new Child(surName, givenName, patronymic, dateOfBirth);

        child.setCertificateNumber(rs.getString("ch_certificate_number"));
        child.setIssueDate(rs.getDate("ch_certificate_date").toLocalDate());

        Long registerOfficeId = rs.getLong("ch_register_office_id");
        String registerOfficeAreaId = rs.getString("r_office_area_id");
        String registerOfficeName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(registerOfficeId, registerOfficeAreaId, registerOfficeName);
        child.setIssueDepartment(ro);

        Address address = new Address();
        address.setPostCode(rs.getString("ch_post_index"));
        Street street = new Street(rs.getLong("ch_street_code"), "");
        address.setStreet(street);
        address.setBuilding(rs.getString("ch_building"));
        address.setExtension(rs.getString("ch_extension"));
        address.setApartment(rs.getString("ch_apartment"));
        child.setAddress(address);

        return child;
    }
}
