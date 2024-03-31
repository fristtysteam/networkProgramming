package Server;

import business.FilmManager;
import business.UserManager;
import protocol.FilmService;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpServer {
    private static  FilmManager filmManager;

    private static  UserManager userManager;

    public static void main(String[] args) throws IOException {
        // SET UP HOST AND PORT INFO
        // Done in FilmService utility class
        // Make a listening socket
        try (ServerSocket listeningSocket = new ServerSocket(FilmService.PORT)) {
            filmManager = new FilmManager();
            userManager = new UserManager();
        }
    }
}
