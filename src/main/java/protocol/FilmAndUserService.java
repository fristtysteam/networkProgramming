package protocol;

 public class FilmAndUserService {
    // Location information
    public static final String HOST = "localhost";
    public static final int PORT = 41235;

    // Delimiter
    public static final String DELIMITER = "%%";

    public static final String EXISTS = "EXISTS";
    public static final String INVALID = "INVALID_REQUEST";
    public final static String RATE = "rate";
    public final static String SEARCH_NAME = "searchByName";
    public final static String SEARCH_GENRE = "searchByGenre";
    public final static String RATE_FILM_REQUEST = "RATE_FILM_REQUEST";
    public final static String SEARCH_FILM_BY_GENRE_REQUEST = "SEARCH_FILM_BY_GENRE_REQUEST";
    public final static String ADD_FILM_REQUEST = "ADD_FILM_REQUEST";
    public final static String REMOVE_FILM_REQUEST = "REMOVE_FILM_REQUEST";
    public final static String GET_ALL_FILMS_REQUEST = "GET_ALL_FILMS_REQUEST"; // New function

    public static final String INVALID_RATING_SUPPLIED_RATE_FILM_RESPONSE = "INVALID_RATING_SUPPLIED";
    public static final String NOT_LOGGED_IN_RATE_FILM_RESPONSE = "NOT_LOGGED_IN";
    public static final String NO_MATCH_FOUND_RATE_FILM_RESPONSE = "NO_MATCH_FOUND";
    public static final String SUCCESSFUL_ADD_FILM_RESPONSE = "ADDED";
    public static final String FAILED_ADD_FILM_RESPONSE = "EXISTS";
    public static final String INSUFFICIENT_PERMISSIONS_ADD_FILM_RESPONSE = "INSUFFICIENT_PERMISSIONS";
    public static final String SUCCESSFUL_REMOVE_FILM_RESPONSE = "REMOVED";
    public static final String FAILED_REMOVE_FILM_RESPONSE = "NOT_FOUND";

    public final static String SEARCH_FILM_REQUEST = "SEARCH_FILM_REQUEST";

    public static final String ADDED = "ADDED";
    public static final String REJECTED = "REJECTED";
    public static final String SUCCESS_ADMIN = "SUCCESS_ADMIN";
    public static final String SUCCESS_USER = "SUCCESS_USER";
    public static final String SUCCESS_REGISTER = "SUCCESS_REGISTER";

    public static final String SUCCESS_LOGOUT = "SUCCESS_LOGOUT";
    public static final String SUCCESS_SHUTDOWN = "SUCCESS_SHUTDOWN";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public final static String REGISTER = "register";
    public final static String LOGGED_OUT = "LOGGED_OUT";
    public final static String NOT_LOGGED_IN = "NOT_LOGGED_IN";
    public final static String LOGIN = "login";
    public final static String LOGOUT = "logout";
    public static final String NO_MATCH = "NO_MATCH_FOUND";

    public static final String EXIT = "GOODBYE";

    public static final String SHUTDOWN = "SHUTTING_DOWN";
    public static final String GET_ALL_FILMS_RESPONSE = "Get all films response";
}
