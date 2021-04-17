package invoice.core;

import invoice.model.*;
import invoice.auth.AuthDecoder;
import invoice.auth.Credentials;
import invoice.exceptions.InvoiceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import invoice.risk.RiskService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
class InvoiceController {

    static final int MINIMAL_RISK_SCORE = 90;
    static final float MINIMAL_AMOUNT_TO_CHECK = 20000;
    static final String SCHEDULE_INVOICE_FIRST_MSG = "Schedule invoice first";
    static final String SCHEDULE_ALREADY_CANCELLED_MSG = "Schedule already cancelled";
    private static final String SCHEDULED_CANCELED_MSG = "Scheduled canceled";
    private final InvoiceRepository repository;
    private final AuthDecoder authDecoder;
    private final RiskService riskService;

    @Autowired
    InvoiceController(InvoiceRepository repository, AuthDecoder authDecoder, RiskService riskService) {
        this.repository = repository;
        this.authDecoder = authDecoder;
        this.riskService = riskService;
    }

    @GetMapping("/invoices")
    List<Invoice> getInvoices(@RequestHeader("Authorization") String authHeader) {
        Credentials credentials = this.authDecoder.decode(authHeader);
        return repository.findAll();
    }

    @PostMapping("/invoices")
    Invoice newInvoice(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody Invoice newInvoice, BindingResult result) {
        Credentials credentials = this.authDecoder.decode(authHeader);
        int riskScore = this.riskService.estimateUserRiskScore(credentials);
        if (MINIMAL_AMOUNT_TO_CHECK < newInvoice.getAmount() && riskScore < MINIMAL_RISK_SCORE) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invoice max amount exceeded");
        } else {
            return repository.save(newInvoice);
        }
    }

    @GetMapping("/invoices/{id}")
    Invoice getInvoiceById(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(Long.parseLong(id)));
    }

    @PutMapping("/invoices/{id}")
    Invoice replaceInvoice(@RequestBody Invoice newInvoice, @PathVariable String id) {

        return repository.findById(id)
                .map(invoice -> {
                    invoice.setAmount(newInvoice.getAmount());
                    invoice.setCreationDate(newInvoice.getCreationDate());
                    return repository.save(invoice);
                })
                .orElseGet(() -> {
                    newInvoice.setInvoiceId(id);
                    return repository.save(newInvoice);
                });
    }

    @DeleteMapping("/invoices/{id}")
    void deleteEmployee(@PathVariable String id) {
        repository.deleteById(id);
    }

    @PutMapping("/invoices/schedule/{id}")
    synchronized ScheduleInvoiceResponse scheduleInvoiceById(@PathVariable String id, @Valid @RequestBody ScheduleInvoiceRequest scheduleInvoiceRequest) {
        Invoice invoice = repository.findById(id)
                .filter(i -> i.getScheduleStatus().equals(ScheduleStatus.WAITINGFORSCHEDULE))
                .orElseThrow(() -> new InvoiceNotFoundException(Long.parseLong(id)));

        LocalDate scheduledDate = scheduleInvoiceRequest.getScheduledDate();
        ScheduleInvoiceResponse response = new ScheduleInvoiceResponse(scheduledDate, id, false);

        if (invoice.isWaitingForSchedule()) {
            invoice.setScheduledDate(scheduledDate)
                    .setScheduleStatus(ScheduleStatus.SCHEDULED);
            repository.save(invoice);
            response.setScheduledSucceeded(true);
        }

        return response;
    }

    @GetMapping("/invoices/scheduled")
    List<Invoice> getScheduledInvoices(@Valid @RequestBody(required = false) GetScheduledInvoicesRequest req) {
        List<Invoice> res;

        if (req != null) {
            LocalDate from = req.getFrom();
            LocalDate to = req.getTo();
            res = repository.findByScheduledDateBetween(from, to);
        } else {
            res = repository.findByScheduleStatus(ScheduleStatus.SCHEDULED);
        }

        return res;
    }

    @DeleteMapping("/invoices/schedule/{id}")
    ResponseEntity<String> cancelSchedule(@PathVariable String id) {
        String msg;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Invoice invoice = repository.findById(id).orElseThrow(() -> new InvoiceNotFoundException(Long.parseLong(id)));
        switch (invoice.getScheduleStatus()) {
            case SCHEDULED:
                invoice.setScheduledDate(null);
                invoice.setScheduleStatus(ScheduleStatus.CANCELED);
                repository.save(invoice);
                msg = SCHEDULED_CANCELED_MSG;
                httpStatus = HttpStatus.OK;
                break;
            case CANCELED:
                msg = SCHEDULE_ALREADY_CANCELLED_MSG;
                break;
            default:
                msg = SCHEDULE_INVOICE_FIRST_MSG;
        }

        return ResponseEntity.status(httpStatus).body(msg);
    }

}
