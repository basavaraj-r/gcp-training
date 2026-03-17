CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

INSERT INTO customers(name, email) VALUES ('Srini', 'srini@example.com');
INSERT INTO customers(name, email) VALUES ('Rama', 'rama@example.com');
INSERT INTO customers(name, email) VALUES ('Chinnu', 'chinnu@example.com');
INSERT INTO customers(name, email) VALUES ('Mahesh', 'mahesh@example.com');
INSERT INTO customers(name, email) VALUES ('Padma', 'padma@example.com');