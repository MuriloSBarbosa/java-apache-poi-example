package murilo.barbosa.java_apache_poi_example.domain;

import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FileDto {

    private String fileName;
    private InputStream inputStream;
}
