package invoice.core;

import invoice.model.ScheduleInvoiceRequest;
import invoice.model.ScheduleInvoiceResponse;
import invoice.auth.AuthDecoder;
import invoice.auth.Credentials;
import invoice.exceptions.InvoiceNotFoundException;
import invoice.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import invoice.risk.RiskService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
class InvoiceController {

    static final int MINIMAL_RISK_SCORE = 90;
    static final float MINIMAL_AMOUNT_TO_CHECK = 20000;
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
    ScheduleInvoiceResponse scheduleInvoiceById(@PathVariable String id, @Valid @RequestBody ScheduleInvoiceRequest scheduleInvoiceRequest) {
        String s = "";
        return repository.findById(id)
                .filter(invoice -> !invoice.getScheduled())
                .orElseThrow(() -> new InvoiceNotFoundException(Long.parseLong(id)))
                .schedule(scheduleInvoiceRequest);
    }

}
