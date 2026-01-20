package jdbc.introduction.repository;

import jdbc.introduction.model.Employee;
import jdbc.introduction.util.DatabaseConnection;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
public class EmployeeRepository implements Repository<Employee> {
    private final Connection connection;

    private Employee createEmployee(ResultSet resultSet) throws SQLException {
        return Employee.builder()
                .id(resultSet.getInt("id"))
                .firstName(resultSet.getString("first_name"))
                .paSurname(resultSet.getString("pa_surname"))
                .maSurname(resultSet.getString("ma_surname"))
                .email(resultSet.getString("email"))
                .salary(resultSet.getFloat("salary"))
                .curp(resultSet.getString("curp"))
                .build();
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); ResultSet resultSet = statement.executeQuery("SELECT * FROM employees")) {
            //JDBCUtils.printResultSet(resultSet);
            resultSet.beforeFirst();
            List<Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
                employees.add(createEmployee(resultSet));
            }
            return employees;
        }
    }

    @Override
    public Optional<Employee> getById(Object id) throws SQLException {
        Optional<Employee> emp = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees WHERE id = ?")) {
            statement.setObject(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    emp = Optional.of(createEmployee(resultSet));
                }
            }
        }
        return emp;
    }

    @Override
    public void save(Employee data) throws Exception {
        boolean employeeExists = data.getId() > 0 && checkEmployeeExists(data.getId());

        Map<String, Object> fieldData = extractNonNullFields(data);

        String query = employeeExists
                ? buildUpdateQuery(fieldData, data.getId())
                : buildInsertQuery(fieldData);

        System.out.println(query);

        executeQuery(query, fieldData.values());
    }

    @Override
    public void delete(int id) throws Exception {
        if (checkEmployeeExists(id)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM employees WHERE id = ?")) {
                preparedStatement.setObject(1, id);
                preparedStatement.executeUpdate();
            }
        } else {
            throw new Exception("No existe empleado con id: " + id);
        }

    }

    private boolean checkEmployeeExists(int id) throws Exception {
        String sql = "SELECT 1 FROM employees WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Map<String, Object> extractNonNullFields(Employee data) {
        Map<String, Object> fieldData = new LinkedHashMap<>();

        for (Field field : data.getClass().getDeclaredFields()) {
            if (field.getName().equals("id")) continue;

            field.setAccessible(true);
            try {
                Object value = field.get(data);
                if (value != null) {
                    String columnName = toSnakeCase(field.getName());
                    fieldData.put(columnName, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return fieldData;
    }

    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private String buildUpdateQuery(Map<String, Object> fieldData, int id) {
        String setClause = String.join(", ",
                fieldData.keySet().stream()
                        .map(col -> col + " = ?")
                        .toList()
        );
        return "UPDATE employees SET " + setClause + " WHERE id = " + id;
    }

    private String buildInsertQuery(Map<String, Object> fieldData) {
        String columns = String.join(", ", fieldData.keySet());
        String placeholders = String.join(", ", "?".repeat(fieldData.size()).split(""));
        return "INSERT INTO employees (" + columns + ") VALUES (" + placeholders + ")";
    }

    private void executeQuery(String query, Collection<Object> values) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            int index = 1;
            for (Object value : values) {
                stmt.setObject(index++, value);
            }
            stmt.executeUpdate();
        }
    }
}
