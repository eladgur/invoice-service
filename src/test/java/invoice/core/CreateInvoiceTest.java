package invoice.core;

import invoice.TestUtil;
import invoice.risk.RiskService;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
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

import static invoice.core.InvoiceController.MINIMAL_AMOUNT_TO_CHECK;
import static invoice.core.InvoiceController.MINIMAL_RISK_SCORE;
import static invoice.TestUtil.testEndpoint;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RiskService riskService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        invoiceRepository.deleteAll();
    }

    @Test
    void validInput() throws Exception {
        String json = TestUtil.toJsonString(TestUtil.createInvoiceTemplate());
        testEndpoint(this.mockMvc, post("/invoices"), json)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void invalidInputMissingField() throws Exception {
        String json = new JSONObject()
                .put("invoiceId", "3")
                .put("amount", 14f)
                .put("creationDate", "1991-12-23")
                .put("companyName", "awesomeComp")
                .toString();

        MockHttpServletRequestBuilder req = post("/invoices")
                .header("Authorization", "Basic QWxpY2U6MTIz")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        ResultActions res = this.mockMvc.perform(req);
        res.andExpect(status().isBadRequest())
                .andExpect(content().string("newInvoice.newInvoice.customerEmail: must not be blank"));
    }

    @Test
    void inSufficientRiskScore() throws Exception {
        String json = TestUtil.toJsonString(TestUtil.createInvoiceTemplate().setAmount(AMOUNT_ABOVE_LIMIT));
        Mockito.when(riskService.estimateUserRiskScore(Mockito.any())).thenReturn(MINIMAL_RISK_SCORE - 1);
        String errorMsg = testEndpoint(this.mockMvc, post("/invoices"), json).andReturn().getResponse().getErrorMessage();
        Assert.assertEquals(errorMsg, "Invoice max amount exceeded");
    }

    @Test
    void sufficientRiskScore() throws Exception {
        Mockito.when(riskService.estimateUserRiskScore(Mockito.any())).thenReturn(MINIMAL_RISK_SCORE + 1);
        String json = TestUtil.toJsonString(TestUtil.createInvoiceTemplate().setAmount(AMOUNT_ABOVE_LIMIT));
        testEndpoint(this.mockMvc, post("/invoices"), json)
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    void uncheckedAmount() throws Exception {
        Mockito.when(riskService.estimateUserRiskScore(Mockito.any())).thenReturn(MINIMAL_RISK_SCORE - 1);
        String json = TestUtil.toJsonString(TestUtil.createInvoiceTemplate().setAmount(AMOUNT_BELLOW_LIMIT));
        testEndpoint(this.mockMvc, post("/invoices"), json)
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }
}