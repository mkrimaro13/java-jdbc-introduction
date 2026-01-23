package jdbc.introduction.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class UtilEntity {
  private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

  private static EntityManagerFactory buildEntityManagerFactory() {
    return Persistence.createEntityManagerFactory("MySqlPersistenceUnit");
  }

  public static EntityManager getEntityManager(){
    return entityManagerFactory.createEntityManager();
  }

}
