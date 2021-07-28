import controller.Library;
import controller.Menu;
import entity.Book;
import entity.Users;
import repository.Database;

public class Main {

    public static void main(String[] args) {
        Database database = new Database();
        if (!database.open()) {
            System.out.println("Something went wrong. Can't open database.");
            return;
        }

        Book book = new Book();
        Users users = new Users();
        Library library = new Library(book, users, database);

        Menu menu = new Menu(library, users, database);
        menu.welcomeMessage();
        menu.start();

        database.close();

    }
}
