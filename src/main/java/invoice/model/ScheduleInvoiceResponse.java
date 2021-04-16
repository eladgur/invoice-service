package invoice.model;

import java.time.LocalDate;

public class ScheduleInvoiceResponse {
    private LocalDate scheduledDate;
    private String invoiceId;
    private boolean scheduledSucceeded;

    public ScheduleInvoiceResponse() {}

    ScheduleInvoiceResponse(LocalDate scheduledDate, String invoiceId, boolean scheduledSucceeded) {
        this.scheduledDate = scheduledDate;
        this.invoiceId = invoiceId;
        this.scheduledSucceeded = scheduledSucceeded;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public boolean isScheduledSucceeded() {
        return scheduledSucceeded;
    }

    void setScheduledSucceeded(boolean scheduledSucceeded) {
        this.scheduledSucceeded = scheduledSucceeded;
    }
}
