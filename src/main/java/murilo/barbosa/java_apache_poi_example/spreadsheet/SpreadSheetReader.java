package murilo.barbosa.java_apache_poi_example.spreadsheet;


import static murilo.barbosa.java_apache_poi_example.spreadsheet.CellFieldConverter.convertCellToField;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SpreadSheetReader<T> {

    private final Class<T> clazz;
    private SpreadSheetCellValidator validator;

    public SpreadSheetReaderResult<T> readSpreadSheet(InputStream inputStream, int jumpingRows)
          throws IOException {

        Workbook workbook = WorkbookFactory.create(inputStream);
        if (workbook.getNumberOfSheets() == 0) {
            throw new IllegalArgumentException("No sheets found in the workbook");
        }

        Sheet sheet = workbook.getSheetAt(0);
        var rows = sheet.rowIterator();

        List<T> data = new ArrayList<>();
        var resultErrors = new ArrayList<CellValidationError>();
        var fields = clazz.getDeclaredFields();

        readRows(rows, data, resultErrors, fields, jumpingRows);

        workbook.close();
        return new SpreadSheetReaderResult<T>(data, resultErrors);
    }

    private void readRows(Iterator<Row> rows, List<T> data, List<CellValidationError> resultErrors,
          Field[] fields, int jumpingRows) {
        while (rows.hasNext()) {
            Row row = rows.next();
            if (row.getRowNum() < jumpingRows) {
                continue;
            }
            T instance = createInstance(clazz);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Cell cell = row.getCell(i);
                if (Objects.isNull(cell)) {
                    continue;
                }
                trySetField(instance, field, cell, resultErrors);
            }
            if (resultErrors.isEmpty()) {
                data.add(instance);
            }
        }
    }

    private T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating instance of " + clazz.getName());
        }
    }

    private void trySetField(T instance, Field field, Cell cell, List<CellValidationError> resultErrors) {
        List<String> instanceErrors = new ArrayList<>();
        try {
            convertCellToField(cell, field, instance);
            if (Objects.nonNull(validator)) {
                var validatorErrors = validator.getErrors(instance, field);
                if (!validatorErrors.isEmpty()) {
                    instanceErrors.addAll(validatorErrors);
                }
            }
        } catch (IllegalStateException e) {
            instanceErrors.add(buildConvertErrorMessage(cell, field));
        } catch (IllegalAccessException e) {
            instanceErrors.add("Internal instanceErrors: %s".formatted(e.getMessage()));
        } finally {
            if (!instanceErrors.isEmpty()) {
                resultErrors.add(buildCellError(cell, instanceErrors));
            }
        }
    }

    private CellValidationError buildCellError(Cell cell, List<String> errors) {
        return new CellValidationError(cell.getRowIndex(), cell.getColumnIndex(), cell.toString(), errors);
    }

    private String buildConvertErrorMessage(Cell cell, Field field) {
        return "Cannot convert %s cell to %s field "
              .formatted(cell.getCellType().name(), field.getType().getSimpleName());
    }


}