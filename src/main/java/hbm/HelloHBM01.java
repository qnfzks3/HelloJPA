package hbm;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;


public class HelloHBM01 {
    public static void main(String[] args) {
        // SessiontFactory 초기화
        String fpath = "src/main/resources/mapping/sungjuk.hbm.xml";


        Configuration cfg = new Configuration().configure();    //경로로 자동으로 담아옴
        SessionFactory sf = cfg.addFile(fpath).buildSessionFactory(); //
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