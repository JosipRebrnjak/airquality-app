package hr.airquality.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionMapperTest {

    private final NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();

    @Test
    void shouldMapNotFoundExceptionToResponse() {
        // Arrange
        NotFoundException exception = new NotFoundException("Postaja nije pronađena");

        // Act
        Response response = mapper.toResponse(exception);
        NotFoundExceptionMapper.ErrorResponse body = (NotFoundExceptionMapper.ErrorResponse) response.getEntity();

        // Assert
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNotNull(body);
        assertEquals("Postaja nije pronađena", body.getMessage());
    }

    @Test
    void shouldIncludeTimestampAndPathIfProvided() {
        // Ako si dodao konstruktor sa path i timestamp
        NotFoundExceptionMapper.ErrorResponse responseBody =
                new NotFoundExceptionMapper.ErrorResponse("Greška", "/fm/postaje");

        assertEquals("Greška", responseBody.getMessage());
        assertEquals("/fm/postaje", responseBody.getPath());
        assertTrue(responseBody.getTimestamp() > 0);
    }
}
