package jdbc.introduction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    static void printRow(String[] rows, int[] widths) {
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < rows.length; i++) {
            row.append("| ").append(String.format("%-" + widths[i] + "s", rows[i]))
                    .append(" ");
        }
        row.append("|");
        System.out.println(row);
    }

    static void printResultSet(ResultSet resultSet) throws SQLException {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String[]> rows = new ArrayList<>();
            String[] header = new String[columnCount];
            int[] widths = new int[columnCount];
            for (int i = 0; i < columnCount; i++) {
                header[i] = metaData.getColumnName(i + 1);
                widths[i] = metaData.getColumnName(i + 1).length();
            }
            rows.add(header);
            while (resultSet.next()) {
                String[] data = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    data[i] = resultSet.getString(i + 1);
                    widths[i] = Math.max(widths[i], data[i].length());
                }
                rows.add(data);
            }
            StringBuilder divider = new StringBuilder();
            for (int width : widths) {
                divider.append("*").append("-".repeat(width + 2));
            }
            System.out.println(divider);
            printRow(header, widths);
            System.out.println(divider);
            for (String[] row : rows) {
                printRow(row, widths);
            }
            System.out.println(divider);

        } catch (SQLException e) {
            System.out.printf("Algo ha salido mal al imprimir el resultado: %s%n", e);
        }
    }

    static ResultSet simpleSelect(Connection connection, String table,
                                  List<String> columns,
                                  List<String> filters) throws SQLException {
        try {
            StringBuilder selectQuery = new StringBuilder("SELECT ");
            if (columns == null || columns.isEmpty()) {
                selectQuery.append("* ");
            } else {
                for (int i = 0; i < columns.size(); i++) {
                    if (i == columns.size() - 1) {
                        selectQuery.append(columns.get(i)).append(" ");
                    } else {
                        selectQuery.append(columns.get(i)).append(", ");
                    }
                }
            }
            if (table != null && table.isEmpty()) {
                throw new IllegalArgumentException("Se debe indicar el nombre de la tabla a consultar");
            }
            System.out.printf("Ejecutando consulta en la tabla: %s%n", table);
            selectQuery.append("FROM ").append(table).append(" ");
            if (filters != null && !filters.isEmpty()) {
                selectQuery.append("WHERE ");
                for (int i = 0; i < filters.size(); i++) {
                    if (i == filters.size() - 1) {
                        selectQuery.append(filters.get(i)).append(" ");
                    } else {
                        selectQuery.append(filters.get(i)).append(", ");
                    }
                }
            }
            System.out.println(selectQuery);
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString());
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.printf("Algo ha salida mal con la sentencia SELECT: %s%n", e);
        }
        return null;
    }

    public static void main(String[] args) {
        String query = "INSERT INTO employees (first_name, pa_surname, ma_surname, email, salary) values (?,?,?,?,?)";
        int rowsAffected;
        Map<String, String> datasourceInformation = Map.of("connectionString", "jdbc:mysql://localhost:3306/project",
                "user", "root", "password", "1234");

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection connection = DriverManager.getConnection(datasourceInformation.get("connectionString"),
                datasourceInformation.get("user"), datasourceInformation.get("password"))) {
            System.out.println("Conectado");


            resultSet = simpleSelect(connection, "employees", List.of("first_name", "pa_surname", "ma_surname", "email", "salary"), null);
            printResultSet(resultSet);
            // statement = connection.createStatement();
            // resultSet = statement.executeQuery("SELECT * FROM employees");
            // while (resultSet.next()) {
            // System.out.println("ID: " + resultSet.getInt("id") + ", first_name: "
            // + resultSet.getString("first_name") + ", pa_surname: " +
            // resultSet.getString("pa_surname")
            // + ", ma_surname: " + resultSet.getString("ma_surname") + ", email: "
            // + resultSet.getString("email") + ", salary: " +
            // resultSet.getDouble("salary"));
            // }

            // System.out.println();

            // preparedStatement = connection.prepareStatement(query);
            // preparedStatement.setString(1, "Marco");
            // preparedStatement.setString(2, "Osorio");
            // preparedStatement.setString(3, "Naranjo");
            // preparedStatement.setString(4, "marco@example.com");
            // preparedStatement.setInt(5, 55000);

            // rowsAffected = preparedStatement.executeUpdate();
            // if (rowsAffected > 0)
            // System.out.println("Empleado creado");

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
