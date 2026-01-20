-- Se crea una base de datos llamada "project" si no existe.
CREATE
DATABASE IF not EXISTS project;
-- Se selecciona la base de datos "project" para utilizarla.
USE
project;
-- Si existe una tabla llamada "employees", se elimina.
DROP TABLE IF EXISTS employees;
-- Se crea la tabla "employees" con las columnas id, first_name, pa_surname, ma_surname, email y salary, y se establece id como clave primaria.
CREATE TABLE `employees`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `first_name` varchar(64)    DEFAULT NULL,
    `pa_surname` varchar(64)    DEFAULT NULL,
    `ma_surname` varchar(64)    DEFAULT NULL,
    `email`      varchar(64)    DEFAULT NULL,
    `salary`     DECIMAL(10, 2) DEFAULT NULL,
    `curp`       varchar(18) UNIQUE,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Se insertan cinco filas en la tabla "employees" con diferentes valores para cada columna.
INSERT INTO `employees` (`first_name`, `pa_surname`, `ma_surname`, `email`, `salary`)
VALUES ('John', 'Doe', 'Smith', 'john.doe@example.com', 50000.00);
INSERT INTO `employees` (`first_name`, `pa_surname`, `ma_surname`, `email`, `salary`)
VALUES ('Jane', 'Smith', 'Johnson', 'jane.smith@example.com', 60000.00);
INSERT INTO `employees` (`first_name`, `pa_surname`, `ma_surname`, `email`, `salary`)
VALUES ('Michael', 'Johnson', 'Brown', 'michael.johnson@example.com', 55000.00);
INSERT INTO `employees` (`first_name`, `pa_surname`, `ma_surname`, `email`, `salary`)
VALUES ('Emily', 'Brown', 'Davis', 'emily.brown@example.com', 52000.00);
INSERT INTO `employees` (`first_name`, `pa_surname`, `ma_surname`, `email`, `salary`)
VALUES ('David', 'Davis', 'Wilson', 'david.davis@example.com', 58000.00);