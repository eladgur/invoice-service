package invoice.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import invoice.core.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class AuthHeaderTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        invoiceRepository.deleteAll();
    }

    @Test
    void badHeaderTest() throws Exception {
        ResultActions res = this.mockMvc.perform(get("/invoices").header("Authorization", "bla"));
        res.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Bad auth header"));
//				.andExpect(content().string(containsString("Hello, World")));
    }

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        ResultActions res = this.mockMvc.perform(get("/invoices").header("Authorization", "Basic QWxpY2U6MTIz"));
        res.andDo(print()).andExpect(status().isOk());
//				.andExpect(content().string(containsString("Hello, World")));
    }
}