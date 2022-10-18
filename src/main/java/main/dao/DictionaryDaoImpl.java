package main.dao;

import main.config.Config;
import main.domain.CountryArea;
import main.domain.PassportOffice;
import main.domain.RegisterOffice;
import main.domain.Street;
import main.exception.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DictionaryDaoImpl implements DictionaryDao {

    private static final String GET_STREET = "select * from jc_street where upper(street_name) like upper(?)";
    private static final String GET_PASSPORT = "select * from jc_passport_office where p_office_area_id = ?";
    private static final String GET_REGISTER = "select * from jc_register_office where r_office_area_id = ?";
    private static final String GET_AREA = "select * from jc_country_struct where area_id like ? and area_id <> ?";


    private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();
    }

    @Override
    public List<Street> findStreets(String pattern) throws DaoException {
        List<Street> result = new LinkedList<>();

        try (Connection con = getConnection(); PreparedStatement statement = con.prepareStatement(GET_STREET)) {
            statement.setString(1, "%" + pattern + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Street street = new Street(
                        resultSet.getLong("street_code"),
                        resultSet.getString("street_name"));
                result.add(street);
            }

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;
    }

    @Override
    public List<PassportOffice> findPassportOffices(String areaId) throws DaoException {
        List<PassportOffice> result = new LinkedList<>();

        try (Connection con = getConnection(); PreparedStatement statement = con.prepareStatement(GET_PASSPORT)) {
            statement.setString(1, areaId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PassportOffice str = new PassportOffice(
                        resultSet.getLong("p_office_id"),
                        resultSet.getString("p_office_area_id"),
                        resultSet.getString("p_office_name"));
                result.add(str);
            }

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;
    }

    @Override
    public List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException {
        List<RegisterOffice> result = new LinkedList<>();

        try (Connection con = getConnection(); PreparedStatement statement = con.prepareStatement(GET_REGISTER)) {
            statement.setString(1, areaId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RegisterOffice str = new RegisterOffice(
                        resultSet.getLong("r_office_id"),
                        resultSet.getString("r_office_area_id"),
                        resultSet.getString("r_office_name"));
                result.add(str);
            }

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;
    }

    @Override
    public List<CountryArea> findAreas(String areaId) throws DaoException {
        List<CountryArea> result = new LinkedList<>();

        try (Connection con = getConnection(); PreparedStatement statement = con.prepareStatement(GET_AREA)) {
            String param1 = buildParam(areaId);
            String param2 = areaId;

            statement.setString(1, param1);
            statement.setString(2, param2);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CountryArea str = new CountryArea(
                        resultSet.getString("area_id"),
                        resultSet.getString("area_name"));
                result.add(str);
            }

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;
    }

    private String buildParam(String areaId) throws SQLException{

        if (areaId == null || areaId.trim().isEmpty()) {
            return "__0000000000";
        } else if (areaId.endsWith("0000000000")) {
            return areaId.substring(0, 2) + "___0000000";
        } else if (areaId.endsWith("0000000")) {
            return areaId.substring(0, 5) + "___0000";
        } else if (areaId.endsWith("0000")) {
            return areaId.substring(0, 8) + "____";
        }

        throw new SQLException("Invalid parameter 'areaId' : " + areaId);
    }
}
