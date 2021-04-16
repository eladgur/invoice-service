package invoice.risk;

import invoice.auth.Credentials;

public interface RiskService {
    int estimateUserRiskScore(Credentials credentials);
}
