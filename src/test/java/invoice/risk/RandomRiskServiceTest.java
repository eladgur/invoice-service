package invoice.risk;

import invoice.auth.Credentials;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class RandomRiskServiceTest {

    private RiskService riskService = new RandomRiskService();

    @Test
    void basicTest(){
        int score = riskService.estimateUserRiskScore(new Credentials("e", "p"));
        Assert.assertNotNull(score);
        Assert.assertTrue(0 < score && score < 101);
    }
}
