package murilo.barbosa.java_apache_poi_example.helper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import murilo.barbosa.java_apache_poi_example.domain.Book;

public class BookCreatorHelper {

    private static int id = 0;

    public static int getNextId() {
        return ++id;
    }

    public static List<Book> getInitialBooks() {
        var now = LocalDateTime.now();
        return new ArrayList<>(List.of(
              new Book(getNextId(), "The Lord of the Rings", "J.R.R. Tolkien",
                    9.3, now),
              new Book(getNextId(), "Harry Potter and the Philosopher's Stone", "J.K. Rowling",
                    8.1, now),
              new Book(getNextId(), "The Hobbit", "J.R.R. Tolkien",
                    8.0, now),
              new Book(getNextId(), "The Catcher in the Rye", "J.D. Salinger",
                    7.8, now),
              new Book(getNextId(), "1984", "George Orwell",
                    8.4, now)
        ));
    }
}
