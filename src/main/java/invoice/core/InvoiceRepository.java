package invoice.core;

import invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

interface InvoiceRepository extends JpaRepository<Invoice, String> {

}