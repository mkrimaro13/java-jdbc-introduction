package jdbc.introduction;

import jdbc.introduction.model.Employee;
import jdbc.introduction.repository.EmployeeRepository;
import jdbc.introduction.repository.Repository;
import jdbc.introduction.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

import static jdbc.introduction.util.DatabaseUtils.printResultsList;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws SQLException, IllegalAccessException {
        try (Connection connection = DatabaseConnection.getInstance()) {
            System.out.println("Conexión a la base de datos realizada");
//            JDBCUtils.simpleInsert(connection, "employees", new String[]{"first_name", "pa_surname", "ma_surname", "email", "salary"}, new Object[]{"Marco", "Osorio", "Naranjo", "marco@example.com", 55000.00});
//            JDBCUtils.simpleUpdate(connection, "employees", new String[]{"first_name", "pa_surname", "ma_surname"}, new Object[]{"Manuela", "Lopera", "Jaramillo"}, List.of("id = 11"));
//            JDBCUtils.simpleDelete(connection, "employees", List.of("id = 10"));
//            JDBCUtils.simpleSelect(connection, "employees", List.of("id", "first_name", "pa_surname", "ma_surname", "email", "salary"), null);
            //JDBCUtils.printResultSet(resultSet);
            Repository<Employee> repo = new EmployeeRepository();
            printResultsList(repo.getAll(), Employee.class);
        }
    }
}
