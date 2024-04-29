package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FilmManager {
    HashMap<String, Film> films;
    public FilmManager(){
        films = new HashMap<>();
        bootstrapFilms();
    }
    private void bootstrapFilms() {
        Film film1 = new Film("yebudy","lightweight",1,2);
        films.put(film1.getTitle(), film1);
    }

    /**
     * Method to add films to hashmap
     * @param film
     * @return true if added false if already exitst
     */
    public boolean addFilm(Film film) {
        if (films.containsKey(film.getTitle())) {
            System.out.println("Film already exists with title: " + film.getTitle());
            return false;
        } else {
            films.put(film.getTitle(), film);
            System.out.println("Film added successfully: " + film.getTitle());
            return true;
        }
    }

    /**
     * method to remove film
     * @param title
     * @return true if deleted false if not found
     */
    public boolean removeFilm(String title) {
        if (films.containsKey(title)) {
            films.remove(title);
            System.out.println("Film removed successfully: " + title);
            return true;
        } else {
            System.out.println("Film not found with title: " + title);
            return false;
        }
    }

    /**
     * rates a film.
     * @param title
     * @param rating
     * @return true if the film exists and was rated, false if the film doesn't exist
     */
    public boolean rateFilm(String title, double rating) {
        Film film = films.get(title);
        if (film != null) {
            double totalRatings = film.getTotalRatings() + rating;
            int numberOfRatings = film.getNumberOfRatings() + 1;
            film.setTotalRatings(totalRatings);
            film.setNumberOfRatings(numberOfRatings);
            films.put(title, film);
            return true;
        }
        return false;
    }

    /**
     * searches for a film based on its title.
     * @param title
     * @return The film if it was found, or null if no film was found
     */
    public Film searchByTitle(String title) {
        return films.get(title);
    }

    /**
     * searches for all films that match a particular genre
     * @param genre
     * @return An ArrayList of films sorted in ascending order by the film titles.
     */
    public ArrayList<Film> searchByGenre(String genre) {
        ArrayList<Film> filmList = new ArrayList<>();
        for (Map.Entry<String, Film> entry : films.entrySet()) {
            Film film = entry.getValue();
            if (film.getGenre().equalsIgnoreCase(genre)) {
                filmList.add(film);
            }
        }
        Collections.sort(filmList, (f1, f2) -> f1.getTitle().compareToIgnoreCase(f2.getTitle()));
        return filmList;
    }
    /**
     * Calculates the average rating of all films.
     *
     * @return The average rating of all films.
     */
    public double getAverageRating() {
        double totalRatings = 0;
        int numberOfFilms = 0;
        for (Map.Entry<String, Film> entry : films.entrySet()) {
            Film film = entry.getValue();
            totalRatings += film.getTotalRatings();
            numberOfFilms++;
        }
        return numberOfFilms > 0 ? totalRatings / numberOfFilms : 0;
    }
    /**
     * Searches for all films with a specific rating.
     *
     * @param rating the rating to search for
     * @return An ArrayList of films with the specified rating.
     */
    public ArrayList<Film> searchByRating(int rating) {
        ArrayList<Film> filmList = new ArrayList<>();
        for (Map.Entry<String, Film> entry : films.entrySet()) {
            Film film = entry.getValue();
            if (film.getTotalRatings() == rating) {
                filmList.add(film);
            }
        }
        return filmList;
    }

    /**
     * Retrieves all films stored in the film manager.
     *
     * @return An ArrayList containing all films.
     */
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }


    }

