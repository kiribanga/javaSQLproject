import com.mysql.jdbc.Driver;

import java.sql.*;
import java.util.Objects;
import java.util.regex.*;

public class sqlApp {
    private static Connection connection;
    private static Statement statement;

    private static String tableName = "People";
    private static String[] tableColumns = {"FullName", "BirthDate", "Sex"};

    public static void main(String[] args) {
        {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaProject", "javaReader", "java0812");
                statement = connection.createStatement();
                switch (args[0]) {
                    case "1" -> createTable();
                    case "2" -> addField(args);
                    case "3" -> showUniquePeople();
                    case "4" -> createMillionPeople();
                    case "5" -> showFPeople();
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createTable() throws SQLException {
        System.out.println("Creating table in database");
        String query = "CREATE TABLE " + tableName + " (" + tableColumns[0] + " VARCHAR(3) NOT NULL, " + tableColumns[1] + " DATE NOT NULL, " + tableColumns[2] + " CHAR NOT NULL);";
        try {
            statement.executeUpdate(query);
            System.out.println("Table " + tableName + " created");
        } catch (SQLException e) {
            statement.close();
            connection.close();
            e.printStackTrace();
        }
    }

    private static void addField(String[] args) throws SQLException {
        System.out.println("Adding new row to table People");
        if (args.length < 4)
            System.out.println("too low arguments");
        else {
            boolean error = false;
            if (args[1].length() != 3) {
                System.out.println("wrong full name format. please use 'FIO'");
                error = true;
            }
            if (!(Objects.equals(args[3], "m") || Objects.equals(args[3], "f"))) {
                System.out.println("wrong sex format. please use one letter");
                error = true;
            }
            int day = 0, month = 0, year = 0;
            if (args[2].length() == 10) {
                if (args[2].charAt(2) == '.' && args[2].charAt(5) == '.' && (args[2].charAt(0) >= '0' && args[2].charAt(0) <= '9') && (args[2].charAt(1) >= '0' && args[2].charAt(1) <= '9') && (args[2].charAt(3) >= '0' && args[2].charAt(3) <= '9') && (args[2].charAt(4) >= '0' && args[2].charAt(4) <= '9') && (args[2].charAt(6) >= '0' && args[2].charAt(6) <= '9') && (args[2].charAt(7) >= '0' && args[2].charAt(7) <= '9') && (args[2].charAt(8) >= '0' && args[2].charAt(8) <= '9') && (args[2].charAt(9) >= '0' && args[2].charAt(9) <= '9')) {
                    day = 10 * (args[2].charAt(0) - '0') + args[2].charAt(1) - '0';
                    month = 10 * (args[2].charAt(3) - '0') + args[2].charAt(4) - '0';
                    year = 1000 * (args[2].charAt(6) - '0') + 100 * (args[2].charAt(7) - '0') + 10 * (args[2].charAt(8) - '0') + args[2].charAt(9) - '0';
                    if (day * month * year == 0 || day > 31 || month > 12 || year < 1900 || year > 2021)
                        error = true;
                }
            } else {
                System.out.println("wrong date format. please use 'dd.mm.yyyy'");
                error = true;
            }

            if (!error)
                addField(args[1], new Date(year - 1900, month - 1, day), args[3].charAt(0));
        }
    }

    private static void addField(String fullName, Date birthDate, char sex) throws SQLException {
        String query = "INSERT " + tableName + " (" + tableColumns[0] + ", " + tableColumns[1] + ", " + tableColumns[2] + ")";
        query += "VALUES ('" + fullName + "', '" + birthDate + "', '" + sex + "');";
        try {
            statement.executeUpdate(query);
            System.out.println("Row added");
        } catch (SQLException e) {
            statement.close();
            connection.close();
            e.printStackTrace();
        }
    }

    private static void showUniquePeople() throws SQLException {
        String query = "SELECT DISTINCT " + tableColumns[0] + ", " + tableColumns[1] + " FROM " + tableName;
        query += " ORDER BY " + tableColumns[0] + ", " + tableColumns[1] + ";";
        try {
            System.out.println("Result of unique people from table");
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                for (int i = 0; i < 2; i++) {
                    System.out.print(result.getString(tableColumns[i]) + ' ');
                }
                System.out.println();
            }
        } catch (SQLException e) {
            statement.close();
            connection.close();
            e.printStackTrace();
        }
    }

    private static void createMillionPeople() {
        System.out.println("start process 4");
    }

    private static void showFPeople() throws SQLException {
        String query = "SELECT DISTINCT " + tableColumns[0] + ", " + tableColumns[1] + " FROM " + tableName;
        query += " WHERE LEFT(" + tableColumns[0] + ", 1) = 'F' AND " + tableColumns[2] + " = 'm';";
        try {
            System.out.println("Result of unique people from table");
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                for (int i = 0; i < 2; i++) {
                    System.out.print(result.getString(tableColumns[i]) + ' ');
                }
                System.out.println();
            }
        } catch (SQLException e) {
            statement.close();
            connection.close();
            e.printStackTrace();
        }
    }
}
