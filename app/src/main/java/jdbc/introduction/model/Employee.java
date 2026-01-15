package jdbc.introduction.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {
    private int id;
    private String firstName;
    private String paSurname;
    private String maSurname;
    private String email;
    private float salary;
}
