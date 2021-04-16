package invoice;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Invoice {

//    private @Id @GeneratedValue Long id;
    @Id private String invoiceId;
    @NotNull private Float amount;
    @NotNull private LocalDate creationDate;
    private String description;
    @NotBlank private String companyName;
    @NotBlank private String customerEmail;

    public Invoice() {}

    Invoice(String invoiceId, Float amount, LocalDate creationDate, String description, String companyName, String customerEmail) {
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

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
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
}