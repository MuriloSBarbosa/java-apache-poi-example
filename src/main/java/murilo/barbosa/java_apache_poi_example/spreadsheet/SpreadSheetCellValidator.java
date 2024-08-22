package murilo.barbosa.java_apache_poi_example.spreadsheet;


import java.lang.reflect.Field;
import java.util.List;

public interface SpreadSheetCellValidator {

    <T> List<String> getErrors(T instance, Field field)
          throws IllegalArgumentException;
}
