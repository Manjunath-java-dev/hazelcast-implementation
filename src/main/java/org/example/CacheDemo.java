package org.example;

import entity.Employee;
import jakarta.persistence.*;

public class CacheDemo {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hazelcast-example");

        // Save one record
        EntityManager em0 = emf.createEntityManager();
        em0.getTransaction().begin();
        em0.persist(new Employee(0,"ravi",1000));
        em0.getTransaction().commit();
        em0.close();

        // EntityManager 1: hits DB (SQL printed)
        EntityManager em1 = emf.createEntityManager();
        Employee e1 = em1.find(Employee.class, 1);
        System.out.println("EM 1: " + e1.getName());
        em1.close();

        // EntityManager 2: served from second-level cache (NO SQL printed)
        EntityManager em2 = emf.createEntityManager();
        Employee e2 = em2.find(Employee.class, 1);
        System.out.println("EM 2: " + e2.getName());
        em2.close();

        // Check whether the entity is in the L2 cache
        System.out.println("In L2 cache? " + emf.getCache().contains(Employee.class, 1));

        emf.close();
    }
}
