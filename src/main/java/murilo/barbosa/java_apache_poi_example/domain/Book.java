package murilo.barbosa.java_apache_poi_example.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import murilo.barbosa.java_apache_poi_example.spreadsheet.SpreadSheetColumnName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {

    @SpreadSheetColumnName("id")
    private int id;
    @SpreadSheetColumnName("nome")
    private String title;
    @SpreadSheetColumnName("autor")
    private String author;
    @SpreadSheetColumnName("score")
    private Double score;
    private LocalDateTime createdAt;
}
