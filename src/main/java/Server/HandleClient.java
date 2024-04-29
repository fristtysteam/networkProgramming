package Server;

import business.Film;
import business.FilmManager;
import business.User;
import business.UserManager;
import protocol.FilmService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class HandleClient implements Runnable {
    private final Socket dataSocket;
    private final FilmManager filmManager;
    private final UserManager userManager;
    private boolean state;
    private User currentUser;

    public HandleClient(Socket dataSocket, FilmManager filmManager, UserManager userManager) {
        this.dataSocket = dataSocket;
        this.filmManager = filmManager;
        this.userManager = userManager;
        this.state = true;
        this.currentUser = null;
    }

    @Override
    public void run() {
        try (dataSocket) {
            try (Scanner input = new Scanner(dataSocket.getInputStream());
                 PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
                while (state) {
                    String message = input.nextLine();
                    System.out.println("Server received: " + message);
                    String response = processRequest(message);
                    output.println(response);
                    output.flush();
                }
            } catch (IOException e) {
                System.out.println("IOException occurred on server socket");
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String processRequest(String message) {
        String[] components = message.split(FilmService.DELIMITER);
        if (components.length == 0) {
            System.out.println("Invalid request format");
            return FilmService.INVALID;
        }
        String action = components[0];
        switch (action) {
            case FilmService.REGISTER:
                return register(components);
            case FilmService.LOGIN:
                return login(components);
            case FilmService.LOGOUT:
                return logout();
            case FilmService.RATE_FILM_REQUEST:
                return rateFilm(components);
            case FilmService.SEARCH_FILM_REQUEST:
                return searchFilm(components);
            case FilmService.SEARCH_FILM_BY_GENRE_REQUEST:
                return searchByGenre(components);
            case FilmService.ADD_FILM_REQUEST:
                return addFilm(components);
            case FilmService.REMOVE_FILM_REQUEST:
                return removeFilm(components);
            case FilmService.EXIT:
                return exit();
            case FilmService.SHUTDOWN:
                return shutdownServer();
            default:
                System.out.println("Unknown action: " + action);
                return FilmService.INVALID;
        }
    }

    private String register(String[] components) {
        String response = FilmService.FAILED;
        if (components.length == 3) {
            String username = components[1];
            String password = components[2];
            if (username.length() >= 3 && UserManager.isPasswordValid(password)) {
                if (userManager.addUser(username, password)) {
                    currentUser = new User(username, username, true);
                    response = FilmService.SUCCESS_REGISTERR;
                } else {
                    response = FilmService.REJECTED;
                }
            } else {
                response = FilmService.INVALID;
            }
        } else {
            response = FilmService.INVALID;
        }
        return response;
    }

    private String login(String[] components) {
        String response = FilmService.FAILED;
        if (components.length == 3) {
            String username = components[1];
            String password = components[2];
            User user = userManager.searchByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                response = user.isAdminStatus() ? FilmService.SUCCESS_ADMIN : FilmService.SUCCESS_USER;
            } else {
                response = FilmService.NO_MATCH;
            }
        } else {
            response = FilmService.INVALID;
        }
        return response;
    }

    private String logout() {
        if (currentUser != null) {
            currentUser = null;
            state = false;
            return FilmService.SUCCESS_LOGOUT;
        } else {
            return FilmService.NOT_LOGGED_IN;
        }
    }

    private String rateFilm(String[] components) {
        if (components.length == 3) {
            if (currentUser != null) {
                try {
                    String filmTitle = components[1];
                    int rating = Integer.parseInt(components[2]);
                    if (rating >= 0 && rating <= 10) {
                        boolean success = filmManager.rateFilm(filmTitle, rating);
                        return success ? FilmService.SUCCESS : FilmService.FAILED;
                    } else {
                        return FilmService.NO_MATCH;
                    }
                } catch (NumberFormatException e) {
                    return FilmService.INVALID;
                }
            } else {
                return FilmService.NOT_LOGGED_IN;
            }
        } else {
            return FilmService.INVALID;
        }
    }

    private String searchFilm(String[] components) {
        if (components.length == 2) {
            Film film = filmManager.searchByTitle(components[1]);
            return (film != null) ? film.toString() : FilmService.NO_MATCH;
        } else {
            return FilmService.INVALID;
        }
    }

    private String searchByGenre(String[] components) {
        if (components.length == 2) {
            ArrayList<Film> films = filmManager.searchByGenre(components[1]);
            return (films != null && !films.isEmpty()) ? films.toString() : FilmService.NO_MATCH;
        } else {
            return FilmService.INVALID;
        }
    }

    private String searchByRating(String[] components) {
        if (components.length == 2) {
            try {
                int rating = Integer.parseInt(components[1]);
                ArrayList<Film> films = filmManager.searchByRating(rating);
                return (films != null && !films.isEmpty()) ? films.toString() : FilmService.NO_MATCH;
            } catch (NumberFormatException e) {
                return FilmService.INVALID;
            }
        } else {
            return FilmService.INVALID;
        }
    }

    private String addFilm(String[] components) {
        if (components.length == 3) {
            if (currentUser != null && currentUser.isAdminStatus()) {
                String title = components[1];
                String genre = components[2];
                Film film = new Film(title, genre, 0.0, 0);
                if (filmManager.addFilm(film)) {
                    return FilmService.ADDED;
                } else {
                    return FilmService.REJECTED;
                }
            } else {
                return FilmService.NO_MATCH;
            }
        } else {
            return FilmService.INVALID;
        }
    }

    private String removeFilm(String[] components) {
        if (components.length == 2) {
            if (currentUser != null && currentUser.isAdminStatus()) {
                String title = components[1];
                if (filmManager.removeFilm(title)) {
                    return FilmService.SUCCESS;
                } else {
                    return FilmService.FAILED;
                }
            } else {
                return FilmService.NO_MATCH;
            }
        } else {
            return FilmService.INVALID;
        }
    }

    private String exit() {
        if (currentUser != null) {
            currentUser = null;
            state = false;
            return FilmService.EXIT;
        } else {
            return FilmService.INVALID;
        }
    }

    private String shutdownServer() {
        return FilmService.SUCCESS_SHUTDOWN;
    }
}
