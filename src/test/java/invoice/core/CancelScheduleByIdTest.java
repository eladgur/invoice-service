package invoice.core;

import invoice.TestUtil;
import invoice.model.Invoice;
import invoice.model.ScheduleInvoiceRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CancelScheduleByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void getById() throws Exception {
        Invoice invoice = createInvoice();
        scheduleInvoice(createInvoice());
        cancelSchedule(invoice.getInvoiceId());
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoice.getInvoiceId());
        Assert.assertTrue(optionalInvoice.isPresent());
        Assert.assertNull(optionalInvoice.get().getScheduledDate());
    }

    private void cancelSchedule(String invoiceId) throws Exception {
        String URL = String.format("/invoices/schedule/%s", invoiceId);
        testEndpoint(this.mockMvc, delete(URL), "")
                .andExpect(status().isOk());
    }

    private void scheduleInvoice(Invoice invoice) throws Exception {
        String invoiceId = invoice.getInvoiceId();
        String scheduleURL = String.format("/invoices/schedule/%s", invoiceId);
        LocalDate scheduleDate = invoice.getCreationDate().plusWeeks(2);
        ScheduleInvoiceRequest scheduleInvoiceRequest = new ScheduleInvoiceRequest(scheduleDate);
        String scheduleReq = TestUtil.toJsonString(scheduleInvoiceRequest);
        testEndpoint(this.mockMvc, put(scheduleURL), scheduleReq)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private Invoice createInvoice() throws Exception {
        Invoice invoice = createInvoiceTemplate();
        String invoiceJson = TestUtil.toJsonString(invoice);
        TestUtil.testEndpoint(mockMvc, post("/invoices"), invoiceJson);

        return invoice;
    }
}
