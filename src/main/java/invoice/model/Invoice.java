package invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Invoice {

    //    private @Id @GeneratedValue Long id;
    @Id
    private String invoiceId;
    @NotNull
    private float amount;
    @NotNull
    private LocalDate creationDate;
    @JsonIgnore
    private LocalDate scheduledDate;
    @JsonIgnore
    private ScheduleStatus scheduleStatus = ScheduleStatus.WAITINGFORSCHEDULE;
    private String description;
    @NotBlank
    private String companyName;
    @NotBlank
    private String customerEmail;

    public Invoice() {
    }

    public Invoice(String invoiceId, Float amount, LocalDate creationDate, String description, String companyName, String customerEmail) {
        this.amount = amount;
        this.creationDate = creationDate;
        this.description = description;
        this.companyName = companyName;
        this.customerEmail = customerEmail;
        this.invoiceId = invoiceId;
    }

    public Float getAmount() {
        return this.amount;
    }

    public @NotNull LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Invoice setAmount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Invoice))
            return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(this.invoiceId, invoice.invoiceId) && Objects.equals(this.amount, invoice.amount)
                && Objects.equals(this.creationDate, invoice.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.invoiceId, this.amount, this.creationDate);
    }

    @Override
    public String toString() {
        return "Invoice{" + "id=" + this.invoiceId + ", amount='" + this.amount + '\'' + ", creationDate='" + this.creationDate + '\'' + '}';
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public Invoice setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Invoice setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Invoice setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
        return this;
    }

    @JsonIgnore
    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public Invoice setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
        return this;
    }

    synchronized public ScheduleInvoiceResponse schedule(ScheduleInvoiceRequest scheduleInvoiceRequest) {
        LocalDate scheduledDate = scheduleInvoiceRequest.getScheduledDate();
        ScheduleInvoiceResponse response = new ScheduleInvoiceResponse(scheduledDate, invoiceId, false);

        if (isWaitingForSchedule()) {
            setScheduledDate(scheduledDate);
            setScheduleStatus(ScheduleStatus.SCHEDULED);
            response.setScheduledSucceeded(true);
        }

        return response;
    }

    @JsonIgnore
    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public Invoice setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
        return this;
    }

    @JsonIgnore
    public boolean isWaitingForSchedule() {
        return scheduleStatus.equals(ScheduleStatus.WAITINGFORSCHEDULE);
    }
}