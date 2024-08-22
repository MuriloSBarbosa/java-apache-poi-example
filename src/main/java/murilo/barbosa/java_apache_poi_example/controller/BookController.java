package murilo.barbosa.java_apache_poi_example.controller;

import java.net.URLConnection;
import lombok.RequiredArgsConstructor;
import murilo.barbosa.java_apache_poi_example.domain.ImportBookResultDto;
import murilo.barbosa.java_apache_poi_example.service.BookService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private static final String CONTENT_DISPOSITION_VALUE = "attachment; filename=%s";

    @GetMapping("/export")
    public ResponseEntity<Resource> export() {
        var fileDto = this.bookService.createSpreadSheetTempFile();
        return ResponseEntity.ok()
              .headers(getHeaders(fileDto.getFileName()))
              .body(new InputStreamResource(fileDto.getInputStream()));
    }

    @GetMapping("/export/in-memory")
    public ResponseEntity<Resource> exportInMemory() {
        var fileDto = this.bookService.createSpreadSheetInMemory();
        return ResponseEntity.ok()
              .headers(getHeaders(fileDto.getFileName()))
              .body(new InputStreamResource(fileDto.getInputStream()));
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportBookResultDto> importFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(this.bookService.importSpreadSheet(file));
    }

    private HttpHeaders getHeaders(String fileName) {
        // content disposition is a header that tells the browser how to handle a file that it
        // receives from a server. attachment means that the browser should download the file.
        // inline means that the browser should display the file if it can.
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUE.formatted(fileName));
        headers.add(HttpHeaders.CONTENT_TYPE, URLConnection.guessContentTypeFromName(fileName));
        return headers;
    }
}
