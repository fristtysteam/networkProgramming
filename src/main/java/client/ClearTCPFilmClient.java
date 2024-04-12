package client;

import protocol.FilmService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClearTCPFlimClient {
    private static boolean loggedIn;

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        boolean validClient = true;
        loggedIn = false;

        while (validClient) {
            try (Socket dataSocket = new Socket(FilmService.HOST, FilmService.PORT);
                 Scanner input = new Scanner(dataSocket.getInputStream());
                 PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {

                boolean validSession = true;

                while (validSession) {
                    displayMenu();

                    String choice = userInput.nextLine();
                    String request = generateRequest(choice, userInput);

                    output.println(request);
                    output.flush();

                    String response = input.nextLine();
                    System.out.println("Received from server: " + response);

                    if (response.equals(FilmService.SUCCESS_LOGOUT)) {
                        validSession = false;
                        loggedIn = false;
                    } else if (response.equals(FilmService.SUCCESS_SHUTDOWN)) {
                        loggedIn = false;
                        validSession = false;
                        validClient = false;
                    } else if (response.equals(FilmService.EXIT)) {
                        loggedIn = false;
                        validSession = false;
                    } else if (response.equals(FilmService.SUCCESS_ADMIN) || response.equals(FilmService.SUCCESS_USER)) {
                        loggedIn = true;
                    } else if (response.equals(FilmService.INVALID)) {
                        System.out.println("Invalid request. Please try again.");
                    } else if (response.equals(FilmService.NO_MATCH)) {
                        System.out.println("No match found.");
                    }
                }

            } catch (UnknownHostException e) {
                System.out.println("Host cannot be found at this moment. Try again later");
            } catch (IOException e) {
                System.out.println("An IO Exception occurred: " + e.getMessage());
            }
        }
    }

    public static void displayMenu() {
        System.out.println("0) Exit");
        if (!loggedIn) {
            System.out.println("1) Register");
            System.out.println("2) Login");
        }
        if (loggedIn) {
            System.out.println("3) Logout");
            System.out.println("4) Rate a film");
        }
        System.out.println("5) Search film by name");
        System.out.println("6) Search all film by genre");
        if (loggedIn) {
            System.out.println("7) Add a film");
            System.out.println("8) Remove a film");
            System.out.println("9) Shut down server");
        }
    }

    public static String generateRequest(String choice, Scanner userInput) {
        switch (choice) {
            case "0":
                System.out.println("Exit?");
                return FilmService.EXIT;
            case "1":
                if (!loggedIn) {
                    System.out.println("Register: ");
                    System.out.println("Enter username: ");
                    String username = userInput.nextLine();
                    System.out.println("Enter password: ");
                    String password = userInput.nextLine();
                    return FilmService.REGISTER + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
                }
                break;
            case "2":
                if (!loggedIn) {
                    System.out.println("Login: ");
                    System.out.println("Enter username: ");
                    String username = userInput.nextLine();
                    System.out.println("Enter password: ");
                    String password = userInput.nextLine();
                    return FilmService.LOGIN + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
                }
                break;
            case "3":
                if (loggedIn) {
                    return FilmService.LOGOUT;
                }
                break;
            case "4":
                if (loggedIn) {
                    System.out.println("Enter film title: ");
                    String title = userInput.nextLine();
                    int rating = getValidRating(userInput, "Rating film from 1 to 10");
                    return FilmService.RATE + FilmService.DELIMITER + title + FilmService.DELIMITER + rating;
                }
                break;
            case "5":
                System.out.println("Search film by title: ");
                System.out.println("Enter title: ");
                String title = userInput.nextLine();
                return FilmService.SEARCH_NAME + FilmService.DELIMITER + title;
            case "6":
                System.out.println("Search film by genre: ");
                System.out.println("Enter genre: ");
                String genre = userInput.nextLine();
                return FilmService.SEARCH_GENRE + FilmService.DELIMITER + genre;
            case "7":
                if (loggedIn) {
                    System.out.println("Add a film: ");
                    System.out.println("Enter title: ");
                    String filmTitle = userInput.nextLine();
                    System.out.println("Enter genre: ");
                    String filmGenre = userInput.nextLine();
                    return FilmService.ADD_FILM_REQUEST + FilmService.DELIMITER + filmTitle + FilmService.DELIMITER + filmGenre;
                }
                break;
            case "8":
                if (loggedIn) {
                    System.out.println("Remove a film: ");
                    System.out.println("Enter title: ");
                    String filmTitle = userInput.nextLine();
                    return FilmService.REMOVE_FILM_REQUEST + FilmService.DELIMITER + filmTitle;
                }
                break;
            case "9":
                if (loggedIn) {
                    System.out.println("Shut down server?");
                    return FilmService.SHUTDOWN;
                }
                break;
            default:
                System.out.println("Please select one of the stated options!");
        }
        return FilmService.INVALID;
    }

    public static int getValidRating(Scanner userInput, String prompt) {
        boolean valid = false;
        int value = 0;
        while (!valid) {
            System.out.println(prompt);
            try {
                value = userInput.nextInt();
                if (value >= 1 && value <= 10) {
                    valid = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid number: 1 to 10. ");
                userInput.nextLine();
            }
        }
        userInput.nextLine();
        return value;
    }
}
