import business.Film;
import business.FilmManager;
import junit.framework.TestCase;

import java.util.ArrayList;

public class FilmManagerTest extends TestCase {
    private FilmManager filmManager;

    public void setUp() throws Exception {
        super.setUp();
        filmManager = new FilmManager();
        filmManager.addFilm(new Film("film1", "genre1",1.1,2));
        filmManager.addFilm(new Film("film2", "genre2",3.2,2));
        filmManager.addFilm(new Film("film3", "genre3",5.43,5));
    }

    public void tearDown() throws Exception {
        filmManager = null;
        super.tearDown();
    }

    public void testAddFilm() {
        Film newFilm = new Film("new film", "genre",2.3,1);
        assertTrue(filmManager.addFilm(newFilm));
        assertTrue(filmManager.getAllFilms().contains(newFilm));
    }

    public void testRemoveFilm() {
        assertTrue(filmManager.removeFilm("film"));
        assertNull(filmManager.searchByTitle("film1"));
    }



    public void testSearchByTitle() {
        Film film = filmManager.searchByTitle("film1");
        assertNotNull(film);
        assertEquals("film1", film.getTitle());
    }

    public void testSearchByGenre() {
        ArrayList<Film> films = filmManager.searchByGenre("genre1");
        assertEquals(2, films.size());
        assertTrue(films.contains(filmManager.searchByTitle("film1")));
        assertTrue(films.contains(filmManager.searchByTitle("film3")));
    }
}
