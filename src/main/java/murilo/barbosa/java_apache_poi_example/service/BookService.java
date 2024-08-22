package murilo.barbosa.java_apache_poi_example.service;

import static murilo.barbosa.java_apache_poi_example.helper.BookCreatorHelper.getInitialBooks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import murilo.barbosa.java_apache_poi_example.domain.Book;
import murilo.barbosa.java_apache_poi_example.domain.BookImportDto;
import murilo.barbosa.java_apache_poi_example.domain.FileDto;
import murilo.barbosa.java_apache_poi_example.domain.ImportBookResultDto;
import murilo.barbosa.java_apache_poi_example.spreadsheet.SpreadSheetWriter;
import murilo.barbosa.java_apache_poi_example.spreadsheet.SpreadSheetReader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final List<Book> books = getInitialBooks();

    public FileDto createSpreadSheetTempFile() {
        SpreadSheetWriter<Book> book = new SpreadSheetWriter<>();
        var woorkBook = book.createSpreadSheet(books);
        try {
            File file = File.createTempFile("books", ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(file);
            woorkBook.write(outputStream);

            var fileDto = FileDto.builder()
                  .fileName(generateFileName())
                  .inputStream(new FileInputStream(file))
                  .build();

            outputStream.close();
            woorkBook.close();
            file.delete();

            return fileDto;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public FileDto createSpreadSheetInMemory() {
        SpreadSheetWriter<Book> book = new SpreadSheetWriter<>();
        var woorkBook = book.createSpreadSheet(books);
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            woorkBook.write(output);
            var fileDto = FileDto.builder()
                  .fileName(generateFileName())
                  .inputStream(new ByteArrayInputStream(output.toByteArray()))
                  .build();
            woorkBook.close();
            return fileDto;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String generateFileName() {
        return "books-%s.xlsx".formatted(LocalDateTime.now());
    }

    public ImportBookResultDto importSpreadSheet(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {

            var reader = new SpreadSheetReader<>(BookImportDto.class);

            var validator = new JakartaSpreadSheetCellValidator();
            reader.setValidator(validator);

            var importedBooks = reader.readSpreadSheet(inputStream, 1);

            if (!importedBooks.getErrors().isEmpty()) {
                return ImportBookResultDto.builder()
                      .message("Some books could not be imported")
                      .operationDate(LocalDateTime.now())
                      .errors(importedBooks.getErrors().size())
                      .cellValidationErrors(importedBooks.getErrors())
                      .build();
            }

            books.addAll(importedBooks.getData().stream().map(BookMapper::toBook).toList());

            return ImportBookResultDto.builder()
                  .importedBooks(importedBooks.getData().size())
                  .message("Books imported successfully")
                  .operationDate(LocalDateTime.now())
                  .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }
}
