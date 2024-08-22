package murilo.barbosa.java_apache_poi_example.service;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import murilo.barbosa.java_apache_poi_example.spreadsheet.SpreadSheetCellValidator;

public class JakartaSpreadSheetCellValidator implements SpreadSheetCellValidator {

    @Override
    public <T> List<String> getErrors(T instance, Field field)
          throws IllegalArgumentException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        var errors = validator.validateProperty(instance, field.getName());
        List<String> messages = new ArrayList<>();
        if (!errors.isEmpty()) {
            errors.iterator().forEachRemaining(e -> messages.add(e.getMessage()));
        }
        return messages;
    }
}
