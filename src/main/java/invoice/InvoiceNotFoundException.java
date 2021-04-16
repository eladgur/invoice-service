package invoice;

class InvoiceNotFoundException extends RuntimeException {

    InvoiceNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}