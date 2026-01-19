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

    public static void printResultSet(ResultSet resultSet) {
        try {
            if (resultSet == null) {
                throw new IllegalArgumentException("Se requiere un set de resultados para poder imprimirlo");
            }
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
            List<String> filters) throws Exception {
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery.toString());) {

            printResultSet(preparedStatement.executeQuery());
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.printf("Algo ha salida mal con la sentencia SELECT: %s%n", e);
        }
        return null;
    }

    public static void simpleInsert(Connection connection, String table, String[] columns, Object[] values)
            throws Exception {
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
            throw new IllegalArgumentException(
                    "El tamaño de las columnas a insertar y los valores a insertar es diferente");
        }
        System.out.printf("Ejecutando inserción en la tabla: %s%n", table);
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString());) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.printf("Algo ha salido mal realizando la inserción de datos: %s%n", e);
        }
    }

    public static void simpleUpdate(Connection connection, String table, String[] columns, Object[] values,
            List<String> filters) {
        if (connection == null) {
            throw new IllegalArgumentException("Se requiere una conexión activa para realizar la consulta");
        }
        if (table == null || table.isEmpty()) {
            throw new IllegalArgumentException("La tabla en la que insertarán los datos no puede ser nula");
        }
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException("Las columnas a insertar no pueden ser nulas");
        }
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Los valores a insertar no pueden ser nulos");
        }
        if (columns.length != values.length) {
            throw new IllegalArgumentException(
                    "El tamaño de las columnas a insertar y los valores a insertar es diferente");
        }
        if (filters == null || filters.isEmpty()) {
            throw new IllegalArgumentException("Se requiere algún filtro para realizar una correcta actualización");
        }
        System.out.printf("Ejecutando actualización sobre la tabla: %s%n", table);
        StringBuilder updateQuery = new StringBuilder("UPDATE ").append(table).append(" SET ");
        for (int i = 0; i < columns.length; i++) {
            if (i == columns.length - 1) {
                updateQuery.append(columns[i]).append(" = ").append("? ");
            } else {
                updateQuery.append(columns[i]).append(" = ").append("?, ");
            }
        }
        updateQuery.append("WHERE ");
        for (int i = 0; i < filters.size(); i++) {
            if (i == filters.size() - 1) {
                updateQuery.append(filters.get(i));
            } else {
                updateQuery.append(filters.get(i)).append(" AND ");
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery.toString());) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.printf("Algo ha salido mal actualizando registros: %s%n", e);
        }
    }

    public static void simpleDelete(Connection connection, String table, List<String> filters) {
        if (connection == null) {
            throw new IllegalArgumentException("Se requiere una conexión activa para realizar la consulta");
        }
        if (table == null || table.isEmpty()) {
            throw new IllegalArgumentException("La tabla en la que insertarán los datos no puede ser nula");
        }
        if (filters == null || filters.isEmpty()) {
            throw new IllegalArgumentException("Se requiere algún filtro para realizar una correcta actualización");
        }
        System.out.printf("Ejecutando eliminación de registros en la tabla: %s%n", table);
        StringBuilder deleteQuery = new StringBuilder("DELETE FROM ").append(table).append(" WHERE ");
        for (int i = 0; i < filters.size(); i++) {
            if (i == filters.size() - 1) {
                deleteQuery.append(filters.get(i));
            } else {
                deleteQuery.append(filters.get(i)).append(" AND ");
            }
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery.toString());) {
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.out.printf("Algo ha salido mal eliminando el registro: %s%n", e);
        }
    }
}
