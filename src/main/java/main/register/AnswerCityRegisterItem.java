package main.register;

import main.domain.Person;

public class AnswerCityRegisterItem {

    private Person person;
    private CityStatus cityStatus;
    private CityError cityError;

    public enum CityStatus {
        YES, NO, ERROR;
    }

    public static class CityError {
        private String code;
        private String text;

        public CityError(String code, String text) {
            this.code = code;
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }

    public AnswerCityRegisterItem(Person person, CityStatus cityStatus) {
        this.person = person;
        this.cityStatus = cityStatus;
    }

    public AnswerCityRegisterItem(Person person, CityStatus cityStatus, CityError cityError) {
        this.person = person;
        this.cityStatus = cityStatus;
        this.cityError = cityError;
    }

    public Person getPerson() {
        return person;
    }

    public CityStatus getCityStatus() {
        return cityStatus;
    }

    public CityError getCityError() {
        return cityError;
    }
}
