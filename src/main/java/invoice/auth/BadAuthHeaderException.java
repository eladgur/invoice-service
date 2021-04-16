package invoice.auth;

class BadAuthHeaderException extends RuntimeException {

    BadAuthHeaderException() {
        super("Bad auth header");
    }
}