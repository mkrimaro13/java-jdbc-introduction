package jdbc.introduction.view;

import jdbc.introduction.model.Employee;
import jdbc.introduction.repository.EmployeeRepository;
import jdbc.introduction.repository.Repository;
import jdbc.introduction.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SwingApp extends JFrame {
    private final Repository<Employee> employeeRepository;
    private final JTable employeeTable;

    public SwingApp() {
        // Configuración de la ventana.
        setTitle("Gestión de empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(630, 630);

        // Crear y mostrar table de empleados
        employeeTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Crear botones
        JButton addButton = new JButton("Agregar");
        JButton updateButton = new JButton("Actualizar");
        JButton deleteButton = new JButton("Eliminar");

        // Configurar botones
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Estilos de los botones
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        updateButton.setBackground(new Color(52, 152, 219));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);

        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);

        // Crear el repositorio
        try{
            employeeRepository = new EmployeeRepository(DatabaseConnection.getInstance());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Cargar empleados iniciales en la tabla
        refreshEmployeeTable();

        // Agregar ActionListener a los botones
        addButton.addActionListener(ae -> {
            try {
                addEmployee();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        updateButton.addActionListener(ae -> updateEmployee());
        deleteButton.addActionListener(ae -> deleteEmployee());
    }

    private void refreshEmployeeTable() {
        try {
            List<Employee> employees = employeeRepository.getAll();

            // Crear modelo de tabla y establecer datos de empleados
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Nombres");
            model.addColumn("Apellido Paterno");
            model.addColumn("Apellido Materno");
            model.addColumn("Email");
            model.addColumn("Salario");

            for (Employee employee : employees) {
                Object[] rowData = {
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getPaSurname(),
                        employee.getMaSurname(),
                        employee.getEmail(),
                        employee.getSalary()
                };
                model.addRow(rowData);
            }

            employeeTable.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener los empleados de la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee() throws Exception {
        JTextField names = new JTextField();
        JTextField paSurname = new JTextField();
        JTextField maSurname = new JTextField();
        JTextField email = new JTextField();
        JTextField salary = new JTextField();

        Object[] fields = {
                "Nombres:", names,
                "Apellido paterno:", paSurname,
                "Apellido materno:", maSurname,
                "Email:", email,
                "Salario:", salary
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Agregar Empleado", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Crear un empleado con los datos ingresados
            Employee employee = Employee.builder()
                    .firstName(names.getText())
                    .paSurname(paSurname.getText())
                    .maSurname(maSurname.getText())
                    .email(email.getText())
                    .salary(Float.parseFloat(salary.getText()))
                    .build();
            employeeRepository.save(employee);

            refreshEmployeeTable();

            JOptionPane.showMessageDialog(this, "Empleado agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateEmployee() {
        String employeeId = JOptionPane.showInputDialog(this, "Ingresa el identificador del empleado a actualizar", "Actualizar empleado", JOptionPane.QUESTION_MESSAGE);
        int id = 0;

        try {
            if (employeeId == null)
                JOptionPane.showMessageDialog(this, "Se requiere un ID para realizar la actualización", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                id = Integer.parseInt(employeeId);
            }
        } catch (
                NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Se requiere un ID para realizar la actualización", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Employee employee = employeeRepository.getById(employeeId).orElse(null);

            if (employee == null) return;

            // Crear un formulario con los datos del empleado
            JTextField nameField = new JTextField(employee.getFirstName());
            JTextField paSurnameField = new JTextField(employee.getPaSurname());
            JTextField maSurnameField = new JTextField(employee.getMaSurname());
            JTextField emailField = new JTextField(employee.getEmail());
            JTextField salaryField = new JTextField(String.valueOf(employee.getSalary()));

            Object[] fields = {
                    "Nombre:", nameField,
                    "Apellido Paterno:", paSurnameField,
                    "Apellido Materno:", maSurnameField,
                    "Email:", emailField,
                    "Salario:", salaryField
            };

            int confirmResult = JOptionPane.showConfirmDialog(this, fields, "Actualizar Empleado", JOptionPane.OK_CANCEL_OPTION);
            if (confirmResult == JOptionPane.OK_OPTION) {
                // Actualizar los datos del empleado con los valores ingresados en el formulario
                employee = Employee.builder()
                        .firstName(nameField.getText())
                        .paSurname(paSurnameField.getText())
                        .maSurname(maSurnameField.getText())
                        .email(emailField.getText())
                        .salary(Float.parseFloat(salaryField.getText()))
                        .build();

                // Guardar los cambios en la base de datos
                employeeRepository.save(employee);

                // Actualizar la tabla de empleados en la interfaz
                refreshEmployeeTable();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos del empleado de la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        String employeeId = JOptionPane.showInputDialog(this, "Ingresa el identificador del empleado a eliminar", "Eliminar empleado", JOptionPane.QUESTION_MESSAGE);
        int id = 0;

        try {
            if (employeeId == null)
                JOptionPane.showMessageDialog(this, "Se requiere un ID para realizar la actualización", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                id = Integer.parseInt(employeeId);
                if (id <= 0) {
                    JOptionPane.showMessageDialog(this, "ID debe ser mayor que 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
                int confirmResult = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el empleado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    // Eliminar el empleado de la base de datos
                    employeeRepository.delete(id);

                    // Actualizar la tabla de empleados en la interfaz
                    refreshEmployeeTable();
                }


            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Se requiere un ID para realizar la actualización", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Algo ha salido mal eliminando al empleado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
