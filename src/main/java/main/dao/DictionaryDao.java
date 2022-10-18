package main.dao;

import main.domain.CountryArea;
import main.domain.PassportOffice;
import main.domain.RegisterOffice;
import main.domain.Street;
import main.exception.DaoException;

import java.util.List;

public interface DictionaryDao {

    List<Street> findStreets(String pattern) throws DaoException;
    List<PassportOffice> findPassportOffices(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException;
    List<CountryArea> findAreas(String areaId) throws DaoException;

}
