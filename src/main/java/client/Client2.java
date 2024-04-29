package client;

import business.User;
import protocol.FilmService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client2 {

    private static boolean loggedIn;
    private static User user = new User();
    private static boolean validClient = true;

    public static void main(String[] args) {
        try (Socket dataSocket = new Socket(FilmService.HOST, FilmService.PORT)) {
            try (Scanner input = new Scanner(dataSocket.getInputStream());
                 PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
                Scanner userInput = new Scanner(System.in);
                loggedIn = false;
                while (validClient) {
                    handleUserRequest(userInput, input, output);
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Host cannot be found at this moment. Try again later");
        } catch (IOException e) {
            System.out.println("An IO Exception occurred: " + e.getMessage());
        }
    }

    private static void handleUserRequest(Scanner userInput, Scanner input, PrintWriter output) {
        boolean validSession = true;
        while (validSession) {
            System.out.println("Please select an option:");
            displayMenu();
            String message = generateRequest(userInput);
            if (message != null) {
                output.println(message);
                output.flush();
                String response = input.nextLine();
                processResponse(response, userInput);
            }
        }
    }

    private static void displayMenu() {
        System.out.println("0) Exit");
        if (!loggedIn) {
            System.out.println("1) Register");
            System.out.println("2) Login");
        } else {
            System.out.println("3) Logout");
            System.out.println("4) Rate a film");
        }
        System.out.println("5) Search film by name");
        System.out.println("6) Search all films by genre");
        if (loggedIn && user.isAdminStatus()) {
            System.out.println("7) Add a film");
            System.out.println("8) Remove a film");
            System.out.println("9) Shutdown server");
        }
        System.out.println("10) Search all films by rating");
    }


    public static String generateRequest(Scanner userInput) {
        String choice = "-1";

        boolean valid = false;
        String request = null;

        while (!valid) {
            choice = userInput.nextLine();
            switch (choice) {
                case "0":
                    System.out.println("Are you sure you want to exit? (Y/N)");
                    String exitChoice = userInput.nextLine().toUpperCase();
                    if (exitChoice.equals("Y")) {
                        request = FilmService.EXIT;
                        valid = true;
                    }
                    break;
                case "1":
                    if (!loggedIn) {
                        request = register(userInput);
                        valid = true;
                    }
                    break;
                case "2":
                    if (!loggedIn) {
                        request = login(userInput);
                        valid = true;
                    }
                    break;
                case "3":
                    if (loggedIn) {
                        request = FilmService.LOGOUT;
                        valid = true;
                    }
                    break;
                case "4":
                    if (loggedIn) {
                        request = rateFilm(userInput);
                        valid = true;
                    }
                    break;
                case "5":
                    System.out.println("Enter film title: ");
                    String title = userInput.nextLine();
                    request = FilmService.SEARCH_NAME + FilmService.DELIMITER + title;
                    valid = true;
                    break;
                case "6":
                    System.out.println("Enter genre: ");
                    String genre = userInput.nextLine();
                    request = FilmService.SEARCH_GENRE + FilmService.DELIMITER + genre;
                    valid = true;
                    break;
                case "7":
                    if (loggedIn && user.isAdminStatus()) {
                        request = addFilm(userInput);
                        valid = true;
                    }
                    break;
                case "8":
                    if (loggedIn && user.isAdminStatus()) {
                        request = removeFilm(userInput);
                        valid = true;
                    }
                    break;
                case "9":
                    if (loggedIn && user.isAdminStatus()) {
                        System.out.println("Are you sure you want to shutdown server? (Y/N)");
                        String shutdownChoice = userInput.nextLine().toUpperCase();
                        if (shutdownChoice.equals("Y")) {
                            request = FilmService.SHUTDOWN;
                            valid = true;
                        }
                    }
                    break;
                case "10":
                    request = searchByRating(userInput);
                    valid = true;
                    break;
                default:
                    System.out.println("Please select one of the options from the menu!");
            }
        }
        return request;
    }

    public static void processResponse(String response, Scanner userInput) {
        switch (response) {
            case FilmService.SUCCESS_ADMIN:
                System.out.println("Successfully logged in as admin.");
                loggedIn = true;
                user.setAdminStatus(true);
                break;
            case FilmService.SUCCESS_USER:
                System.out.println("Successfully logged in as user.");
                loggedIn = true;
                user.setAdminStatus(false);
                break;
            case FilmService.SUCCESS_LOGOUT:
                System.out.println("Successfully logged out.");
                loggedIn = false;
                break;
            case FilmService.ADDED:
                System.out.println("Film added successfully.");
                break;
            case FilmService.REJECTED:
                System.out.println("Failed to add film. Film already exists.");
                break;
            case FilmService.LOGGED_OUT:
                System.out.println("You have been logged out.");
                loggedIn = false;
                break;
            case FilmService.NOT_LOGGED_IN:
                System.out.println("You are not logged in.");
                loggedIn = false;
                break;
            case FilmService.SUCCESS_SHUTDOWN:
                System.out.println("Server shutdown successful.");
                loggedIn = false;
                validClient = false;
                break;
            case FilmService.NO_MATCH:
                System.out.println("No matching films found.");
                break;
            default:
                System.out.println("Unknown response: " + response);
        }
    }

    public static String register(Scanner userInput) {
        System.out.println("Register: ");
        System.out.println("Enter username: ");
        String username = userInput.nextLine();
        System.out.println("Enter password: ");
        String password = userInput.nextLine();
        return FilmService.REGISTER + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
    }

    public static String login(Scanner userInput) {
        System.out.println("Login: ");
        System.out.println("Enter username: ");
        String username = userInput.nextLine();
        System.out.println("Enter password: ");
        String password = userInput.nextLine();
        return FilmService.LOGIN + FilmService.DELIMITER + username + FilmService.DELIMITER + password;
    }

    public static String rateFilm(Scanner userInput) {
        System.out.println("Enter film title: ");
        String title = userInput.nextLine();
        int rating = getValidRating(userInput, "Rate the film from 1 to 10:");
        return FilmService.RATE + FilmService.DELIMITER + title + FilmService.DELIMITER + rating;
    }

    public static String addFilm(Scanner userInput) {
        System.out.println("Add a film: ");
        System.out.println("Enter title: ");
        String title = userInput.nextLine();
        System.out.println("Enter genre: ");
        String genre = userInput.nextLine();
        return FilmService.ADD_FILM_REQUEST + FilmService.DELIMITER + title + FilmService.DELIMITER + genre;
    }

    public static String removeFilm(Scanner userInput) {
        System.out.println("Remove a film: ");
        System.out.println("Enter title: ");
        String title = userInput.nextLine();
        return FilmService.REMOVE_FILM_REQUEST + FilmService.DELIMITER + title;
    }

    public static String searchByRating(Scanner userInput) {
        int rating = getValidRating(userInput, "Enter rating (1 to 10):");
        return FilmService.RATE_FILM_REQUEST + FilmService.DELIMITER + rating;
    }

    public static int getValidRating(Scanner userInput, String prompt) {
        int value = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println(prompt);
            try {
                value = userInput.nextInt();
                if (value >= 1 && value <= 10) {
                    valid = true;
                } else {
                    System.out.println("Please enter a rating between 1 and 10.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 10.");
                userInput.nextLine();
            }
        }
        userInput.nextLine();
        return value;
    }
}