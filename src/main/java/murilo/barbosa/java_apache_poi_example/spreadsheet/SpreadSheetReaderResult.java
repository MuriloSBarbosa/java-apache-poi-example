package murilo.barbosa.java_apache_poi_example.spreadsheet;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpreadSheetReaderResult<T> {

    private List<T> data;
    private List<CellValidationError> errors;
}
