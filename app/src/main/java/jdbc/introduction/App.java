package jdbc.introduction;

import java.sql.*;
import java.util.*;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }


    public static void main(String[] args) {
        Map<String, String> datasourceInformation = Map.of("connectionString", "jdbc:mysql://localhost:3306/project",
                "user", "root", "password", "1234");
        ResultSet resultSet = null;
        try (Connection connection = DriverManager.getConnection(datasourceInformation.get("connectionString"),
                datasourceInformation.get("user"), datasourceInformation.get("password"))) {
            System.out.println("Conexión a la base de datos realizada");

            JDBCUtils.simpleInsert(connection, "employees", new String[]{"first_name", "pa_surname", "ma_surname", "email", "salary"}, new Object[]{"Marco", "Osorio", "Naranjo", "marco@example.com", 55000.00});

            resultSet = JDBCUtils.simpleSelect(connection, "employees", List.of("first_name", "pa_surname", "ma_surname", "email", "salary"), null);
            JDBCUtils.printResultSet(resultSet);
        } catch (Exception e) {
            System.out.println("Algo a salido mal: " + e);
            throw new RuntimeException("Fallo en la conexión:\n", e);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch (Exception e) {
                System.out.println("Algo a salido mal: " + e);
            }
        }
    }
}
