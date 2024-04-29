package Server;

import business.*;
import protocol.FilmAndUserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    private static FilmManager filmManager;
    private static UserManager userManager;

    public static void main(String[] args) {
        try (ServerSocket listeningSocket = new ServerSocket(FilmAndUserService.PORT)) {
            initialize();

            while (true) {
                Socket clientSocket = listeningSocket.accept();
                System.out.println("ClearTCPFlimClient connected: " + clientSocket.getInetAddress());

                handleClient(clientSocket);

                System.out.println("ClearTCPFlimClient disconnected.");
            }
        } catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + FilmAndUserService.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
        }
    }

    private static void initialize() {
        filmManager = new FilmManager();
        userManager = new UserManager();
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Received request: " + request);

                String response = processRequest(request);

                out.println(response);

                if (response.equals(FilmAndUserService.EXIT) || response.equals(FilmAndUserService.SHUTDOWN)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String processRequest(String request) {
        String[] parts = request.split(FilmAndUserService.DELIMITER);
        if (parts.length < 2) {
            return FilmAndUserService.INVALID;
        }

        String action = parts[0];
        String username = parts[1];
        String password = parts.length > 2 ? parts[2] : "";

        switch (action) {
            case FilmAndUserService.REGISTER:
                return register(parts);
            case FilmAndUserService.LOGIN:
                return login(username, password);
            case FilmAndUserService.LOGOUT:
                return FilmAndUserService.LOGGED_OUT;
            case FilmAndUserService.RATE:
                return rateFilm(parts);
            case FilmAndUserService.EXIT:
                return FilmAndUserService.EXIT;
            case FilmAndUserService.SHUTDOWN:
                return FilmAndUserService.SHUTDOWN;
            default:
                return FilmAndUserService.INVALID;
        }
    }

    private static String register(String[] components) {
        if (components.length != 3) {
            return FilmAndUserService.INVALID;
        }

        String username = components[1];
        String password = components[2];

        if (username.length() < 3) {
            return "Username too short";
        }

        if (!VerifyCredentials.checkPassword(password)) {
            return "Invalid password entered";
        }

        if (userManager.addUser(username, password)) {
            User user = new User(username, password, true);
            return FilmAndUserService.SUCCESS_REGISTER;
        } else {
            return "User already exists";
        }
    }

    private static String login(String username, String password) {
        User user = userManager.searchByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user.isAdminStatus() ? FilmAndUserService.SUCCESS_ADMIN : FilmAndUserService.SUCCESS_USER;
        }
        return FilmAndUserService.FAILED;
    }

    private static String rateFilm(String[] parts) {
        if (parts.length < 3) {
            return FilmAndUserService.INVALID;
        }
        return filmManager.rateFilm(parts[1], Double.parseDouble(parts[2])) ? FilmAndUserService.SUCCESS : FilmAndUserService.NO_MATCH;
    }

    private static String addFilm(String[] parts, String title, String genre, double totalRatings, int numberOfRatings) {
        if (filmManager.searchByTitle(title).equals(title)) {
            return FilmAndUserService.EXISTS;
        }
        if (parts.length < 4) {
            return FilmAndUserService.INVALID;
        }
        return filmManager.addFilm(new Film(title,genre,totalRatings, numberOfRatings))? FilmAndUserService.ADDED : FilmAndUserService.NO_MATCH;
    }

    private static boolean removeFilm(String title) {

        return filmManager.removeFilm(title);//? FilmService.SUCCESS : FilmService.NO_MATCH;
    }

}


