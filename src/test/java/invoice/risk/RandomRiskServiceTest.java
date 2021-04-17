package invoice.risk;

import invoice.auth.Credentials;
import invoice.core.InvoiceRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RandomRiskServiceTest {

    private RiskService riskService = new RandomRiskService();

    @Autowired
    private InvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        invoiceRepository.deleteAll();
    }

    @Test
    void basicTest(){
        int score = riskService.estimateUserRiskScore(new Credentials("e", "p"));
        Assert.assertTrue(0 < score && score < 101);
    }
}
