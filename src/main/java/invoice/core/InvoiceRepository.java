package invoice.core;

import invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

interface InvoiceRepository extends JpaRepository<Invoice, String> {

    List<Invoice> findByScheduledDateBetween(LocalDate from, LocalDate to);

}