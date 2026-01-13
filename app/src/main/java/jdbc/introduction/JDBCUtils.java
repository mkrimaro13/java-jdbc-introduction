package jdbc.introduction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtils {
    public static void printRow(String[] rows, int[] widths) {
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < rows.length; i++) {
            row.append("| ").append(String.format("%-" + widths[i] + "s", rows[i]))
                    .append(" ");
        }
        row.append("|");
        System.out.println(row);
    }

    public static void printResultSet(ResultSet resultSet) throws SQLException {
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
            divider.append("*");
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

    public static ResultSet simpleSelect(Connection connection, String table,
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

    public static boolean simpleInsert(Connection connection, String table, String[] columns, Object[] values) throws SQLException {
        try {
            if (connection == null) {
                throw new IllegalArgumentException("Se requiere una conexión activa para realizar la consulta");
            }
            if (table == null || table.isEmpty()) {
                throw new IllegalArgumentException("La tabla en la que insertarán los datos no puede ser nula");
            }
            if (columns == null) {
                throw new IllegalArgumentException("Las columnas a insertar no pueden ser nulas");
            }
            if (values == null) {
                throw new IllegalArgumentException("Los valores a insertar no pueden ser nulos");
            }
            if (columns.length != values.length) {
                throw new IllegalArgumentException("El tamaño de las columnas a insertar y los valores a insertar es diferente");
            }
            StringBuilder insertQuery = new StringBuilder("INSERT INTO ").append(table).append(" (");
            for (int i = 0; i < columns.length; i++) {
                if (i == columns.length - 1) {
                    insertQuery.append(columns[i]).append(" ");
                } else {
                    insertQuery.append(columns[i]).append(", ");
                }
            }
            insertQuery.append(") VALUES (");
            for (int i = 0; i < values.length; i++) {
                if (i == columns.length - 1) {
                    insertQuery.append("?").append(" ");
                } else {
                    insertQuery.append("?").append(", ");
                }
            }
            insertQuery.append(")");
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString());
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            System.out.printf("Ejecutando inserción: %s%n", insertQuery);
            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted == 1;
        } catch (Exception e) {
            System.out.printf("Algo ha salido mal realizando la inserción de datos: %s%n", e);
        }
        return false;
    }
}
