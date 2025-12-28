package hr.airquality.exception;

import jakarta.ws.rs.core.Response;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
class NotFoundExceptionMapperIT {

    private final NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();

    @Test
    void shouldMapNotFoundExceptionToResponseInCDIContext() {
        // Arrange
        NotFoundException exception = new NotFoundException("Postaja nije pronađena");

        // Act
        Response response = mapper.toResponse(exception);
        NotFoundExceptionMapper.ErrorResponse body = (NotFoundExceptionMapper.ErrorResponse) response.getEntity();

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus(), "Status mora biti 404");
        assertNotNull(body, "Body ne smije biti null");
        assertEquals("Postaja nije pronađena", body.getMessage(), "Message mora odgovarati exceptionu");
    }

    @Test
    void shouldContainTimestampAndOptionalPath() {
        NotFoundExceptionMapper.ErrorResponse error = 
            new NotFoundExceptionMapper.ErrorResponse("Greška", "/fm/postaje");

        assertEquals("Greška", error.getMessage());
        assertEquals("/fm/postaje", error.getPath());
        assertTrue(error.getTimestamp() > 0, "Timestamp mora biti postavljen");
    }
}
