package server.logic;

import server.datamodel.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

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

    /**
     * Persist a user to the database.
     *
     * @param user = the user to create.
     */
    public void createUser(User user){
        etx = em.getTransaction();
        etx.begin();
        em.persist(user);
        etx.commit();
    }

    /**
     * Get a specific user.
     *
     * @param username = the username
     * @return = the user if exists, otherwise null.
     */
    public User getUser(String username) {
        List result = em.createNamedQuery("getUser").setParameter("userName", username).getResultList();
        if (result.size() > 0) {
            return (User) result.get(0);
        } else {
            return null;
        }
    }

    /**
     * Update an already existing user.
     *
     * @param user = user to update.
     */
    public void updateUser(User user){
        etx = em.getTransaction();
        etx.begin();
        em.merge(user);
        etx.commit();
    }

    public List<User> getHighscore(){
        return em.createNamedQuery("getHighscore").setMaxResults(10).getResultList();
    }

}
