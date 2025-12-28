package hr.airquality.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vraća JSON odgovor sa message, timestamp i opcionalno path.
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    private static final Logger log = LoggerFactory.getLogger(NotFoundExceptionMapper.class);

    @Override
    public Response toResponse(NotFoundException exception) {
        log.warn("NotFoundException: {}", exception.getMessage());

        ErrorResponse error = new ErrorResponse(exception.getMessage(), null);

        return Response.status(Response.Status.NOT_FOUND)
                       .entity(error)
                       .build();
    }

    @Data
    @NoArgsConstructor
    public static class ErrorResponse {
        private String message;
        private long timestamp = System.currentTimeMillis();
        private String path;

        public ErrorResponse(String message, String path) {
            this.message = message;
            this.path = path;
        }
    }
}
