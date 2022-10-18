package main.validator.register;

import main.domain.Adult;
import main.domain.Child;
import main.exception.TransportException;
import main.register.CityRegisterResponse;
import main.domain.Person;
import main.exception.CityRegisterException;

// Временный класс-заглулка для CityRegisterValidator, пока не настрою RealCityRegisterChecker

public class FakeCityRegisterChecker implements CityRegisterChecker {

    private static final String GOOD_1 = "1000";
    private static final String GOOD_2 = "2000";
    private static final String BAD_1 = "1001";
    private static final String BAD_2 = "2001";
    private static final String GRN_ERROR_1 = "1002";
    private static final String GRN_ERROR_2 = "2002";
    private static final String TRANSPORT_ERROR_1 = "1003";
    private static final String TRANSPORT_ERROR_2 = "2003";


    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException {

        CityRegisterResponse response = new CityRegisterResponse();

        if (person instanceof Adult) {
            Adult adult = (Adult) person;
            String passportSeries = adult.getPassportSeries();
            if (passportSeries.equals(GOOD_1) || passportSeries.equals(GOOD_2)) {
                response.setExisting(true);
                response.setTemporal(false);
            }
            if (passportSeries.equals(BAD_1) || passportSeries.equals(BAD_2)) {
                response.setExisting(false);
            }
            if (passportSeries.equals(GRN_ERROR_1) || passportSeries.equals(GRN_ERROR_2)) {
                CityRegisterException ex = new CityRegisterException("1", "GRN ERROR " + passportSeries);
                throw ex;
            }
            if (passportSeries.equals(TRANSPORT_ERROR_1) || passportSeries.equals(TRANSPORT_ERROR_2)) {
                TransportException ex = new TransportException("Transport ERROR " + passportSeries);
                throw ex;
            }
        }

        if (person instanceof Child) {
            response.setExisting(true);
            response.setTemporal(true);
        }

        System.out.println(response);
        return response;
    }
}