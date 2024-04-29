package Server;

import business.Film;
import business.FilmManager;
import business.User;
import business.UserManager;
import protocol.FilmAndUserService;

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
        String[] components = message.split(FilmAndUserService.DELIMITER);
        if (components.length == 0) {
            System.out.println("Invalid request format");
            return FilmAndUserService.INVALID;
        }
        String action = components[0];
        switch (action) {
            case FilmAndUserService.REGISTER:
                return register(components);
            case FilmAndUserService.LOGIN:
                return login(components);
            case FilmAndUserService.LOGOUT:
                return logout();
            case FilmAndUserService.RATE_FILM_REQUEST:
                return rateFilm(components);
            case FilmAndUserService.SEARCH_FILM_REQUEST:
                return searchFilm(components);
            case FilmAndUserService.SEARCH_FILM_BY_GENRE_REQUEST:
                return searchByGenre(components);
            case FilmAndUserService.ADD_FILM_REQUEST:
                return addFilm(components);
            case FilmAndUserService.REMOVE_FILM_REQUEST:
                return removeFilm(components);
            case FilmAndUserService.EXIT:
                return exit();
            case FilmAndUserService.SHUTDOWN:
                return shutdownServer();
            default:
                System.out.println("Unknown action: " + action);
                return FilmAndUserService.INVALID;
        }
    }

    private String register(String[] components) {
        String response = FilmAndUserService.FAILED;
        if (components.length == 3) {
            String username = components[1];
            String password = components[2];
            if (username.length() >= 3 && UserManager.isPasswordValid(password)) {
                if (userManager.addUser(username, password)) {
                    currentUser = new User(username, username, true);
                    response = FilmAndUserService.SUCCESS_REGISTERR;
                } else {
                    response = FilmAndUserService.REJECTED;
                }
            } else {
                response = FilmAndUserService.INVALID;
            }
        } else {
            response = FilmAndUserService.INVALID;
        }
        return response;
    }

    private String login(String[] components) {
        String response = FilmAndUserService.FAILED;
        if (components.length == 3) {
            String username = components[1];
            String password = components[2];
            User user = userManager.searchByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                response = user.isAdminStatus() ? FilmAndUserService.SUCCESS_ADMIN : FilmAndUserService.SUCCESS_USER;
            } else {
                response = FilmAndUserService.NO_MATCH;
            }
        } else {
            response = FilmAndUserService.INVALID;
        }
        return response;
    }

    private String logout() {
        if (currentUser != null) {
            currentUser = null;
            state = false;
            return FilmAndUserService.SUCCESS_LOGOUT;
        } else {
            return FilmAndUserService.NOT_LOGGED_IN;
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
                        return success ? FilmAndUserService.SUCCESS : FilmAndUserService.FAILED;
                    } else {
                        return FilmAndUserService.NO_MATCH;
                    }
                } catch (NumberFormatException e) {
                    return FilmAndUserService.INVALID;
                }
            } else {
                return FilmAndUserService.NOT_LOGGED_IN;
            }
        } else {
            return FilmAndUserService.INVALID;
        }
    }

    private String searchFilm(String[] components) {
        if (components.length == 2) {
            Film film = filmManager.searchByTitle(components[1]);
            return (film != null) ? film.toString() : FilmAndUserService.NO_MATCH;
        } else {
            return FilmAndUserService.INVALID;
        }
    }

    private String searchByGenre(String[] components) {
        if (components.length == 2) {
            ArrayList<Film> films = filmManager.searchByGenre(components[1]);
            return (films != null && !films.isEmpty()) ? films.toString() : FilmAndUserService.NO_MATCH;
        } else {
            return FilmAndUserService.INVALID;
        }
    }

    private String searchByRating(String[] components) {
        if (components.length == 2) {
            try {
                int rating = Integer.parseInt(components[1]);
                ArrayList<Film> films = filmManager.searchByRating(rating);
                return (films != null && !films.isEmpty()) ? films.toString() : FilmAndUserService.NO_MATCH;
            } catch (NumberFormatException e) {
                return FilmAndUserService.INVALID;
            }
        } else {
            return FilmAndUserService.INVALID;
        }
    }

    private String addFilm(String[] components) {
        if (components.length == 3) {
            if (currentUser != null && currentUser.isAdminStatus()) {
                String title = components[1];
                String genre = components[2];
                Film film = new Film(title, genre, 0.0, 0);
                if (filmManager.addFilm(film)) {
                    return FilmAndUserService.ADDED;
                } else {
                    return FilmAndUserService.REJECTED;
                }
            } else {
                return FilmAndUserService.NO_MATCH;
            }
        } else {
            return FilmAndUserService.INVALID;
        }
    }

    private String removeFilm(String[] components) {
        if (components.length == 2) {
            if (currentUser != null && currentUser.isAdminStatus()) {
                String title = components[1];
                if (filmManager.removeFilm(title)) {
                    return FilmAndUserService.SUCCESS;
                } else {
                    return FilmAndUserService.FAILED;
                }
            } else {
                return FilmAndUserService.NO_MATCH;
            }
        } else {
            return FilmAndUserService.INVALID;
        }
    }

    private String exit() {
        if (currentUser != null) {
            currentUser = null;
            state = false;
            return FilmAndUserService.EXIT;
        } else {
            return FilmAndUserService.INVALID;
        }
    }

    private String shutdownServer() {
        return FilmAndUserService.SUCCESS_SHUTDOWN;
    }
}
