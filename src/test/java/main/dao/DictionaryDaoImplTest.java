package main.dao;

import main.domain.CountryArea;
import main.domain.PassportOffice;
import main.domain.RegisterOffice;
import main.domain.Street;
import main.exception.DaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryDaoImplTest {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryDaoImplTest.class);

    @BeforeAll
    public static void startUp() throws Exception {
        DataBaseInit.startUp();
    }

    @Test
    public void testStreet() throws DaoException {
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("TEST {}", dateTime);
        List<Street> streets = new DictionaryDaoImpl().findStreets("про");
        Assertions.assertTrue(streets.size() == 2);
    }

    @Test
    public void testPassportOffice() throws DaoException {
        List<PassportOffice> passportOffices = new DictionaryDaoImpl().findPassportOffices("010020000000");
        Assertions.assertTrue(passportOffices.size() == 2);
    }

    @Test
    public void testRegisterOffice() throws DaoException {
        List<RegisterOffice> registerOffice = new DictionaryDaoImpl().findRegisterOffice("010010000000");
        Assertions.assertTrue(registerOffice.size() == 2);
    }

    @Test
    public void testCountryArea() throws DaoException {
        List<CountryArea> ca1 = new DictionaryDaoImpl().findAreas("");
        Assertions.assertTrue(ca1.size() == 2);
        List<CountryArea> ca2 = new DictionaryDaoImpl().findAreas("020000000000");
        Assertions.assertTrue(ca2.size() == 2);
        List<CountryArea> ca3 = new DictionaryDaoImpl().findAreas("020010000000");
        Assertions.assertTrue(ca3.size() == 2);
        List<CountryArea> ca4 = new DictionaryDaoImpl().findAreas("020010010000");
        Assertions.assertTrue(ca4.size() == 2);
    }

}