package hbm;


import model.SungJuk;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;


public class HelloHBM03 {
    public static void main(String[] args) {  //jpa 설정을 이용해서 hibnation 을 만든다.
      
        //jpa 설정 파일 읽어옴
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        EntityManager em = emf.createEntityManager();


        //jpa에서 생성된 em을 session 객체로 전환

        Session session =em.unwrap(Session.class); //세션변수 session으로 이름 지정
        SessionFactory sf = session.getSessionFactory();
        Session sess = sf.openSession();

        try {
            //hql을 이용한 조회
            Query query=sess.createQuery("from SungJuk ");   //쿼리 코드를 이렇게 만 써도 sql문 안쓰고 사용가능

            List sjs = query.list();

            System.out.println(sjs);

            query= sess.createQuery("from SungJuk where name = ?1");  //위치 기반으로 검색
            
            query.setParameter(1,"혜교");                   // 데이터로 가져와서 
            sjs = query.list();                               // 리스트에 담아옴
 
            System.out.println(sjs);                         // 출력



        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sess.close();
            sf.close();
        }

    }
}