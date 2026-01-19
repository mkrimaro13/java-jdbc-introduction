package jdbc.introduction.util;

import jdbc.introduction.JDBCUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    public static void printRow(String[] rows, int[] widths) {
        JDBCUtils.printRow(rows, widths);
    }

    public static <T> void printResultsList(List<T> data, Class<T> type) throws Exception {
        if (data == null || data.isEmpty()) {
            throw new Exception("No hay información para imprimir");
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
        for (T datum : data) {
            String[] row = new String[columnCount];
            for (int j = 0; j < columnCount; j++) {
                row[j] = fields[j].get(datum) != null ? fields[j].get(datum).toString() : "NO DATA" ;
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
