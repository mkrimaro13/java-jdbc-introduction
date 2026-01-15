package jdbc.introduction.model;

import lombok.*;

import java.lang.reflect.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private int id;
    private String firstName;
    private String paSurname;
    private String maSurname;
    private String email;
    private float salary;

    public String toString() {
        // Obtener campos de la clase
        Field[] fields = this.getClass().getDeclaredFields();
        int fieldsCounts = fields.length;
        // Datos a imprimir
        String[] header = new String[fieldsCounts];
        String[] row = new String[fieldsCounts];
        // Calcular anchos
        int[] widths = new int[fieldsCounts];
        // Obtener nombres y valores de campos
        for (int i = 0; i < fieldsCounts; i++) {
            fields[i].setAccessible(true);
            header[i] = fields[i].getName();

            try {
                row[i] = fields[i].get(this).toString();
            } catch (Exception e) {
                System.out.printf("Algo ha salido mal convirtiendo la clase a texto: %s%n", e);
                row[i] = "NO DATA";
            }

            widths[i] = Math.max(header[i].length(), row[i].length());
        }
        // Crear cadena de texto que se imprimirá
        StringBuilder result = new StringBuilder();
        String divider = buildDivider(widths);

        result.append(divider);
        result.append(buildRow(header, widths));
        result.append(divider);
        result.append(buildRow(row, widths));
        result.append(divider);
        return result.toString();
    }

    private String buildDivider(int[] widths) {
        StringBuilder divider = new StringBuilder();
        for (int width : widths) {
            divider.append("*").append("-".repeat(width + 2));
        }
        return divider.append("*\n").toString();
    }

    private String buildRow(String[] data, int[] widths) {
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            row.append("| ").append(String.format("%-" + widths[i] + "s", data[i])).append(" ");
        }
        return row.append("|\n").toString();
    }
}
