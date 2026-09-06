package com.kai.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConfig {

	private static final EntityManagerFactory FACTORY =
			Persistence.createEntityManagerFactory("52HZ-platform");

	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
	}

	public static void shutdown() {
		if (FACTORY != null && FACTORY.isOpen()) {
			FACTORY.close();
		}
	}
}