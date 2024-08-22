package murilo.barbosa.java_apache_poi_example.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import murilo.barbosa.java_apache_poi_example.spreadsheet.CellValidationError;

@Data
@Builder
public class ImportBookResultDto {

    private int importedBooks;
    private int errors;
    private String message;
    private List<CellValidationError> cellValidationErrors;
    private LocalDateTime operationDate;
}
