package main.dao;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SimpleRunner {

    public static void main(String[] args) {
        SimpleRunner simpleRunner = new SimpleRunner();
        simpleRunner.runTests();
    }

    private void runTests() {
        try {
            Class aClass = Class.forName("main.dao.DictionaryDaoImplTest");
            Constructor constructor = aClass.getConstructor();
            Object entity = constructor.newInstance();
            Method[] methods = aClass.getMethods();
            for (Method m : methods) {
                Test annotation = m.getAnnotation(Test.class);
                if (annotation != null) {
                    m.invoke(entity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
