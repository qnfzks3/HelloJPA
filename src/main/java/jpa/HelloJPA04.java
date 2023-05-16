package jpa;

import model.Employees;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class HelloJPA04 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        try {
            //Citeria  사용준비
            CriteriaBuilder cb= em.getCriteriaBuilder(); //대부분 이런식

            //사원 데이터테이블 조회

            CriteriaQuery<Employees> query = cb.createQuery(Employees.class);  //조회대상 지정
            Root<Employees> e=query.from(Employees.class);

            CriteriaQuery<Employees> cq = query.select(e);
            List<Employees> emps = em.createQuery(cq.select(e)).getResultList();

            System.out.println("사원 전체 조회");
            for (Employees emp : emps)
                System.out.println(emp);

            //사원 데이터 조회 - 이름 , 부서번호 , 입사일 : multiselect <-select와는 다름
            //커럼 지정 : 객체.get(변수명)
            CriteriaQuery<Object[]> mcq = cb.createQuery(Object[].class);

            Root<Employees> me = mcq.from(Employees.class);
            mcq.multiselect(e.get("fname"),e.get("deptid"),e.get("hdate")); //get으로 가져올 걸 생성자가 있어야한다.
            List<Object[]> items = em.createQuery(mcq).getResultList();

            System.out.println("이름 부서번호 입사일");
            for (Object[] item : items)
                System.out.println(item);

            //정렬 조회 : 부서번호 기준, orderby

            Order deptid = cb.desc(e.get("deptid"));
            cq=query.select(e).orderBy(deptid);

            emps = em.createQuery(cq).getResultList();

            for (Employees emp : emps)
                System.out.println(emp);

            //조건검색 : 직책인 IT_PROG인 사원 조회 , where
            Predicate jobid = cb.equal(e.get("jobid"),"IT_PROG");
            cq=query.select(e).where(jobid);

            emps = em.createQuery(cq).getResultList();

            System.out.println("IT_PROG가 직책인 사원");
            for (Employees emp : emps)
                System.out.println(emp);



            // 조건 검색 : 연봉이 20000 이상인 사원 조회
            Predicate salGT = cb.ge(e.get("sal"),20000);
            cq=query.select(e).where(salGT);

            emps = em.createQuery(cq).getResultList();

            System.out.println("연봉이 20000 이상인 사원 조회");
            for (Employees emp : emps)
                System.out.println(emp);

            
            //직책 수 조회1
            cb= em.getCriteriaBuilder();
            query = cb.createQuery(Employees.class);
            e=query.from(Employees.class);      // 만약 이렇게 3줄(초기화 작업) 없다면 초기화안되고 위에 지시한 SELECT 문 유지되서 출력한다.
                                                // 여기 없이하면 위에 연봉 2만이상인도 포함되어버린다.

            Expression cntJob = cb.count(e.get("jobid"));
            cq=query.select(cntJob);
            List<Employees> cnt = em.createQuery(cq).getResultList();

            System.out.println("직책수 조회1");
            System.out.println(cnt);
            
            //직책 수 조회2 :distinct
            cntJob = cb.count(e.get("jobid"));
            cq=query.select(e.get("jobid")).distinct(true);
            cnt = em.createQuery(cq).getResultList();

            System.out.println("직책수 조회2");
            System.out.println(cnt);


            //직책 수 조회3 :countDistinct
            cntJob = cb.countDistinct(e.get("jobid"));
            cq=query.select(cntJob);
            cnt = em.createQuery(cq).getResultList();

            System.out.println("직책수 조회3");
            System.out.println(cnt);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

    }
}