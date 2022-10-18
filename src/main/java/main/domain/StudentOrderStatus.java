package main.domain;

public enum StudentOrderStatus {
    START, CHECKED;


    public static StudentOrderStatus fromValue (int value) {
        for (StudentOrderStatus soStatus : StudentOrderStatus.values()) {
            if (soStatus.ordinal() == value) {
                return soStatus;
            }
        }
        throw new RuntimeException("Unknown value: " + value);
    }
}
