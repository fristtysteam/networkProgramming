package Server;

import business.FilmManager;
import business.UserManager;
import protocol.FilmService;
import business.User;
import business.Film;

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

    public static void main(String[] args) throws IOException {
        // SET UP HOST AND PORT INFO
        // Done in FilmService utility class
        // Make a listening socket
        try (ServerSocket listeningSocket = new ServerSocket(FilmService.PORT)) {
            filmManager = new FilmManager();
            userManager = new UserManager();

            while (true) {
                Socket dataSocket = listeningSocket.accept();
                System.out.println("Client connected: " + dataSocket.getInetAddress());

                handleClient(dataSocket);

                dataSocket.close();
                System.out.println("Client disconnected.");
            }
        } catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + FilmService.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
        }

    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Received request: " + request);

                // Process request and generate response
                String response = processRequest(request);

                // Send response back to client
                out.println(response);

                // Check for exit or shutdown requests
                if (response.equals(FilmService.EXIT) || response.equals(FilmService.SHUTDOWN)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(String request) {
        String[] parts = request.split(FilmService.DELIMITER);
        if (parts.length < 2) {
            return FilmService.INVALID;
        }

        String action = parts[0];
        String username = parts[1];
        String password = parts.length > 2 ? parts[2] : "";

        switch (action) {
            case "register":
                return userManager.addUser(username, password) ? FilmService.ADDED : FilmService.REJECTED;
            case "login":
                User user = userManager.searchByUsername(username);
                if (user != null && user.getPassword().equals(password)) {
                    return user.isAdminStatus() ? FilmService.SUCCESS_ADMIN : FilmService.SUCCESS_USER;
                } else {
                    return  FilmService.FAILED;
                }
            case "logout":
                return FilmService.LOGGED_OUT;
            case "rate":
                if (parts.length < 3) {
                    return FilmService.INVALID;
                }
                return filmManager.rateFilm(parts[0], Double.parseDouble(parts[1])) ? FilmService.SUCCESS:FilmService.NO_MATCH;

            case "exit":
                return "GOODBYE";
            case "shutdown":
                return "SHUTTING_DOWN";
            default:
                return FilmService.INVALID;
        }
    }
}

