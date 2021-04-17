package invoice.core;

import invoice.TestUtil;
import invoice.model.Invoice;
import invoice.model.ScheduleInvoiceRequest;
import invoice.model.ScheduleInvoiceResponse;
import invoice.model.ScheduleStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static invoice.TestUtil.createInvoiceTemplate;
import static invoice.TestUtil.testEndpoint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduleInvoiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void tryToForceCreateScheduledInvoiceShouldFail() throws Exception {
        Invoice invoiceTemplate = createInvoiceTemplate().setScheduleStatus(ScheduleStatus.SCHEDULED);
        String json = TestUtil.toJsonString(invoiceTemplate);
        testEndpoint(this.mockMvc, post("/invoices"), json);
        String URL = String.format("/invoices/%s", invoiceTemplate.getInvoiceId());
        String response = testEndpoint(this.mockMvc, get(URL), json).andReturn().getResponse().getContentAsString();
        Invoice invoiceResponse = TestUtil.objectMapper.readValue(response, Invoice.class);
        Assert.assertEquals(ScheduleStatus.WAITINGFORSCHEDULE, invoiceResponse.getScheduleStatus());
    }

    @Test
    void scheduleInvoice() throws Exception {
        Invoice invoice = createInvoiceTemplate();
        String invoiceJson = TestUtil.toJsonString(invoice);
        TestUtil.testEndpoint(mockMvc, post("/invoices"), invoiceJson);

        String invoiceId = invoice.getInvoiceId();
        String scheduleURL = String.format("/invoices/schedule/%s", invoiceId);
        LocalDate scheduleDate = invoice.getCreationDate().plusWeeks(2);
        ScheduleInvoiceRequest scheduleInvoiceRequest = new ScheduleInvoiceRequest(scheduleDate);
        String scheduleReq = TestUtil.toJsonString(scheduleInvoiceRequest);
        String response = testEndpoint(this.mockMvc, put(scheduleURL), scheduleReq)
                .andReturn()
                .getResponse()
                .getContentAsString();

        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        Assert.assertTrue(optionalInvoice.isPresent());
        Invoice scheduledInvoice = optionalInvoice.get();
        Assert.assertEquals(ScheduleStatus.SCHEDULED, scheduledInvoice.getScheduleStatus());
        Assert.assertEquals(scheduleDate, scheduledInvoice.getScheduledDate());

        //        assert response
        ScheduleInvoiceResponse scheduleInvoiceResponse = TestUtil.objectMapper.readValue(response, ScheduleInvoiceResponse.class);
        Assert.assertTrue(scheduleInvoiceResponse.isScheduledSucceeded());
        Assert.assertEquals(scheduleDate, scheduleInvoiceResponse.getScheduledDate());
        Assert.assertEquals(invoiceId, scheduleInvoiceResponse.getInvoiceId());
    }
}
