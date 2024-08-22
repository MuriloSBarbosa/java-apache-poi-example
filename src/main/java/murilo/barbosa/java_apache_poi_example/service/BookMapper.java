package murilo.barbosa.java_apache_poi_example.service;

import static murilo.barbosa.java_apache_poi_example.helper.BookCreatorHelper.getNextId;

import java.time.LocalDateTime;
import murilo.barbosa.java_apache_poi_example.domain.Book;
import murilo.barbosa.java_apache_poi_example.domain.BookImportDto;

public class BookMapper {

    public static Book toBook(BookImportDto dto) {
        return Book.builder()
              .id(getNextId())
              .title(dto.getTitle())
              .author(dto.getAuthor())
              .score(dto.getScore())
              .createdAt(LocalDateTime.now())
              .build();
    }

}
