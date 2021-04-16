package invoice;

import org.springframework.data.jpa.repository.JpaRepository;

interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}