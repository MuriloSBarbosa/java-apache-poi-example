package murilo.barbosa.java_apache_poi_example.spreadsheet;

import static murilo.barbosa.java_apache_poi_example.spreadsheet.CellFieldConverter.convertValueToCell;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SpreadSheetWriter<T> {

    private String sheetName;
    private CellStyle headerCellStyle;
    private CellStyle dataCellStyle;

    public Workbook createSpreadSheet(List<T> data) {
        Workbook workbook = new XSSFWorkbook();
        T baseObject = data.stream().findFirst()
              .orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NO_CONTENT, "No data to export"));
        Sheet sheet = workbook.createSheet(getSheetName(baseObject));
        var fields = getFields(baseObject);
        var columns = getColumns(fields);
        var fieldsName = fields.stream().map(Field::getName).toList();
        createHeaderCells(sheet, columns);
        createDataCells(sheet, fieldsName, data);
        return workbook;
    }

    private String getSheetName(T baseObject) {
        if (Objects.isNull(this.sheetName)) {
            return baseObject.getClass().getName();
        }

        return this.sheetName;
    }

    private List<Field> getFields(T data) {
        var fields = data.getClass().getDeclaredFields();
        List<Field> fieldsSpreadSheet = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(SpreadSheetColumnName.class)) {
                fieldsSpreadSheet.add(field);
            }
        }
        return fieldsSpreadSheet;
    }

    private List<String> getColumns(List<Field> fields) {
        return fields.stream()
              .map(field -> field.getAnnotation(SpreadSheetColumnName.class))
              .map(SpreadSheetColumnName::value).toList();
    }


    private void createHeaderCells(Sheet sheet, List<String> columns) {
        Row row = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(this.dataCellStyle);
            cell.setCellValue(columns.get(i));
        }
    }

    private void createDataCells(Sheet sheet, List<String> fieldsName, List<T> data) {
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i + 1);
            T current = data.get(i);

            for (int j = 0; j < fieldsName.size(); j++) {
                Cell cell = row.createCell(j);
                var attributeValue = getAttributeValue(current, fieldsName.get(j));
                convertValueToCell(cell, attributeValue);
                cell.setCellStyle(this.dataCellStyle);
                sheet.autoSizeColumn(j);
            }
        }
    }


    private Object getAttributeValue(T data, String fieldName) {
        try {
            Field field = data.getClass().getDeclaredField(fieldName);
            field.trySetAccessible();
            return field.get(data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}