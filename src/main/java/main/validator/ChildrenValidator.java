package main.validator;

import main.answer.AnswerChildren;
import main.domain.StudentOrder;

public class ChildrenValidator {

    public AnswerChildren checkChildren(StudentOrder studentOrder) {
        System.out.println("Children check is running");
        AnswerChildren answerChildren = new AnswerChildren();
        return answerChildren;
    }
}
