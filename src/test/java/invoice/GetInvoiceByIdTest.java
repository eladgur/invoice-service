package invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Date;

import static invoice.InvoiceController.MINIMAL_AMOUNT_TO_CHECK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GetInvoiceByIdTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public void setUp() throws Exception {
        Invoice invoice = new Invoice("1", MINIMAL_AMOUNT_TO_CHECK + 1, new Date(121, 12, 1), "asd", "awesomeComp", "e@awesomeComp.com");
        String json = objectMapper.writeValueAsString(invoice);
        MockHttpServletRequestBuilder req = post("/invoices")
                .header("Authorization", "Basic QWxpY2U6MTIz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString());
    }

    @Test
    void highAmountLowRiskScore() throws Exception {
        Invoice invoice = new Invoice("1", MINIMAL_AMOUNT_TO_CHECK + 1, new Date(121, 12, 1), "asd", "awesomeComp", "e@awesomeComp.com");
        String json = objectMapper.writeValueAsString(invoice);
        MockHttpServletRequestBuilder req = post("/invoices")
                .header("Authorization", "Basic QWxpY2U6MTIz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString());
        ResultActions res = this.mockMvc.perform(req);
        String errorMsg = res.andReturn().getResponse().getErrorMessage();
        Assert.assertEquals(errorMsg, "Invoice max amount exceeded");
    }
}