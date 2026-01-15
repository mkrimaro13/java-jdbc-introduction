package jdbc.introduction.util;

import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jdbc.introduction.JDBCUtils.printRow;

public class DatabaseUtils<T> {
    public static <T> void printResultsList(List<T> data, Class<T> type) throws IllegalAccessException {
        if (data == null || data.isEmpty()) {
            System.out.println("No hay información para imprimir");
        }
        System.out.printf("Imprimiendo datos de la lista respecto a la clase indicada: %s%n", type.getSimpleName());

        Field[] fields = type.getDeclaredFields();
        int columnCount = fields.length;
        List<String[]> rows = new ArrayList<>();
        String[] header = new String[columnCount];
        int[] widths = new int[columnCount];
        for (int i = 0; i < columnCount; i++) {
            fields[i].setAccessible(true);
            header[i] = fields[i].getName();
            widths[i] = fields[i].getName().length();
        }
        for (int i = 0; i < data.size(); i++) {
            String[] row = new String[columnCount];
            T item = data.get(i);
            for (int j = 0; j < columnCount; j++) {
                row[j] = fields[j].get(item).toString();
                widths[j] = Math.max(widths[j], row[j].length());
            }
            rows.add(row);
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
    }
}
