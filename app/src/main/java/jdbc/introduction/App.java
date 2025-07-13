package jdbc.introduction;

import java.sql.*;
import java.util.Map;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        String query = "INSERT INTO employees (first_name, pa_surname, ma_surname, email, salary) values (?,?,?,?,?)";
        int rowsAffected;
        Map<String, String> datasourceInformation = Map.of("connectionString", "jdbc:mysql://localhost:3306/project", "user", "root", "password", "1234");

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement;
        try {
            connection = DriverManager.getConnection(datasourceInformation.get("connectionString"), datasourceInformation.get("user"), datasourceInformation.get("password"));
            System.out.println("Conectado");

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM employees");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", first_name: " + resultSet.getString("first_name") + ", pa_surname: " + resultSet.getString("pa_surname") + ", ma_surname: " + resultSet.getString("ma_surname") + ", email: " + resultSet.getString("email") + ", salary: " + resultSet.getDouble("salary"));
            }

            System.out.println();

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Marco");
            preparedStatement.setString(2, "Osorio");
            preparedStatement.setString(3, "Naranjo");
            preparedStatement.setString(4, "marco@example.com");
            preparedStatement.setInt(5, 55000);

            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) System.out.println("Empleado creado");

        } catch (Exception e) {
            System.out.println("Algo a salido mal: " + e);
            throw new RuntimeException("Fallo en la conexión:\n", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.out.println("Algo a salido mal: " + e);
            }
        }
    }
}
