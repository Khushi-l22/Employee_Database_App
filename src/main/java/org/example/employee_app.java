package org.example;

import java.sql.*;
import java.util.Scanner;

public class employee_app {
    private static final String URL = "jdbc:mysql://localhost:3306/employee";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "khushi@12345"; // Change to your MySQL password

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            while (true) {
                System.out.println("\n=== Employee Database Menu ===");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> addEmployee(sc);
                    case 2 -> viewEmployees();
                    case 3 -> updateEmployee(sc);
                    case 4 -> deleteEmployee(sc);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        String name = sc.next();
        System.out.print("Enter department: ");
        String department = sc.next();
        System.out.print("Enter salary: ");
        double salary = sc.nextDouble();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, department);
            ps.setDouble(3, salary);
            int rows = ps.executeUpdate();
            System.out.println(rows + " employee(s) added.");
        }
    }

    private static void viewEmployees() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM employees")) {
            System.out.println("\nID\tName\tDepartment\tSalary");
            while (rs.next()) {
                System.out.printf("%d\t%s\t%s\t%.2f%n",
                        rs.getInt("e_id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        }
    }

    private static void updateEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to update: ");
        int e_id = sc.nextInt();
        System.out.print("Enter new name: ");
        String name = sc.next();
        System.out.print("Enter new department: ");
        String department = sc.next();
        System.out.print("Enter new salary: ");
        double salary = sc.nextDouble();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE employees SET name=?, department=?, salary=? WHERE e_id=?")) {
            ps.setString(1, name);
            ps.setString(2, department);
            ps.setDouble(3, salary);
            ps.setInt(4, e_id);
            int rows = ps.executeUpdate();
            System.out.println(rows + " employee(s) updated.");
        }
    }

    private static void deleteEmployee(Scanner sc) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int e_id = sc.nextInt();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM employees WHERE e_id=?")) {
            ps.setInt(1, e_id);
            int rows = ps.executeUpdate();
            System.out.println(rows + " employee(s) deleted.");
        }
    }

}
