package invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import invoice.auth.Credentials;
import invoice.risk.RiskService;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

import java.util.Date;

import static invoice.InvoiceController.MINIMAL_AMOUNT_TO_CHECK;
import static invoice.InvoiceController.MINIMAL_RISK_SCORE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CreateInvoiceTest {

    private static final float AMOUNT_BELLOW_LIMIT = MINIMAL_AMOUNT_TO_CHECK - 1;
    private static final float AMOUNT_ABOVE_LIMIT = MINIMAL_AMOUNT_TO_CHECK + 1;
    private Invoice invoiceTemplate = new Invoice("1", MINIMAL_AMOUNT_TO_CHECK - 1, new Date(121, 12, 1), "asd", "awesomeComp", "e@awesomeComp.com");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RiskService riskService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void validInput() throws Exception {
        JSONObject json = new JSONObject();
        json.put("invoiceId", "3")
                .put("amount", 14.0)
                .put("creationDate", "1991-12-23")
                .put("companyName", "Asd")
                .put("customerEmail", "Asd@gmail.com");
        MockHttpServletRequestBuilder req = post("/invoices")
                .header("Authorization", "Basic QWxpY2U6MTIz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString());
        ResultActions res = this.mockMvc.perform(req);
        res.andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(json.put("creationDate", "1991-12-23T00:00:00.000+00:00").toString()));
    }

    @Test
    void inValidInput() throws Exception {
        JSONObject json = new JSONObject();
        json.put("invoiceId", "3")
                .put("amount", 14.0)
                .put("creationDate", "1991-12-23")
                .put("companyName", "Asd");
        MockHttpServletRequestBuilder req = post("/invoices")
                .header("Authorization", "Basic QWxpY2U6MTIz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString());
        ResultActions res = this.mockMvc.perform(req);
        res.andExpect(status().isBadRequest())
                .andExpect(content().string("newInvoice.newInvoice.customerEmail: must not be blank"));
    }

    @Test
    void inSufficientRiskScore() throws Exception {
        Mockito.when(riskService.estimateUserRiskScore(Mockito.any())).thenReturn(MINIMAL_RISK_SCORE - 1);
        Invoice invoice = new Invoice("1", AMOUNT_ABOVE_LIMIT, new Date(121, 12, 1), "asd", "awesomeComp", "e@awesomeComp.com");
        String json = objectMapper.writeValueAsString(invoice);
        MockHttpServletRequestBuilder req = post("/invoices")
                .header("Authorization", "Basic QWxpY2U6MTIz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString());
        ResultActions res = this.mockMvc.perform(req);
        String errorMsg = res.andReturn().getResponse().getErrorMessage();
        Assert.assertEquals(errorMsg, "Invoice max amount exceeded");
    }

    @Test
    void sufficientRiskScore() throws Exception {
        Mockito.when(riskService.estimateUserRiskScore(Mockito.any())).thenReturn(MINIMAL_RISK_SCORE + 1);
        JSONObject json = new JSONObject();
        json.put("invoiceId", "3")
                .put("amount", AMOUNT_ABOVE_LIMIT)
                .put("creationDate", "1991-12-23")
                .put("companyName", "Asd")
                .put("customerEmail", "Asd@gmail.com");
        MockHttpServletRequestBuilder req = post("/invoices")
                .header("Authorization", "Basic QWxpY2U6MTIz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString());
        ResultActions res = this.mockMvc.perform(req).andExpect(status().isOk())
                .andExpect(content().json(json.put("creationDate", "1991-12-23T00:00:00.000+00:00").toString()));
    }

    @Test
    void uncheckedAmount() throws Exception {
        Mockito.when(riskService.estimateUserRiskScore(Mockito.any())).thenReturn(MINIMAL_RISK_SCORE - 1);
        JSONObject json = new JSONObject();
        json.put("invoiceId", "3")
                .put("amount", AMOUNT_BELLOW_LIMIT)
                .put("creationDate", "1991-12-23")
                .put("companyName", "Asd")
                .put("customerEmail", "Asd@gmail.com");
        MockHttpServletRequestBuilder req = post("/invoices")
                .header("Authorization", "Basic QWxpY2U6MTIz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString());
        ResultActions res = this.mockMvc.perform(req).andExpect(status().isOk())
                .andExpect(content().json(json.put("creationDate", "1991-12-23T00:00:00.000+00:00").toString()));
    }
}