package jpa;

import model.SungJuk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class HelloJPA02 {
    public static void main(String[] args) {
        // jpa 객체 초기화 : emf -> em -> tx
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 데이터 추가 : persist(대상)
            SungJuk sj = new SungJuk();
            sj.setName("혜교");
            sj.setKor(87);
            sj.setEng(78);
            sj.setMat(67);

            tx.begin();
            em.persist(sj);
            tx.commit();

            // 데이터 조회 : find(객체, 기본키값)
            tx.begin();
            sj = em.find(SungJuk.class, 1);
            System.out.println(sj);
            tx.commit();

            // 데이터 수정 : setXxx(변경값)
            tx.begin();
            sj = em.find(SungJuk.class, 2);  // 수정할 객체 찾음
            sj.setName("지현");   // 값 변경
            em.persist(sj);
            tx.commit();

            // 데이터 삭제 : remove(대상)
            tx.begin();
            sj = em.find(SungJuk.class, 3);  // 삭제할 객체 찾음
            em.remove(sj);   // 대상 삭제
            tx.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

    }
}