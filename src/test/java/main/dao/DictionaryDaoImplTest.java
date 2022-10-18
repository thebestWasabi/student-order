package main.dao;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class DictionaryDaoImplTest {

    @BeforeAll
    public static void startUp() throws Exception {
        URL url1 = DictionaryDaoImpl.class.getClassLoader().getResource("student-project.sql");
        URL url2 = DictionaryDaoImpl.class.getClassLoader().getResource("student-data.sql");

        List<String> strings1 = Files.readAllLines(Paths.get(url1.toURI()));
        String sql1 = strings1.stream().collect(Collectors.joining());

        List<String> strings2 = Files.readAllLines(Paths.get(url1.toURI()));
        String sql2 = strings2.stream().collect(Collectors.joining());

        try (Connection connection = ConnectionBuilder.getConnection();
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql1);
            statement.executeUpdate(sql2);
        }
    }

    @Test
    public void testExample1() {
        System.out.println("TEST1");
    }
    @Test
    public void testExample2() {
        System.out.println("TEST2");
    }
    @Test
    public void testExample3() {
        System.out.println("TEST3");
    }

}