package invoice.core;

import invoice.model.Invoice;
import invoice.model.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    List<Invoice> findByScheduledDateBetween(LocalDate from, LocalDate to);
    List<Invoice> findByScheduleStatus(ScheduleStatus scheduleStatus);

}