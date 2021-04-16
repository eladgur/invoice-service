package invoice.core;

import invoice.risk.RiskService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class CreateInvoiceTestConfiguration {
    @Bean
    @Primary
    public RiskService riskService() {
        return Mockito.mock(RiskService.class);
    }
}