package protocol;

public class FilmService {
    // Location information
    public static final String HOST = "localhost";
    public static final int PORT = 41235;

    // Delimiter
    public static final String DELIMITER = "%%";

    // Requests

    // Responses
    public static final String INVALID = "INVALID_REQUEST";
    public static final String ADDED = "ADDED";
    public static final String REJECTED = "REJECTED";
    public static final String SUCCESS_ADMIN = "SUCCESS_ADMIN";
    public static final String SUCCESS_USER = "SUCCESS_USER";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    public static final String NO_MATCH = "NO_MATCH_FOUND";

    public static final String LOGGED_OUT = "LOGGED_OUT";

    public static final String EXIT = "GOODBYE";

    public static final String SHUTDOWN = "SHUTTING_DOWN";
}
