package invoice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import invoice.model.Invoice;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestUtil {
    static public ObjectMapper objectMapper = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String toJsonString(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public static Invoice createInvoiceTemplate() {
        return new Invoice("3", 1f, LocalDate.of(1991, 12, 1), "asd", "awesomeComp", "e@awesomeComp.com");
    }

    public static ResultActions testEndpoint(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return testEndpoint(mockMvc, requestBuilder, null);
    }

    public static ResultActions testEndpoint(MockMvc mockMvc, MockHttpServletRequestBuilder requestBuilder, String json) throws Exception {
        MockHttpServletRequestBuilder req = requestBuilder.header("Authorization", "Basic QWxpY2U6MTIz");

        if (json != null) {
            req.contentType(MediaType.APPLICATION_JSON).content(json);
        }

        return mockMvc.perform(req);
    }


}
