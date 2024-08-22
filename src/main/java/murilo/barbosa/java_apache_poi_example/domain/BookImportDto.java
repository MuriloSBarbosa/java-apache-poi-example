package murilo.barbosa.java_apache_poi_example.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookImportDto {

    @NotBlank
    private String title;
    @NotBlank
    @Size(min = 1, max = 100)
    private String author;
    @DecimalMin(value = "0.0")
    private Double score;
}
