package invoice.auth;

public interface AuthDecoder {
    public Credentials decode(String s);
}

