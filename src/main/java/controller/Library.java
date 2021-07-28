package controller;

import entity.Book;
import entity.Users;
import repository.Database;
import types.Genre;
import types.Status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Library {
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss", Locale.ENGLISH);
    private Date currentDate = new Date();
    private Scanner scanner = new Scanner(System.in);
    private final ArrayList<Book> bookCart = new ArrayList<>();
    private Book book;
    private Users users;
    private Database database;

    public Library(Book book, Users users, Database database) {
        this.book = book;
        this.users = users;
        this.database = database;
    }

    public void setActiveUser(Users activeUsers) {
        this.users = users;
    }

    public Users getActiveUsers() {
        return users;
    }

    public void addBook() {
        try {
            System.out.print("Enter book author: ");
            book.setAuthor(scanner.nextLine());
            System.out.print("Enter book title: ");
            book.setTitle(scanner.nextLine());
            System.out.print("Enter genre from the list - ");
            System.out.print("\nEDUCATION, BIOGRAPHY, KIDS, HEALTH_WELL_BEING, ROMANCE," +
                    "CRIME_THRILLERS, SCI_FI_FANTASY:");
            book.setGenre(Genre.valueOf(scanner.nextLine().toUpperCase()));
            System.out.print("Enter book ISBN:");
            book.setISBN(scanner.nextLine());
            System.out.print("Enter number of pages: ");
            book.setPages(Integer.parseInt(scanner.nextLine()));
            System.out.print("Enter status from the list - ");
            System.out.print("\nAVAILABLE, MISSING, DAMAGED, DUE_DATE, BILLED: ");
            book.setStatus(Status.valueOf(scanner.nextLine().toUpperCase()));

            database.addBook(book.getAuthor(), book.getTitle(), book.getGenre(),
                    book.getISBN(), book.getPages(), book.getStatus());
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println("Please try to create book again!");
            addBook();
        }
    }

    public void removeBook() {
        try {
            display();
            System.out.print("Please enter book ID to remove from the Catalog: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = database.checkIfExists(id);
            if (test != 0) {
                database.removeItem(id);
                database.resetId();
                database.reset();
            } else {
                notExistingIDMessage(id);
                removeBook();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to remove the book again");
        }
    }

    public void notExistingIDMessage(int id) {
        System.out.println("Book with the ID " + id + " not found in the Catalog! Please try again!");
    }

    public void invalidInputMessage() {
        System.out.println("Invalid input!");
    }

    public void display() {
        database.printCount();
        database.displayBooks();
    }

    public void editBookAuthor() {
        try {
            display();
            System.out.print("Please enter book ID to edit author: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = database.checkIfExists(id);
            if (test != 0) {
                System.out.print("Enter new author: ");
                String author = scanner.nextLine();
                database.editAuthor(author, id);
            } else {
                notExistingIDMessage(id);
                editBookAuthor();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the book author again");
        }
    }

    public void editBookTitle() {
        try {
            display();
            System.out.print("Please enter book ID to edit title: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = database.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter a new product name: ");
                String name = scanner.nextLine();
                database.editTitle(name, id);
            } else {
                notExistingIDMessage(id);
                editBookTitle();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the book title again");
        }
    }

    public void editBookGenre() {
        try {
            display();
            System.out.println("Please enter book ID to edit genre:");
            int id = Integer.parseInt(scanner.nextLine());
            int test = database.checkIfExists(id);
            if (test != 0) {
                System.out.print("Enter new genre from the list - ");
                System.out.print("\nEDUCATION, BIOGRAPHY, KIDS, HEALTH_WELL_BEING, ROMANCE," +
                        "CRIME_THRILLERS, SCI_FI_FANTASY:");

                book.setGenre(Genre.valueOf(scanner.nextLine().toUpperCase()));
                database.editGenre(book.getGenre(), id);
            } else {
                notExistingIDMessage(id);
                editBookGenre();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the book genre again!");
        }
    }

    public void editBookStatus() {
        try {
            display();
            System.out.println("Please enter book ID to edit status:");
            int id = Integer.parseInt(scanner.nextLine());
            int test = database.checkIfExists(id);
            if (test != 0) {
                System.out.print("Enter new status from the list - ");
                System.out.print("\nAVAILABLE, MISSING, DAMAGED, DUE_DATE, BILLED: ");

                book.setStatus(Status.valueOf(scanner.nextLine().toUpperCase()));
                database.editStatus(book.getStatus(), id);
                System.out.println("Successfully updated one row");
            } else {
                notExistingIDMessage(id);
                editBookStatus();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the book status again!");
        }
    }

    public void editBookIsbn() {
        try {
            display();
            System.out.print("Please enter book ID to edit ISBN: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = database.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter a new book ISBN: ");
                int isbn = scanner.nextInt();
                scanner.nextLine();
                database.editIsbn(isbn, id);
                System.out.println("Successfully updated one row");
            } else {
                notExistingIDMessage(id);
                editBookIsbn();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the book isbn again");
        }
    }

    public void editBookPages() {
        try {
            display();
            System.out.print("Please enter book ID to edit page number: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = database.checkIfExists(id);
            if (test != 0) {
                System.out.print("Please enter a new page number: ");
                int pages = scanner.nextInt();
                scanner.nextLine();
                database.editPages(pages, id);
                System.out.println("Successfully updated one row");
            } else {
                notExistingIDMessage(id);
                editBookPages();
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to change the page number again");
        }
    }

    public void borrowBooks(String username) {
        System.out.println("Dear, " + username + " please be aware that Library policy includes ");
        System.out.println("a borrowing limit of 2 books at any point in time for each user.");
        System.out.println(" ");
        database.displayAvailableBooks(Status.AVAILABLE);
        try {
            System.out.println("Please enter book ID to insert into Books Cart: ");
            int id = Integer.parseInt(scanner.nextLine());
            int test = database.checkIfExists(id);
            if (test != 0) {
                String checkStatus = String.valueOf(database.getBookStatus(id));
                if (checkStatus.contains(String.valueOf(Status.AVAILABLE))) {
                    String author = database.getBookAuthor(id);
                    String title = database.getBookTitle(id);
                    String isbn = database.getBookIsbn(id);
                    int pages = database.getBookPages(id);

                    bookCart.add(new Book(id, author, title, isbn, pages));
                    System.out.println("\"" + author + " - " + title + "\"" + " successfully added to the Book Cart!");
                } else {
                    System.out.println("Unfortunately book isn't available!");
                }
            } else {
                invalidInputMessage();
                System.out.println("Please try to use Library Catalog again");
            }
        } catch (Exception e) {
            invalidInputMessage();
            System.out.println("Please try to use Library Catalog again");
        }
    }

    public void displayBookCart() {
        System.out.println(String.format("%-5s%-30s%-40s%-20s%-15s", "ID ", "AUTHOR",
                "TITLE", "ISBN", "PAGES"));
        for (Book book : bookCart) {
            System.out.println(String.format("%-5s%-30s%-40s%-20s%-15s", book.getId(), book.getAuthor(),
                    book.getTitle(), book.getISBN(), book.getPages()));
        }
    }

    public void bookCart() {
        try {
            if (bookCart.size() == 0) {
                System.out.println("Your Book Cart is empty! ");
            } else if (bookCart.size() == 1) {
                System.out.println("You have " + bookCart.size() + " book in your Book Cart!");
                displayBookCart();
            } else {
                System.out.println("You have " + bookCart.size() + " books in your Book Cart!");
                displayBookCart();
            }
        } catch (Exception e) {
            System.out.println("Something went wrong! Please try again!");
        }
    }

    public void clearBookCart() {
        bookCart.clear();
        System.out.println("Book Cart successfully cleared!");
    }

    public void removeBookFromBookCart() {
        try {
            if (bookCart.size() == 0) {
                System.out.println("Nothing to be cleared. Book Cart is empty!");
            } else {
                displayBookCart();
                System.out.print("Please enter ID to remove the book from the Book Cart: ");
                int id = Integer.parseInt(scanner.nextLine());
                for (Iterator<Book> iterator = bookCart.iterator(); iterator.hasNext(); ) {
                    Book book = iterator.next();
                    if (book.getId() == id) {
                        iterator.remove();
                        System.out.println("\"" + book.getAuthor() + " - " + book.getTitle() + "\"" + " successfully removed from the Book Cart!");
                    }
                }
                System.out.println("");
            }
        } catch (Exception ex) {
            System.out.println("Something went wrong! Please try again!");
        }
    }

    public void checkOut(String username) {
        try {
            int limit = database.countIssuedBooks(username, Status.DUE_DATE);
            if (limit == 2) {
                limitExceedMessage();
            } else if (limit == 1) {
                if (bookCart.size() > 1) {
                    System.out.println("You can borrow only 1 book, but your Book Cart contains " + bookCart.size() + " books.");
                    while (bookCart.size() > 1) {
                        removeBookFromBookCart();
                    }
                    checkoutBook(username);
                } else if (bookCart.size() == 1) {
                    checkoutBook(username);
                } else {
                    System.out.println("Your Book Cart is empty! Please visit Library Catalog to borrow books!");
                }
            } else {
                if (bookCart.size() > 2) {
                    System.out.println("You can borrow only 2 book, but your Book Cart contains " + bookCart.size() + " books.");
                    while (bookCart.size() > 2) {
                        removeBookFromBookCart();
                    }
                    checkoutBook(username);
                } else if (bookCart.size() == 0) {
                    System.out.println("Your Book Cart is empty! Please visit Library Catalog to borrow books!");
                } else {
                    checkoutBook(username);
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong! Please try again!");
        }
    }

    private void limitExceedMessage() {
        System.out.println("Unfortunately borrowing limit exceeded!");
        System.out.println("Please return borrowed books and try again!");
    }

    private void checkoutBook(String username) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 14);
        Date completeDueDate = calendar.getTime();
        for (Book book : bookCart) {
            database.createLoans(book.getId(), dateFormat.format(currentDate),
                    dateFormat.format(completeDueDate),
                    username, Status.DUE_DATE, null);
            database.editStatus(Status.DUE_DATE, book.getId());
            System.out.println("\"" + book.getAuthor() + " - " + book.getTitle() + "\"");
        }
        System.out.println("successfully checked out till " + dateFormat.format(completeDueDate));
        bookCart.clear();
    }

    public void returnBook(String username) {
        try {
            System.out.println(" ");
            database.displayUserBorrowedBooks(username, Status.DUE_DATE);
            System.out.print("\nPlease enter Book ID to return the book: ");
            int id = Integer.parseInt(scanner.nextLine());
            String checkStatus = String.valueOf(database.getLoanStatus(id));
            int test = database.checkIfExistsInLoan(id);
            if (test != 0) {
                if (checkStatus.contains(String.valueOf(Status.DUE_DATE))) {
                    database.updateBookReturnStatus(Status.RETURNED, dateFormat.format(currentDate), id);
                    int booksID = database.selectBooksId(id);
                    database.updateBookStatus(Status.AVAILABLE, booksID);
                    System.out.println("Thank you! Book successfully returned!");
                } else {
                    System.out.println("Book with ID " + id + " is already returned!");
                    returnBook(username);
                }
            } else {
                System.out.println("Book with ID " + id + " not found.");
                returnBook(username);
            }
        } catch (Exception e) {
            System.out.println("Invalid input! Please try again!");
        }
    }

    public void extendDueDate(String username) {
        try {
            database.displayUserBorrowedBooks(username, Status.DUE_DATE);
            System.out.print("Please enter Books ID to extend the due date for 5 days: ");
            int id = Integer.parseInt(scanner.nextLine());
            String checkStatus = String.valueOf(database.getLoanStatus(id));
            int test = database.checkIfExistsInLoan(id);
            if (test != 0) {
                if (checkStatus.contains(String.valueOf(Status.DUE_DATE).trim())) {
                    Date dueDateCalculation = calculateDateToExtend(id);
                    database.updateDueDate(dateFormat.format(dueDateCalculation), id);
                    System.out.println("Due date extended!");
                } else {
                    System.out.println("No Issued books found!");
                }
            } else {
                System.out.println(id + " not found. Please try again!");
            }
        } catch (Exception e) {
            System.out.println("Something went wrong! Please try again!");
        }
    }

    private Date calculateDateToExtend(int id) {
        Date newIssuedDate = database.getBookDueDate(id);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newIssuedDate);
        calendar.add(Calendar.DATE, 5);
        Date dueDateCalculation = calendar.getTime();
        return dueDateCalculation;
    }
}
