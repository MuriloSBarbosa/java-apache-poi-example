package murilo.barbosa.java_apache_poi_example.spreadsheet;

import java.util.List;

public record CellValidationError(
      int row,
      int column,
      Object value,
      List<String> errors
) {

}
