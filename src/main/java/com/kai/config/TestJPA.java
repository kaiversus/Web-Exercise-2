package com.kai.config;

import com.kai.entity.Category;
import com.kai.entity.Video;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class TestJPA {

	public static void main(String[] args) {
		EntityManager enma = JPAConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();

		Category cate = new Category();
		cate.setCategoryname("Iphone");
		cate.setImages("abc.jpg");
		cate.setStatus(1);

		Video video = new Video();
		video.setVideoId("v01");
		video.setTitle("test");
		video.setCategory(cate);

		try {
			trans.begin();
			enma.persist(cate);
			enma.persist(video);
			trans.commit();
			System.out.println("OK - đã insert, categoryid = " + cate.getCategoryid());
		} catch (Exception e) {
			e.printStackTrace();
			if (trans.isActive()) {
				trans.rollback();
			}
		} finally {
			enma.close();
			JPAConfig.shutdown();
		}
	}
}