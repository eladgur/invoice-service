package invoice.risk;

import invoice.auth.Credentials;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component("RandomRiskService")
public class RandomRiskService implements RiskService {

    private static final int TOP_LIMIT = 101;
    private Random random = new Random();

    @Override
    public int estimateUserRiskScore(Credentials credentials) {
        return this.random.nextInt(TOP_LIMIT);
    }
}
