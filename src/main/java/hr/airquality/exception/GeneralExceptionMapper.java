package hr.airquality.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralizirani ExceptionMapper za sve neočekivane greške.
 * Vraća JSON odgovor sa message, timestamp i opcionalno path.
 */
@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger log = LoggerFactory.getLogger(GeneralExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        log.error("Neočekivana greška: ", exception);

        // Path može kasnije dohvatiti iz request konteksta ako je potrebno
        ErrorResponse error = new ErrorResponse("Došlo je do greške na serveru", null);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
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
