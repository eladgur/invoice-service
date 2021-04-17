package invoice.core;

import com.fasterxml.jackson.core.type.TypeReference;
import invoice.TestUtil;
import invoice.model.Invoice;
import invoice.model.GetScheduledInvoicesRequest;
import invoice.model.ScheduleInvoiceRequest;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static invoice.TestUtil.createInvoiceTemplate;
import static invoice.TestUtil.testEndpoint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetScheduledInvoicesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        invoiceRepository.deleteAll();
    }

    @Test
    void noScheduledInvoices() throws Exception {
        LocalDate from = LocalDate.of(2021, 1, 1);
        LocalDate to = LocalDate.of(2021, 1, 28);
        String json = TestUtil.toJsonString(new GetScheduledInvoicesRequest(from, to));
        String res = testEndpoint(this.mockMvc, get("/invoices/scheduled"), json).andReturn().getResponse().getContentAsString();
        List invoices = TestUtil.objectMapper.readValue(res, List.class);
        Assert.assertTrue(invoices.isEmpty());
    }

    @Test
    void withScheduledInvoicesInRange() throws Exception {
        Invoice invoice1 = createInvoiceTemplate().setInvoiceId("4");
        Invoice invoice2 = createInvoiceTemplate().setInvoiceId("5");
        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        LocalDate scheduleDate = LocalDate.of(2022, 5, 12);
        String scheduleURL = String.format("/invoices/schedule/%s", invoice1.getInvoiceId());
        String scheduleReq = TestUtil.toJsonString(new ScheduleInvoiceRequest(scheduleDate));
        testEndpoint(this.mockMvc, put(scheduleURL), scheduleReq);

        scheduleDate = LocalDate.of(2022, 5, 14);
        scheduleURL = String.format("/invoices/schedule/%s", invoice2.getInvoiceId());
        scheduleReq = TestUtil.toJsonString(new ScheduleInvoiceRequest(scheduleDate));
        testEndpoint(this.mockMvc, put(scheduleURL), scheduleReq);

        LocalDate from = LocalDate.of(2022, 1, 1);
        LocalDate to = LocalDate.of(2022, 5, 13);
        String json = TestUtil.toJsonString(new GetScheduledInvoicesRequest(from, to));
        String res = testEndpoint(this.mockMvc, get("/invoices/scheduled"), json).andReturn().getResponse().getContentAsString();
        List<Invoice> invoices = TestUtil.objectMapper.readValue(res, new TypeReference<List<Invoice>>(){});
        List<String> scheduledIds = invoices.stream().map(Invoice::getInvoiceId).collect(Collectors.toList());
        Assert.assertEquals(1, invoices.size());
        Assert.assertTrue(scheduledIds.contains(invoice1.getInvoiceId()));
    }

    @Test
    void withScheduledInvoices() throws Exception {
        Invoice invoice1 = createInvoiceTemplate().setInvoiceId("4");
        Invoice invoice2 = createInvoiceTemplate().setInvoiceId("5");
        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        LocalDate scheduleDate = LocalDate.of(2022, 5, 12);
        String scheduleURL = String.format("/invoices/schedule/%s", invoice1.getInvoiceId());
        String scheduleReq = TestUtil.toJsonString(new ScheduleInvoiceRequest(scheduleDate));
        testEndpoint(this.mockMvc, put(scheduleURL), scheduleReq);

        scheduleDate = LocalDate.of(2022, 5, 14);
        scheduleURL = String.format("/invoices/schedule/%s", invoice2.getInvoiceId());
        scheduleReq = TestUtil.toJsonString(new ScheduleInvoiceRequest(scheduleDate));
        testEndpoint(this.mockMvc, put(scheduleURL), scheduleReq);

        String res = testEndpoint(this.mockMvc, get("/invoices/scheduled")).andReturn().getResponse().getContentAsString();
        List<Invoice> invoices = TestUtil.objectMapper.readValue(res, new TypeReference<List<Invoice>>(){});
        List<String> scheduledIds = invoices.stream().map(Invoice::getInvoiceId).collect(Collectors.toList());
        Assert.assertEquals(2, invoices.size());
        Assert.assertTrue(scheduledIds.contains(invoice1.getInvoiceId()));
        Assert.assertTrue(scheduledIds.contains(invoice2.getInvoiceId()));
    }

}
