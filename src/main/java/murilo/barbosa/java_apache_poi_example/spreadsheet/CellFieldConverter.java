package murilo.barbosa.java_apache_poi_example.spreadsheet;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import org.apache.poi.ss.usermodel.Cell;

public class CellFieldConverter {

    public CellFieldConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void convertValueToCell(Cell cell, Object value) {
        switch (value) {
            case String s -> cell.setCellValue(s);
            case Integer i -> cell.setCellValue(i);
            case Double v -> cell.setCellValue(v);
            case Boolean b -> cell.setCellValue(b);
            case LocalDateTime ldt -> cell.setCellValue(ldt);
            case Long l -> cell.setCellValue(l);
            case Float f -> cell.setCellValue(f);
            case Short s -> cell.setCellValue(s);
            case Byte b -> cell.setCellValue(b);
            case Character c -> cell.setCellValue(c);
            case null -> cell.setCellValue("");
            default -> cell.setCellValue(value.toString());
        }
    }

    public static <T> void convertCellToField(Cell cell, Field field, T instance)
          throws IllegalStateException, IllegalAccessException {
        field.trySetAccessible();
        switch (field.getType().getSimpleName().toLowerCase()) {
            case "string" -> field.set(instance, cell.getStringCellValue());
            case "integer", "int" -> field.set(instance, (int) cell.getNumericCellValue());
            case "double" -> field.set(instance, cell.getNumericCellValue());
            case "boolean" -> field.set(instance, cell.getBooleanCellValue());
            case "localdatetime" -> field.set(instance, cell.getLocalDateTimeCellValue());
            case "long" -> field.set(instance, (long) cell.getNumericCellValue());
            case "float" -> field.set(instance, (float) cell.getNumericCellValue());
            case "short" -> field.set(instance, (short) cell.getNumericCellValue());
            case "byte" -> field.set(instance, (byte) cell.getNumericCellValue());
            case "character" -> field.set(instance, cell.getStringCellValue().charAt(0));
            default -> field.set(instance, cell.getStringCellValue());
        }
    }
}
