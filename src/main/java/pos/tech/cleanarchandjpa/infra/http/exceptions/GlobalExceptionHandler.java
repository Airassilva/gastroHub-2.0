package pos.tech.cleanarchandjpa.infra.http.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String STATUS_KEY = "status";
    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";
    private static final String CAUSE_KEY = "causa";
    private static final String PATH_KEY = "path";
    private static final String INTERNAL_SERVER_ERROR_MSG = "Erro interno do servidor";

    private Map<String, Object> buildError(HttpStatus status, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put(TIMESTAMP_KEY, OffsetDateTime.now(ZoneOffset.UTC).toString());
        error.put(STATUS_KEY, status.value());
        error.put(ERROR_KEY, status.getReasonPhrase());
        error.put(MESSAGE_KEY, message);
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        log.warn("[VALIDATION] Erro de validação em parâmetros: {}", ex.getMessage(), ex);

        List<Map<String, Object>> fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fieldError(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, "Erro de validação", request.getRequestURI(), fields));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        log.warn("[VALIDATION] Violação de restrição: {}", ex.getMessage(), ex);

        List<Map<String, Object>> fields = ex.getConstraintViolations().stream()
                .map(cv -> fieldError(
                        simplifyPath(cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : null),
                        cv.getMessage(),
                        cv.getInvalidValue()
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, "Erro de validação", request.getRequestURI(), fields));
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Map<String, Object>> handleTransactionSystem(
            TransactionSystemException ex,
            HttpServletRequest request
    ) {
        log.error("[TX] Erro de transação: {}", ex.getMessage(), ex);

        ConstraintViolationException cve = findCause(ex, ConstraintViolationException.class);
        if (cve != null) return handleConstraintViolation(cve, request);

        DataIntegrityViolationException dive = findCause(ex, DataIntegrityViolationException.class);
        if (dive != null) return handleDataIntegrity(dive, request);

        Throwable root = rootCause(ex);
        log.error("[TX] Falha ao commitar transação: {}", root != null ? root.getMessage() : ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno do servidor. Entre em contato com o suporte.",
                        request.getRequestURI(),
                        null));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        log.warn("[DB] Violação de integridade de dados: {}", ex.getMessage(), ex);

        var cause = ex.getMostSpecificCause();
        String raw = Optional.of(cause)
                .map(Throwable::getMessage)
                .orElse(ex.getMessage());
        String lower = raw != null ? raw.toLowerCase() : "";

        HttpStatus status;
        String message;

        if (lower.contains("duplicate") || lower.contains("1062")) {
            if (lower.contains("email")) message = "Já existe um usuário com este e-mail.";
            else if (lower.contains("cpf")) message = "Já existe um usuário com este CPF.";
            else if (lower.contains("cnpj")) message = "Já existe um usuário com este CNPJ.";
            else message = "Registro duplicado. Verifique os valores únicos informados.";
            status = HttpStatus.CONFLICT;
        } else if (lower.contains("foreign key") || lower.contains("1452") || lower.contains("1451") || lower.contains("fk_")) {
            message = "Violação de chave estrangeira. Verifique as referências informadas.";
            status = HttpStatus.BAD_REQUEST;
        } else {
            message = "Violação de integridade de dados.";
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(status)
                .body(buildError(status, message, request.getRequestURI(), null));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request) {

        log.warn("[BAD_REQUEST] {}", ex.getMessage(), ex);

        Map<String, Object> error = new HashMap<>();
        error.put(TIMESTAMP_KEY, OffsetDateTime.now(ZoneOffset.UTC).toString());
        error.put(STATUS_KEY, HttpStatus.BAD_REQUEST.value());
        error.put(ERROR_KEY, "Bad Request");
        error.put(MESSAGE_KEY, ex.getMessage());
        error.put(PATH_KEY, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({org.springframework.orm.jpa.JpaSystemException.class, jakarta.persistence.PersistenceException.class})
    public ResponseEntity<Map<String, Object>> handleJpaLike(Exception ex, HttpServletRequest request) {
        log.error("[JPA] Exceção JPA: {}", ex.getMessage(), ex);

        ConstraintViolationException cve = findCause(ex, ConstraintViolationException.class);
        if (cve != null) return handleConstraintViolation(cve, request);

        DataIntegrityViolationException dive = findCause(ex, DataIntegrityViolationException.class);
        if (dive != null) return handleDataIntegrity(dive, request);

        Throwable root = rootCause(ex);
        log.error("[JPA] {}", root != null ? root.getMessage() : ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno do servidor. Entre em contato com o suporte.",
                        request.getRequestURI(),
                        null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.warn("[REQUEST] JSON inválido: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, "Formato de requisição inválido. Verifique o JSON enviado."));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNoSuchElement(NoSuchElementException ex) {
        log.warn("[REQUEST] Elemento não encontrado: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("[REQUEST] Entidade não encontrada: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointer(NullPointerException ex, WebRequest request) {
        log.error("[NULL] Valor obrigatório ausente ou nulo: {}", ex.getMessage(), ex);

        Map<String, Object> error = new LinkedHashMap<>();
        error.put(STATUS_KEY, HttpStatus.BAD_REQUEST.value());
        error.put(ERROR_KEY, "Valor nulo ou ausente");
        error.put(MESSAGE_KEY, ex.getMessage() != null ? ex.getMessage() : "Um valor obrigatório está ausente ou nulo.");
        error.put(CAUSE_KEY, ex.getClass().getName());
        error.put(PATH_KEY, request.getDescription(false).replace("uri=", ""));
        error.put(TIMESTAMP_KEY, OffsetDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<Map<String, Object>> handleRequestBindingException(Exception ex) {
        log.warn("[REQUEST] Parâmetros ausentes ou inválidos: {}", ex.getMessage(), ex);
        String mensagem = "Parâmetros da requisição inválidos ou ausentes. Verifique os valores enviados.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildError(HttpStatus.BAD_REQUEST, mensagem));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleBindException(BindException ex) {
        log.warn("[BIND] Erro de binding: {}", ex.getMessage(), ex);
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getFieldErrors().forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> error = buildError(HttpStatus.BAD_REQUEST, "Erro de binding nos parâmetros da requisição");
        error.put("fields", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({IllegalArgumentException.class, NumberFormatException.class})
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(Exception ex, WebRequest request) {
        log.warn("[REQUEST] Parâmetro inválido: {} - causa: {}", ex.getMessage(), ex.getCause(), ex);

        String mensagem = "Um dos parâmetros informados está com valor inválido. " +
                "Verifique se os valores passados na URL ou no corpo da requisição são do tipo esperado.";

        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put(STATUS_KEY, HttpStatus.BAD_REQUEST.value());
        errorBody.put(ERROR_KEY, "Requisição inválida");
        errorBody.put("mensagem", mensagem);
        errorBody.put("detalhes", ex.getMessage());
        errorBody.put(CAUSE_KEY, ex.getCause() != null ? ex.getCause().toString() : "Não especificada");
        errorBody.put(PATH_KEY, request.getDescription(false).replace("uri=", ""));
        errorBody.put(TIMESTAMP_KEY, LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Map<String, Object>> handleMissingPathVariable(MissingPathVariableException ex) {
        log.warn("[REQUEST] Variável de caminho ausente: {}", ex.getMessage(), ex);
        String mensagem = "URL de requisição inválida!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildError(HttpStatus.BAD_REQUEST, mensagem));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
        log.error("[GENERIC] Erro inesperado: {} - causa: {}", ex.getMessage(), ex.getCause(), ex);

        if (ex instanceof BadRequestException badRequest) {
            Map<String, Object> error = new HashMap<>();
            error.put(TIMESTAMP_KEY, OffsetDateTime.now(ZoneOffset.UTC).toString());
            error.put(STATUS_KEY, HttpStatus.BAD_REQUEST.value());
            error.put(ERROR_KEY, "Bad Request");
            error.put(MESSAGE_KEY, badRequest.getMessage());
            error.put(PATH_KEY, request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put(STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorBody.put(ERROR_KEY, INTERNAL_SERVER_ERROR_MSG);
        errorBody.put("mensagem", ex.getMessage() != null ? ex.getMessage() : "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        errorBody.put(CAUSE_KEY, ex.getCause() != null ? ex.getCause().toString() : "Não especificada");
        errorBody.put(PATH_KEY, request.getDescription(false).replace("uri=", ""));
        errorBody.put(TIMESTAMP_KEY, LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }

    private Map<String, Object> buildError(HttpStatus status, String message, String path, List<Map<String, Object>> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(STATUS_KEY, status.value());
        body.put(ERROR_KEY, status.getReasonPhrase());
        body.put(MESSAGE_KEY, message);
        body.put(TIMESTAMP_KEY, OffsetDateTime.now());
        if (path != null) body.put(PATH_KEY, path);
        if (errors != null && !errors.isEmpty()) body.put("errors", errors);
        return body;
    }

    private Map<String, Object> fieldError(String field, String message, Object rejectedValue) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("field", field);
        m.put(MESSAGE_KEY, message);
        if (rejectedValue != null) {
            m.put("rejectedValue", maskIfSensitive(field, rejectedValue));
        }
        return m;
    }

    private Object maskIfSensitive(String field, Object value) {
        if (value == null || field == null) return value;
        String f = field.toLowerCase();
        if (f.contains("senha") || f.contains("password")) return "********";
        return value;
    }

    private String simplifyPath(String path) {
        if (path == null) return null;
        String[] parts = path.split("\\.");
        return parts.length == 0 ? path : parts[parts.length - 1];
    }

    private Throwable rootCause(Throwable t) {
        Throwable cur = t;
        while (cur != null && cur.getCause() != null && cur.getCause() != cur) {
            cur = cur.getCause();
        }
        return cur;
    }

    private <T extends Throwable> T findCause(Throwable ex, Class<T> type) {
        Throwable cur = ex;
        while (cur != null) {
            if (type.isInstance(cur)) return type.cast(cur);
            cur = cur.getCause();
        }
        return null;
    }
}