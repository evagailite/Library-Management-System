package controller;

import entity.Users;
import repository.Database;
import types.UserType;

import java.util.Scanner;

public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private Library library;
    private Users users;
    private Database database;

    public Menu(Library library, Users users, Database database) {
        this.library = library;
        this.users = users;
        this.database = database;
    }

    public void welcomeMessage() {
        System.out.println("\n     ===================================");
        System.out.println("           WELCOME TO THE LIBRARY       ");
        System.out.println("     ===================================");
    }

    public void start() {
        getStartMenu();
        try {
            String userChoice = scanner.nextLine();
            Users activeUser = returnUserType(userChoice);

            if (activeUser == null) {
                invalidInputMessage();
                start();
            }

            library.setActiveUser(activeUser);
            showUserMenu(activeUser.getUserType());

        } catch (NumberFormatException e) {
            invalidInputMessage();
            start();
        }
    }

    public String login(UserType userType) {
        try {
            System.out.println("Please LOGIN");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if ((database.checkUsername(username).contains(username)) &&
                    (database.checkPassword(password).contains(password) &&
                            (database.checkUserType(username, password).contains(String.valueOf(userType))))) {
                return username;
            }
        } catch (Exception e) {
            System.out.println("Invalid username or password!!");
            handleException(userType);
        }
        return null;
    }

    private void handleException(UserType userType) {
        if (userType.equals(UserType.READER)) {
            handleSignIn(userType);
        } else {
            start();
        }
    }

    private void handleSignIn(UserType userType) {
        try {
            System.out.println("Press: ");
            System.out.println("\t1 - Sign in");
            System.out.println("\t2 - Exit to Main menu");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                createUser(userType);
            } else if (choice.equals("2")) {
                library.clearBookCart();
                start();
            } else {
                invalidInputMessage();
                start();
            }
        } catch (Exception e) {
            invalidInputMessage();
            handleSignIn(userType);
        }
    }

    private void handleAccess(UserType userType) {
        try {
            System.out.println("Press: ");
            System.out.println("\t1 - Log in");
            System.out.println("\t2 - Sign in");
            System.out.println("\t3 - Return to Main Menu");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                String username = login(userType);
                if (username != null) {
                    loginWelcomeMessage(username);
                    getReaderMenu();
                    String userChoice = scanner.nextLine();
                    handleMenuChoice(userType, userChoice, username);
                } else {
                    loginErrorMessage();
                    handleSignIn(userType);
                }
            } else if (choice.equals("2")) {
                createUser(userType);
            } else if (choice.equals("3")) {
                start();
            } else {
                invalidInputMessage();
                start();
            }
        } catch (Exception e) {
            invalidInputMessage();
            handleSignIn(userType);
        }
    }

    private void loginErrorMessage() {
        System.out.println("Invalid username or password!");
        System.out.println("Please try again!");
    }

    private void loginWelcomeMessage(String username) {
        System.out.println("\nWelcome " + username + " to the Library!");
    }

    public void createUser(UserType userType) {
        try {
            if (userType.equals(UserType.READER)) {
                System.out.println("\nPlease Sign-In");
                getUserData(userType);
                System.out.print("Enter library card number: ");
                String libraryCardNumber = scanner.nextLine();
                users.setLibraryCardNumber(libraryCardNumber);
                database.createUseReader(users.getUsername(), users.getPassword(), users.getName(),
                        users.getEmail(), users.getLibraryCardNumber(), userType);

                System.out.println("Please Log in!");
                start();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try again");
            createUser(userType);
        }
    }

    public void getUserData(UserType userType) {
        try {
            System.out.print("Enter username: ");
            users.setUsername(scanner.nextLine());
            if ((database.checkUsername(users.getUsername()) != null)) {
                System.out.println("Unfortunately username already taken");
                System.out.println("Please try again!");
                createUser(userType);
            } else {
                System.out.print("Enter password: ");
                users.setPassword(scanner.nextLine());
                System.out.print("Enter name: ");
                users.setName(scanner.nextLine());
                System.out.print("Enter email: ");
                users.setEmail(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println("Please try again!");
            createUser(userType);
        }
    }

    public void invalidInputMessage() {
        System.out.println("Invalid input! Please choose a number from the menu to proceed!");
    }

    private void getStartMenu() {
        System.out.println("\nPlease choose user type to proceed with LOG-IN: ");
        System.out.println("\t 1 - Administrator");
        System.out.println("\t 2 - Reader");
        System.out.println("\t 3 - Exit the Library");
    }

    private Users returnUserType(String userChoice) {
        switch (Integer.parseInt(userChoice)) {
            case 1:
                return new Users(UserType.ADMINISTRATOR);
            case 2:
                return new Users(UserType.READER);
            case 3:
                quit();
            default:
                break;
        }
        return null;
    }

    private void showUserMenu(UserType userType) {
        switch (userType) {
            case ADMINISTRATOR:
                String username = login(userType);
                handleAdministratorAccess(username, userType);
                break;
            case READER:
                handleAccess(userType);
                break;
            default:
                start();
                break;
        }
    }

    private void quit() {
        System.out.println("Application has stopped working!");
        System.exit(0);
    }

    private void getAdministratorMenu() {
        System.out.println("\nChoose the option to proceed: " +
                "\n\t 1. Add Book to the Catalog" +
                "\n\t 2. Remove Book from the Catalog" +
                "\n\t 3. Edit Book" +
                "\n\t 4. Display Books" +
                "\n\t 5. Display Loans" +
                "\n\t 6. Display Users" +
                "\n\t 7. Logout");
    }

    private void handleAdministratorAccess(String username, UserType userType) {
        if (username != null) {
            loginWelcomeMessage(username);
            getAdministratorMenu();
            String userChoice = scanner.nextLine();
            handleMenuChoice(userType, userChoice, username);
        } else {
            loginErrorMessage();
            start();
        }
    }

    private void handleMenuChoice(UserType userType, String userChoice, String username) {
        switch (userType) {
            case ADMINISTRATOR:
                handleAdministratorChoice(userChoice, username);
                break;
            case READER:
                handleReaderChoice(userChoice, username);
                break;
            default:
                start();
                break;
        }
    }

    private void handleAdministratorChoice(String userChoice, String username) {
        switch (userChoice) {
            case "1":
                library.addBook();
                break;
            case "2":
                library.removeBook();
                break;
            case "3":
                editBook(username);
                break;
            case "4":
                database.displayBooks();
                break;
            case "5":
                database.displayLoans();
                break;
            case "6":
                database.displayUsers();
                break;
            case "7":
                proceedExit();
                break;
            default:
                invalidInputMessage();
                getAdministratorMenu();
                userChoice = scanner.nextLine();
                handleAdministratorChoice(userChoice, username);
                break;
        }
        getAdministratorMenu();
        userChoice = scanner.nextLine();
        handleAdministratorChoice(userChoice, username);
    }

    private void editBook(String username) {
        getEditMenu();
        try {
            String choice = scanner.nextLine();
            handleEditMenu(choice, username);
        } catch (Exception e) {
        }
    }

    private void getEditMenu() {
        System.out.println("\nChoose field to edit: " +
                "\n\t 1. Author" +
                "\n\t 2. Title" +
                "\n\t 3. Genre" +
                "\n\t 4. ISBN" +
                "\n\t 5. Number of Pages" +
                "\n\t 6. Status" +
                "\n\t 7. Return to Main Menu");
    }

    private void handleEditMenu(String userChoice, String username) {
        switch (userChoice) {
            case "1":
                library.editBookAuthor();
                break;
            case "2":
                library.editBookTitle();
                break;
            case "3":
                library.editBookGenre();
                break;
            case "4":
                library.editBookIsbn();
                break;
            case "5":
                library.editBookPages();
                break;
            case "6":
                library.editBookStatus();
                break;
            case "7":
                getAdministratorMenu();
                userChoice = scanner.nextLine();
                handleAdministratorChoice(userChoice, username);
                break;
            default:
                invalidInputMessage();
                getAdministratorMenu();
                userChoice = scanner.nextLine();
                handleAdministratorChoice(userChoice, username);
                break;
        }
    }

    private void getReaderMenu() {
        System.out.println("\nChoose the option to proceed: " +
                "\n\t 1. Borrow Books" +
                "\n\t 2. Display Book Cart" +
                "\n\t 3. Remove Book from the Book Cart" +
                "\n\t 4. Clear Book Cart" +
                "\n\t 5. Checkout Books" +
                "\n\t 6. Extend Due Date" +
                "\n\t 7. Return Books" +
                "\n\t 8. Checkout History" +
                "\n\t 9. Logout");
    }

    private void handleReaderChoice(String userChoice, String username) {
        switch (userChoice) {
            case "1":
                library.borrowBooks(username);
                break;
            case "2":
                library.bookCart();
                break;
            case "3":
                library.removeBookFromBookCart();
                break;
            case "4":
                library.clearBookCart();
                break;
            case "5":
                library.checkOut(username);
                break;
            case "6":
                library.extendDueDate(username);
                break;
            case "7":
                library.returnBook(username);
                break;
            case "8":
                database.displayUserBorrowedBooksHistory(username);
                break;
            case "9":
                library.clearBookCart();
                proceedExit();
                break;
            default:
                invalidInputMessage();
                getReaderMenu();
                userChoice = scanner.nextLine();
                handleReaderChoice(userChoice, username);
                break;
        }
        getReaderMenu();
        userChoice = scanner.nextLine();
        handleReaderChoice(userChoice, username);
    }

    private void proceedExit() {
        try {
            System.out.println("Press: ");
            System.out.println("\t1 - To Exit");
            System.out.println("\t2 - Open the Main Menu");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                quit();
            } else if (choice.equals("2")) {
                start();
            } else {
                invalidInputMessage();
                proceedExit();
            }
        } catch (Exception exception) {
            invalidInputMessage();
            proceedExit();
        }
    }
}
