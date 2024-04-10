package client;

import business.Film;
import business.FilmManager;
import business.UserManager;
import protocol.FilmService;
import business.User;
import Server.TcpServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClearTCPFilmClient {

    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);
        try (Socket dataSocket = new Socket(FilmService.HOST, FilmService.PORT)) {

            // Sets up communication lines
            // Create a Scanner to receive messages
            // Create a Printwriter to send messages
            try (Scanner input = new Scanner(dataSocket.getInputStream());
                 PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {

                }
            }

        } catch (UnknownHostException e) {
            System.out.println("Host cannot be found at this moment. Try again later");
        } catch (IOException e) {
            System.out.println("An IO Exception occurred: " + e.getMessage());
        }
        // Close connection to server
    }  // Requests a connection


}
