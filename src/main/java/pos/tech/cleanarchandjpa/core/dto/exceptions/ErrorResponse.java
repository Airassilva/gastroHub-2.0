package pos.tech.cleanarchandjpa.core.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private List<FieldError> erros;
    private LocalDateTime timestamp;
}
