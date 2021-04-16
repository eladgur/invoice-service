package invoice.model;

import java.time.LocalDate;

public class ScheduleInvoiceRequest {

    private LocalDate scheduledDate;

    public ScheduleInvoiceRequest() {}

    public ScheduleInvoiceRequest(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
