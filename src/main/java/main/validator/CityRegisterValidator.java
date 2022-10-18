package main.validator;

import main.domain.Person;
import main.exception.TransportException;
import main.register.AnswerCityRegister;
import main.domain.Child;
import main.register.AnswerCityRegisterItem;
import main.register.CityRegisterResponse;
import main.domain.StudentOrder;
import main.exception.CityRegisterException;
import main.validator.register.CityRegisterChecker;
import main.validator.register.FakeCityRegisterChecker;

import java.util.List;

public class CityRegisterValidator {

    public static final String IN_CODE = "NO GRN";

    private CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        personChecker = new FakeCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder studentOrder) {
        AnswerCityRegister answer = new AnswerCityRegister();

        answer.addItem(checkPerson(studentOrder.getHusband()));
        answer.addItem(checkPerson(studentOrder.getWife()));
        for (Child child : studentOrder.getChildren()) {
            answer.addItem(checkPerson(child));
        }
        return answer;
    }

    private AnswerCityRegisterItem checkPerson(Person person) {
        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;

        try {
            CityRegisterResponse tmp = personChecker.checkPerson(person);
            status = tmp.isExisting() ? AnswerCityRegisterItem.CityStatus.YES : AnswerCityRegisterItem.CityStatus.NO;
        } catch (CityRegisterException ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(ex.getCode(), ex.getMessage());
        } catch (TransportException ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, ex.getMessage());
        }

        AnswerCityRegisterItem answer = new AnswerCityRegisterItem(person, status, error);
        return answer;
    }

}