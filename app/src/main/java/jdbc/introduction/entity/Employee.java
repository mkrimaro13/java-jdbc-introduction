package jdbc.introduction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.lang.reflect.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "employees", catalog = "project")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name", length = 64)
    private String firstName;
    @Column(name = "pa_surname", length = 64)
    private String paSurname;
    @Column(name = "ma_surname", length = 64)
    private String maSurname;
    @Column(length = 64)
    private String email;
    @Column(length = 18, unique = true)
    private String curp;
    @Column
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
                // System.out.printf("Algo ha salido mal convirtiendo la clase a texto: %s%n",
                // e);
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