package invoice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
public class Invoice {

    private @Id @GeneratedValue Long id;
    @Column(unique =true) private String invoiceId;
    @NotNull private Float amount;
    @NotNull private Date creationDate;
    private String description;
    @NotBlank private String companyName;
    @NotBlank private String customerEmail;

    public Invoice() {}

    Invoice(String invoiceId, Float amount, Date creationDate, String description, String companyName, String customerEmail) {
        this.amount = amount;
        this.creationDate = creationDate;
        this.description = description;
        this.companyName = companyName;
        this.customerEmail = customerEmail;
        this.invoiceId = invoiceId;
    }

    public Long getId() {
        return this.id;
    }

    public Float getAmount() {
        return this.amount;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Invoice))
            return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(this.id, invoice.id) && Objects.equals(this.amount, invoice.amount)
                && Objects.equals(this.creationDate, invoice.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.amount, this.creationDate);
    }

    @Override
    public String toString() {
        return "Invoice{" + "id=" + this.id + ", amount='" + this.amount + '\'' + ", creationDate='" + this.creationDate + '\'' + '}';
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

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}