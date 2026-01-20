package jdbc.introduction;

import jdbc.introduction.model.Employee;
import jdbc.introduction.repository.EmployeeRepository;
import jdbc.introduction.repository.Repository;
import jdbc.introduction.util.DatabaseConnection;
import jdbc.introduction.view.SwingApp;

import java.sql.Connection;

import static jdbc.introduction.util.DatabaseUtils.printResultsList;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        try (Connection connection = DatabaseConnection.getInstance()) {
            System.out.println("Conexión a la base de datos realizada");
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            try {
                Repository<Employee> repo = new EmployeeRepository(connection);
                System.out.println("Insertando empleados");
                repo.save(Employee.builder()
                        .firstName("Anastasia")
                        .paSurname("Banamerina")
                        .email("bananaana@aaa.a")
                        .salary(8000.00f)
                        .curp("ABN123ASDPLZ1W2K03")
                        .build());
                printResultsList(repo.getAll(), Employee.class);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
            //JDBCUtils.simpleInsert(connection, "employees", new String[]{"first_name", "pa_surname", "ma_surname", "email", "salary"}, new Object[]{"Marco", "Osorio", "Naranjo", "marco@example.com", 55000.00});
            //JDBCUtils.simpleUpdate(connection, "employees", new String[]{"first_name", "pa_surname", "ma_surname"}, new Object[]{"Manuela", "Lopera", "Jaramillo"}, List.of("id = 11"));
            //JDBCUtils.simpleDelete(connection, "employees", List.of("id = 10"));
            //JDBCUtils.simpleSelect(connection, "employees", List.of("id", "first_name", "pa_surname", "ma_surname", "email", "salary"), null);
            //JDBCUtils.printResultSet(resultSet);
            //repo.delete(15);
            //System.out.println(repo.getById(2).orElse(new Employee()));
            //SwingApp app = new SwingApp();
            //app.setVisible(true);
        }
    }
}
