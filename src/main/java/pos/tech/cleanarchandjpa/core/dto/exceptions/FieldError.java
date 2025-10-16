package pos.tech.cleanarchandjpa.core.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldError {
    private String field;
    private String message;
    private Object rejectedValue;
}