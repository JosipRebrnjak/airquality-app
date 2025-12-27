package hr.airquality.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        Map<String, Object> error = new HashMap<>();
        error.put("error", exception.getMessage());
        error.put("type", exception.getClass().getSimpleName());

        Response.Status status;

        // odredi status kod
        if (exception instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return Response.status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
