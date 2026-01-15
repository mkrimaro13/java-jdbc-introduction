package jdbc.introduction.repository;

import jdbc.introduction.JDBCUtils;
import jdbc.introduction.model.Employee;
import jdbc.introduction.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository implements Repository<Employee> {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    private Employee createEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("pa_surname"), resultSet.getString("ma_surname"), resultSet.getString("email"), resultSet.getFloat("salary"));
    }

    @Override
    public List<Employee> getAll() throws SQLException {
        try (Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employees")) {
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
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM employees WHERE id = ?")) {
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
    public void save(Employee data) {

    }

    @Override
    public void delete(Object id) {

    }
}
