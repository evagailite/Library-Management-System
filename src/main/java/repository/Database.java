package repository;

import types.Genre;
import types.Status;
import types.UserType;

import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class Database {
    private static Scanner scanner = new Scanner(System.in);
    private Connection connection;
    // JDBC driver name and database URL
    private static final String DB_URL = "jdbc:h2:D:\\AccentureBootcamp2021\\projects\\library";
    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    //1st table
    private static final String TABLE_USERS = "users";

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "first_name";
    private static final String EMAIL = "email";
    private static final String LIBRARY_CARD_NUMBER = "library_card_number";
    private static final String USER_TYPE = "user_type";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
            USERNAME + " VARCHAR(50) NOT NULL, " +
            PASSWORD + " VARCHAR(50) NOT NULL, " +
            FIRST_NAME + " VARCHAR(50) NOT NULL, " +
            EMAIL + " VARCHAR(50) NOT NULL, " +
            LIBRARY_CARD_NUMBER + " VARCHAR(50), " +
            USER_TYPE + " VARCHAR(50) NOT NULL, " +
            "CONSTRAINT pk_username PRIMARY KEY (" + USERNAME + ")" +
            ");";
    //2nd table
    private static final String TABLE_BOOKS = "books";

    private static final String BOOKS_ID = "id";
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String GENRE = "genre";
    private static final String ISBN = "isbn";
    private static final String PAGES = "pages";
    private static final String STATUS = "status";

    private static final String CREATE_TABLE_BOOKS = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKS + " (" +
            BOOKS_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            AUTHOR + " VARCHAR(100) NOT NULL, " +
            TITLE + " VARCHAR(100) NOT NULL, " +
            GENRE + " VARCHAR(100) NOT NULL, " +
            ISBN + " VARCHAR(100) NOT NULL, " +
            PAGES + " INTEGER NOT NULL, " +
            STATUS + " VARCHAR(100) NOT NULL" +
            ");";
    //3rd table
    private static final String TABLE_LOANS = "loans";

    private static final String LOANS_ID = "id";
    private static final String BOOK = "book";
    private static final String ISSUED_DATE = "issued_date";
    private static final String DUE_DATE = "due_date";
    private static final String RETURN_DATE = "return_date";

    private static final String CREATE_TABLE_LOANS = "CREATE TABLE IF NOT EXISTS " + TABLE_LOANS + " (" +
            LOANS_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
            BOOK + " VARCHAR(100) NOT NULL REFERENCES " + TABLE_BOOKS + "(" + BOOKS_ID + ")," +
            ISSUED_DATE + " TIMESTAMP WITHOUT TIME ZONE NOT NULL," +
            DUE_DATE + " TIMESTAMP WITHOUT TIME ZONE NOT NULL, " +
            USERNAME + " VARCHAR(100) NOT NULL," +
            STATUS + " VARCHAR(100) NOT NULL," +
            RETURN_DATE + " TIMESTAMP WITHOUT TIME ZONE NOT NULL " +
            ");";

    public boolean open() {
        try {
            connection = getConnection();
            prepareDatabase(connection);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection " + e.getMessage());
        }
    }

    private static void prepareDatabase(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_USERS);
            statement.executeUpdate(CREATE_TABLE_BOOKS);
            statement.executeUpdate(CREATE_TABLE_LOANS);
        }
    }

    private static final String ADD_BOOK = "INSERT INTO " + TABLE_BOOKS + "(" +
            AUTHOR + ", " + TITLE + ", " + GENRE + ", " +
            ISBN + ", " + PAGES + ", " + STATUS + ")" + " VALUES (?, ?, ?, ?, ?, ? )";

    public void addBook(String author, String title, Genre genre, String isbn, int pages, Status status) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_BOOK)) {
                preparedStatement.setString(1, author);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, String.valueOf(genre));
                preparedStatement.setString(4, isbn);
                preparedStatement.setInt(5, pages);
                preparedStatement.setString(6, String.valueOf(status));
                preparedStatement.executeUpdate();
                System.out.println("\n" + title + " successfully added to the library!");
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String ADD_USER_READER = "INSERT INTO " + TABLE_USERS + "(" +
            USERNAME + ", " + PASSWORD + ", " + FIRST_NAME + ", " +
            EMAIL + ", " + LIBRARY_CARD_NUMBER + ", " + USER_TYPE + ")" + " VALUES (?, ?, ?, ?, ?, ? )";

    public void createUseReader(String username, String password, String firstName,
                                String email, String libraryCardNumber, UserType userType) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_READER)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, firstName);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, libraryCardNumber);
                preparedStatement.setString(6, String.valueOf(userType));
                preparedStatement.executeUpdate();
                System.out.println("\n" + username + " account successfully created!");
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String ADD_LOAN = "INSERT INTO " + TABLE_LOANS + "(" +
            BOOK + ", " + ISSUED_DATE + ", " +
            DUE_DATE + ", " + USERNAME + ", " + STATUS + "," +
            RETURN_DATE + ")" + " VALUES (?, ?, ?, ?, ?, ?)";

    public void createLoans(int book, String issuedDate, String dueDate,
                            String username, Status status, String returnDate) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_LOAN)) {
                preparedStatement.setInt(1, book);
                preparedStatement.setString(2, issuedDate);
                preparedStatement.setString(3, dueDate);
                preparedStatement.setString(4, username);
                preparedStatement.setString(5, String.valueOf(status));
                preparedStatement.setString(6, returnDate);

                preparedStatement.executeUpdate();
                // System.out.println("\nBook successfully checked out till " + dueDate + "!");
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    public static final String CHECK_FOR_PASSWORD = "SELECT " + PASSWORD + " FROM " + TABLE_USERS + " WHERE " +
            PASSWORD + " =?";

    public String checkPassword(String password) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_PASSWORD)) {
                preparedStatement.setString(1, password);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        password = rs.getString(PASSWORD);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return password;
    }

    public static final String CHECK_FOR_USERNAME = "SELECT " + USERNAME + " FROM " + TABLE_USERS + " WHERE " +
            USERNAME + " =?";

    public String checkUsername(String username) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_USERNAME)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString(USERNAME);
                        return username;
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return null;
    }

    public static final String CHECK_FOR_USERTYPE = "SELECT " + USER_TYPE + " FROM " + TABLE_USERS + " WHERE " +
            USERNAME + " =? AND " + PASSWORD + "=?";

    public String checkUserType(String username, String password) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FOR_USERTYPE)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        String userType = rs.getString(USER_TYPE);
                        return userType;
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return null;
    }

    private static final String DELETE_ITEM = "DELETE FROM " + TABLE_BOOKS + " WHERE " + BOOKS_ID + "=?";

    public void removeItem(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DELETE_ITEM)) {
                statement.setInt(1, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully deleted one row");
//                } else if (update == 0) {
//                    System.out.println("No changes have been made. Entered ID not found!");
//                }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    private static final String UPDATE_PAGES = "UPDATE " + TABLE_BOOKS + " SET " + PAGES + " = ? WHERE "
            + BOOKS_ID + "=?";

    public void editPages(int pages, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_PAGES)) {
                statement.setInt(1, pages);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
//                    System.out.println("Successfully updated one row");
                } else if (update == 0) {
//                    System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_ISBN = "UPDATE " + TABLE_BOOKS + " SET " + ISBN + " = ? WHERE "
            + BOOKS_ID + "=?";

    public void editIsbn(int isbn, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_ISBN)) {
                statement.setInt(1, isbn);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    //  System.out.println("Successfully updated one row");
                } else if (update == 0) {
                    //  System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    private static final String UPDATE_GENRE = "UPDATE " + TABLE_BOOKS + " SET " + GENRE + " = ? WHERE "
            + BOOKS_ID + "=?";

    public void editGenre(Genre genre, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_GENRE)) {
                statement.setString(1, String.valueOf(genre));
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully updated one row");
                }
//                } else if (update == 0) {
//                    System.out.println("No changes have been made. Entered ID not found!");
//                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_STATUS = "UPDATE " + TABLE_BOOKS + " SET " + STATUS + " = ? WHERE "
            + BOOKS_ID + "=?";

    public void editStatus(Status status, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS)) {
                statement.setString(1, String.valueOf(status));
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    //   System.out.println("Successfully updated one row");
                }
//                } else if (update == 0) {
//                    System.out.println("No changes have been made. Entered ID not found!");
//                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_AUTHOR = "UPDATE " + TABLE_BOOKS + " SET " + AUTHOR + " = ? WHERE "
            + BOOKS_ID + "=?";

    public void editAuthor(String author, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_AUTHOR)) {
                statement.setString(1, author);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully updated one row");
                }
//                } else if (update == 0) {
//                    System.out.println("No changes have been made. Entered ID not found!");
//                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_TITLE = "UPDATE " + TABLE_BOOKS + " SET " + TITLE + " = ? WHERE "
            + BOOKS_ID + "=?";

    public void editTitle(String title, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_TITLE)) {
                statement.setString(1, title);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    System.out.println("Successfully updated one row");
                }
//                } else if (update == 0) {
//                    System.out.println("No changes have been made. Entered ID not found!");
//                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_USERS = "SELECT * FROM " + TABLE_USERS + ";";

    public void displayUsers() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_USERS);
                try (ResultSet rs = statement.executeQuery(DISPLAY_USERS)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 4) {
                            System.out.printf("%-20s", metaData.getColumnName(i));
                        } else if (i == 5) {
                            System.out.printf("%-22s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-15s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 4) {
                                System.out.printf("%-20s", rs.getString(i));
                            } else if (i == 5) {
                                System.out.printf("%-22s", rs.getString(i));
                            } else {
                                System.out.printf("%-15s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_BOOKS = "SELECT * FROM " + TABLE_BOOKS + ";";

    public void displayBooks() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_BOOKS);
                try (ResultSet rs = statement.executeQuery(DISPLAY_BOOKS)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 1) {
                            System.out.printf("%-5s", metaData.getColumnName(i));
                        } else if (i == 2) {
                            System.out.printf("%-22s", metaData.getColumnName(i));
                        } else if (i == 3) {
                            System.out.printf("%-40s", metaData.getColumnName(i));
                        } else if (i == 5) {
                            System.out.printf("%-15s", metaData.getColumnName(i));
                        } else if (i == 6) {
                            System.out.printf("%-8s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-20s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 1) {
                                System.out.printf("%-5s", rs.getString(i));
                            } else if (i == 2) {
                                System.out.printf("%-22s", rs.getString(i));
                            } else if (i == 3) {
                                System.out.printf("%-40s", rs.getString(i));
                            } else if (i == 5) {
                                System.out.printf("%-15s", rs.getString(i));
                            } else if (i == 6) {
                                System.out.printf("%-8s", rs.getString(i));
                            } else {
                                System.out.printf("%-20s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_AVAILABLE_BOOKS = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + STATUS + "=?;";

    public void displayAvailableBooks(Status status) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(DISPLAY_AVAILABLE_BOOKS)) {
                statement.setString(1, String.valueOf(status));

                try (ResultSet rs = statement.executeQuery()) {

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 1) {
                            System.out.printf("%-5s", metaData.getColumnName(i));
                        } else if (i == 2) {
                            System.out.printf("%-22s", metaData.getColumnName(i));
                        } else if (i == 3) {
                            System.out.printf("%-40s", metaData.getColumnName(i));
                        } else if (i == 5) {
                            System.out.printf("%-15s", metaData.getColumnName(i));
                        } else if (i == 6) {
                            System.out.printf("%-8s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-20s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 1) {
                                System.out.printf("%-5s", rs.getString(i));
                            } else if (i == 2) {
                                System.out.printf("%-22s", rs.getString(i));
                            } else if (i == 3) {
                                System.out.printf("%-40s", rs.getString(i));
                            } else if (i == 5) {
                                System.out.printf("%-15s", rs.getString(i));
                            } else if (i == 6) {
                                System.out.printf("%-8s", rs.getString(i));
                            } else {
                                System.out.printf("%-20s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static final String DISPLAY_LOANS = "SELECT * FROM " + TABLE_LOANS + ";";

    public void displayLoans() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(DISPLAY_LOANS);
                try (ResultSet rs = statement.executeQuery(DISPLAY_LOANS)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 1) {
                            System.out.printf("%-5s", metaData.getColumnName(i));
                        } else if (i == 2) {
                            System.out.printf("%-5s", metaData.getColumnName(i));
                        } else if (i == 6) {
                            System.out.printf("%-18s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-15s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 1) {
                                System.out.printf("%-5s", rs.getString(i));
                            } else if (i == 2) {
                                System.out.printf("%-5s", rs.getString(i));
                            } else if (i == 6) {
                                System.out.printf("%-18s", rs.getString(i));
                            } else {
                                System.out.printf("%-15s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static final String COUNT_ITEMS = "SELECT COUNT (DISTINCT " + BOOKS_ID + ") FROM " + TABLE_BOOKS + ";";

    public void printCount() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeQuery(COUNT_ITEMS);
                try (ResultSet rs = statement.executeQuery(COUNT_ITEMS)) {
                    while (rs.next()) {
                        String title = rs.getString(1);
                        System.out.println("You have " + title + " books in the Library Catalog");
                        System.out.println(" ");
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String RESET_ID = "ALTER TABLE " + TABLE_BOOKS + " DROP " + BOOKS_ID + ";";

    public void resetId() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(RESET_ID);
            }

        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String RESET = "ALTER TABLE " + TABLE_BOOKS + " ADD " + BOOKS_ID +
            " int UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;";

    public void reset() {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(RESET);
            }

        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    public static final String CHECK_ID = "SELECT " + BOOKS_ID + " FROM " +
            TABLE_BOOKS + " WHERE " + BOOKS_ID + "=?";

    public int checkIfExists(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ID)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt(BOOKS_ID);
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return id;
    }

    public static final String CHECK_BOOK_LOAN_ID = "SELECT " + LOANS_ID + " FROM " +
            TABLE_LOANS + " WHERE " + LOANS_ID + "=?";

    public int checkIfExistsInLoan(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_BOOK_LOAN_ID)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt(LOANS_ID);
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return id;
    }


    public static final String GET_BOOK_AUTHOR = "SELECT " + AUTHOR + " FROM " +
            TABLE_BOOKS + " WHERE " + BOOKS_ID + "=?";

    public String getBookAuthor(int id) {
        String author = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOK_AUTHOR)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        author = rs.getString(AUTHOR);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return author;
    }

    public static final String GET_LOAN_DUE_DATE = "SELECT " + DUE_DATE + " FROM " +
            TABLE_LOANS + " WHERE " + LOANS_ID + "=?";

    public Date getBookDueDate(int id) {
        java.sql.Date dueDate = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LOAN_DUE_DATE)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        dueDate = rs.getDate(DUE_DATE);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return dueDate;
    }

    public static final String GET_BOOK_TITLE = "SELECT " + TITLE + " FROM " +
            TABLE_BOOKS + " WHERE " + BOOKS_ID + "=?";

    public String getBookTitle(int id) {
        String title = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOK_TITLE)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        title = rs.getString(TITLE);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return title;
    }

    public static final String GET_BOOK_ISBN = "SELECT " + ISBN + " FROM " +
            TABLE_BOOKS + " WHERE " + BOOKS_ID + "=?";

    public String getBookIsbn(int id) {
        String isbn = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOK_ISBN)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        isbn = rs.getString(ISBN);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return isbn;
    }

    public static final String GET_PRODUCT_PAGES = "SELECT " + PAGES + " FROM " +
            TABLE_BOOKS + " WHERE " + BOOKS_ID + "=?";

    public int getBookPages(int id) {
        int pages = 0;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_PAGES)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        pages = rs.getInt(PAGES);
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return pages;
    }

    public static final String GET_PRODUCT_STATUS = "SELECT " + STATUS + " FROM " +
            TABLE_BOOKS + " WHERE " + BOOKS_ID + "=?";

    public String getBookStatus(int id) {
        String status = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_STATUS)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        status = rs.getString(STATUS);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return status;
    }

    public static final String GET_LOAN_STATUS = "SELECT " + STATUS + " FROM " +
            TABLE_LOANS + " WHERE " + LOANS_ID + "=?";

    public String getLoanStatus(int id) {
        String status = null;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LOAN_STATUS)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        status = rs.getString(STATUS);
                    } else {
                        return null;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return status;
    }

    private static final String UPDATE_LOAN_STATUS = "UPDATE " + TABLE_LOANS + " SET " + STATUS + " = ? WHERE "
            + LOANS_ID + "=?";

    public void updateLoanStatus(String status, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_LOAN_STATUS)) {
                statement.setString(1, status);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    //  System.out.println("Successfully updated one row");
                } else if (update == 0) {
                      System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_DUE_DATE = "UPDATE " + TABLE_LOANS + " SET " + DUE_DATE + " = ? WHERE "
            + LOANS_ID + "=?";

    public void updateDueDate(String dueDate, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_DUE_DATE)) {
                statement.setString(1, dueDate);
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    //  System.out.println("Successfully updated one row");
                } else if (update == 0) {
                     System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    private static final String UPDATE_BOOK_STATUS_AFTER_RETURN = "UPDATE " + TABLE_LOANS + " SET " + STATUS +
            " = ?, " + RETURN_DATE + " =? WHERE "
            + LOANS_ID + "=?";

    public void updateBookReturnStatus(Status status, String returnDate, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK_STATUS_AFTER_RETURN)) {
                statement.setString(1, String.valueOf(status));
                statement.setString(2, returnDate);
                statement.setInt(3, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    //  System.out.println("Successfully updated one row");
                } else if (update == 0) {
                     System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    private static final String UPDATE_BOOK_STATUS = "UPDATE " + TABLE_BOOKS + " SET " + STATUS + " = ? WHERE "
            + BOOKS_ID + "=?";

    public void updateBookStatus(Status status, int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK_STATUS)) {
                statement.setString(1, String.valueOf(status));
                statement.setInt(2, id);
                int update = statement.executeUpdate();
                if (update == 1) {
                    //  System.out.println("Successfully updated one row");
                } else if (update == 0) {
                   System.out.println("No changes have been made. Entered ID not found!");
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }


    public static final String DISPLAY_USER_LOANS = "SELECT " + TABLE_LOANS + "." + LOANS_ID + ", " +
            TABLE_BOOKS + "." + AUTHOR + ", " + TABLE_BOOKS + "." + TITLE + ", " + TABLE_LOANS + "." + ISSUED_DATE + ", " +
            TABLE_LOANS + "." + DUE_DATE + ", " + TABLE_LOANS + "." + STATUS + ", " + TABLE_LOANS + "."
            + RETURN_DATE + " FROM " + TABLE_LOANS +
            " INNER JOIN " + TABLE_BOOKS + " ON " + TABLE_LOANS + "." + BOOK + "=" + TABLE_BOOKS + "." + BOOKS_ID +
            " WHERE " + TABLE_LOANS + "." + USERNAME + "=? AND " + TABLE_LOANS + "." + STATUS + " =?;";

    public void displayUserBorrowedBooks(String username, Status status) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DISPLAY_USER_LOANS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, String.valueOf(status));
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 1) {
                            System.out.printf("%-5s", metaData.getColumnName(i));
                        } else if (i == 3) {
                            System.out.printf("%-40s", metaData.getColumnName(i));
                        } else if (i == 6) {
                            System.out.printf("%-15s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-27s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 1) {
                                System.out.printf("%-5s", rs.getString(i));
                            } else if (i == 3) {
                                System.out.printf("%-40s", rs.getString(i));
                            } else if (i == 6) {
                                System.out.printf("%-15s", rs.getString(i));
                            } else {
                                System.out.printf("%-27s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }

    }

    public static final String DISPLAY_USER_LOANS_HISTORY = "SELECT " + TABLE_LOANS + "." + LOANS_ID + ", " +
            TABLE_BOOKS + "." + AUTHOR + ", " + TABLE_BOOKS + "." + TITLE + ", " + TABLE_LOANS + "." + ISSUED_DATE + ", " +
            TABLE_LOANS + "." + DUE_DATE + ", " + TABLE_LOANS + "." + STATUS + ", " + TABLE_LOANS + "."
            + RETURN_DATE + " FROM " + TABLE_LOANS +
            " INNER JOIN " + TABLE_BOOKS + " ON " + TABLE_LOANS + "." + BOOK + "=" + TABLE_BOOKS + "." + BOOKS_ID +
            " WHERE " + TABLE_LOANS + "." + USERNAME + "=?;";

    public void displayUserBorrowedBooksHistory(String username) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DISPLAY_USER_LOANS_HISTORY)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 1) {
                            System.out.printf("%-5s", metaData.getColumnName(i));
                        } else if (i == 3) {
                            System.out.printf("%-40s", metaData.getColumnName(i));
                        } else if (i == 6) {
                            System.out.printf("%-20s", metaData.getColumnName(i));
                        } else {
                            System.out.printf("%-27s", metaData.getColumnName(i));
                        }
                    }
                    System.out.println(" ");
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            if (i == 1) {
                                System.out.printf("%-5s", rs.getString(i));
                            } else if (i == 3) {
                                System.out.printf("%-40s", rs.getString(i));
                            } else if (i == 6) {
                                System.out.printf("%-20s", rs.getString(i));
                            } else {
                                System.out.printf("%-27s", rs.getString(i));
                            }
                        }
                        System.out.println();
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }

    }

    public static final String GET_BOOKS_ID = "SELECT " + TABLE_BOOKS + "." + BOOKS_ID + " FROM " +
            TABLE_LOANS + " INNER JOIN " + TABLE_BOOKS + " ON " +
            TABLE_LOANS + "." + BOOK + "=" + TABLE_BOOKS + "." + BOOKS_ID +
            " WHERE " + TABLE_LOANS + "." + LOANS_ID + "=?";

    public int selectBooksId(int id) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOKS_ID)) {
                preparedStatement.setInt(1, id);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt(BOOKS_ID);
                    } else {
                        return 0;
                    }
                }
            } catch (SQLException throwables) {
                System.out.println("Something went wrong " + throwables.getMessage());
                throwables.printStackTrace();
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return id;
    }

    private static final String COUNT_ISSUED_BOOKS = "SELECT COUNT (DISTINCT " + LOANS_ID + ") FROM "
            + TABLE_LOANS + " WHERE " + USERNAME + "=? AND " + STATUS + "=? ;";

    public int countIssuedBooks(String username, Status status) {
        int order = 0;
        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_ISSUED_BOOKS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, String.valueOf(status));
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        order = rs.getInt(1);
                        return order;
                    }
                }
            }
        } catch (SQLException throwables) {
            System.out.println("Something went wrong " + throwables.getMessage());
            throwables.printStackTrace();
        }
        return 0;
    }


}
