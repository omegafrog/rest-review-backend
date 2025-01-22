package org.example.sbb.util;

import jakarta.persistence.EntityManager;

public class Util {
    public static class Test{
        public static void setUp(EntityManager em){
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

            em.createNativeQuery("TRUNCATE TABLE QUESTION").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE ANSWER").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE QUESTION_VOTER").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE ANSWER_VOTER").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE MEMBER").executeUpdate();
            em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
        }
    }
}
