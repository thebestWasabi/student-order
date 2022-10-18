package main.validator.register;

import main.domain.Person;
import main.exception.CityRegisterException;
import main.exception.TransportException;
import main.register.CityRegisterResponse;

public class RealCityRegisterChecker implements CityRegisterChecker {

    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException {
        return null;
    }
}
