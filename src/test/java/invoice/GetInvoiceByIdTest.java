package invoice;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static invoice.TestUtil.createInvoiceTemplate;
import static invoice.TestUtil.testEndpoint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetInvoiceByIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void getById() throws Exception {
        String json = TestUtil.toJsonString(createInvoiceTemplate());
        testEndpoint(this.mockMvc, post("/invoices"), json);
        String URL = String.format("/invoices/%s", createInvoiceTemplate().getInvoiceId());
        testEndpoint(this.mockMvc, get(URL), json)
                .andExpect(content().json(json));
    }
}
