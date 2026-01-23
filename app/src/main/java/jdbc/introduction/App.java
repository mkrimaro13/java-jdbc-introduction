package jdbc.introduction;

import java.util.List;

import jakarta.persistence.EntityManager;
import jdbc.introduction.entity.Employee;
import jdbc.introduction.util.UtilEntity;

public class App {

    public static void main(String[] args) {
        EntityManager em = UtilEntity.getEntityManager();
        try {

            // Consultar 1 registro
            Employee emp = em.find(Employee.class, 3);
            System.out.println(emp);

            // // Insertar 1 registro
            // Employee newEmployee = Employee.builder()
            //         .firstName("Maria")
            //         .paSurname("Adelaida")
            //         .maSurname("Luna")
            //         .salary(7900f)
            //         .email("mal@mal")
            //         .curp("AWQOPES12398QWEI82")
            //         .build();

            // em.getTransaction().begin();
            // em.persist(newEmployee);
            // em.getTransaction().commit();

            // Obtener todos los registros
            List<Employee> employees = em
                    .createQuery(String.format("select e from %s e", Employee.class.getName()), Employee.class)
                    .getResultList();
            employees.forEach(System.out::print);

        } finally {
            em.close();
        }
    }

}
