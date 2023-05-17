package dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import model.QSungJuk;
import model.SungJuk;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class HelloQueryDSL01 {
    public static void main(String[] args) {
        // jpa 객체 초기화 : emf -> em -> tx
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            //쿼리 객체 준비

            QSungJuk qsj=QSungJuk.sungJuk;   //
            JPAQueryFactory query = new JPAQueryFactory(em);

            //조회
            List<SungJuk> sjs =query.selectFrom(qsj).fetch();
            System.out.println(sjs);

            //필터링
            sjs=query.selectFrom(qsj).where(qsj.name.eq("지현")).fetch(); //이렇게 하면 혜교인 학생의 성적 데이터가 나온다.

            System.out.println(sjs);




        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

    }
}