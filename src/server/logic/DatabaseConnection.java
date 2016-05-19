package server.logic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by Jonas on 2016-05-19.
 */

public class DatabaseConnection {

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction etx;

    public DatabaseConnection() {
        emf = Persistence.createEntityManagerFactory("JPA");
        em = emf.createEntityManager();
    }

    /**
     * Close the database connection.
     */
    public void closeDB() {
        emf.close();
        em.close();
    }

}
