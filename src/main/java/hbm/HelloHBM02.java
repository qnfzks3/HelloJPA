package hbm;


import model.SungJuk;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;


public class HelloHBM02 {
    public static void main(String[] args) {
        // SessiontFactory 초기화



        Configuration cfg = new Configuration().configure();    //- 설청 파일 불러옴
        SessionFactory sf = cfg.addAnnotatedClass(SungJuk.class).buildSessionFactory();
        // 모델이 있는 성적 클래스를 가져와서 객체 생성
        //만약 .addAnnotatedClass 을 연속으로 계속 붙이면 계속 해서 클래스 타입 여러개를 가져와 사용가능하다.
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