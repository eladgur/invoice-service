package invoice.model;

import java.time.LocalDate;

public class GetScheduledInvoicesRequest {
    private LocalDate from;
    private LocalDate to;

    public GetScheduledInvoicesRequest() {}

    public GetScheduledInvoicesRequest(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
