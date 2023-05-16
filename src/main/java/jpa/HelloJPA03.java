package jpa;

import model.Employees;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class HelloJPA03 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        try {
            // 사원 데이터 조회 - TypedQuery
            // createQuery(질의문, 리턴될객체종류)
            String jpql = "select e from Employees as e";
            //List<Employees> emps =
            //        em.createQuery(jpql, Employees.class).getResultList();

            //for (Employees emp : emps)
            //    System.out.println(emp);

            // 사원 데이터 조회 - Query : 이름, 부서번호, 입사일
            // createQuery(질의문)
            jpql = "select fname, deptid, hdate from Employees e";
            //List<Object[]> items = em.createQuery(jpql).getResultList();

            //for (Object[] item : items)
            //    System.out.println(item[0] + "/" + item[1] + "/" + item[2]);

            // 사원 직책 조회 - jobid가 IT_PROG인 사원
            // 파라메터 바인딩 - :파라메터명, ?순번
            jpql = "select e from Employees e where jobid = :jobid"; // 이름기반
            //jpql = "select e from Employees e where jobid = ?1";   // 위치기반

            //TypedQuery<Employees> query = em.createQuery(jpql, Employees.class);
            //query.setParameter("jobid", "IT_PROG");
            //query.setParameter(1, "IT_PROG");

            //emps = query.getResultList();

            //for (Employees emp : emps)
            //    System.out.println(emp);

            // 사원 평균 연봉 조회 - TypedQuery
            jpql = "select avg(sal) from Employees e";
            //Double avgsal = em.createQuery(jpql, Double.class).getSingleResult();

            //System.out.println(avgsal);

            // 사원 직책수 조회
            //jpql = "select count(jobid) from Employees e";
            jpql = "select count(distinct jobid) from Employees e";
            //Long cntjob = em.createQuery(jpql, Long.class).getSingleResult();

            //System.out.println(cntjob);

            // jobid으로 정렬후 3 페이지 조회 : 페이징 (페이지당 출력건수 : 15)
            // setFirstResult(startpos) : 페이징 시작 위치
            // setMaxResult(getdatacnt) : 조회할 데이터 수
            jpql = "select e from Employees e order by jobid";
            //List<Employees> pemps = em.createQuery(jpql, Employees.class)
            //    .setFirstResult(30).setMaxResults(15).getResultList();

            //for (Employees emp : pemps)
            //    System.out.println(emp);


            // 직책별 평균연봉, 사원수 조회
            jpql = "select jobid, avg(sal), count(jobid) from Employees e group by jobid";
            //List<Object[]> items = em.createQuery(jpql).getResultList();

            //for (Object[] item : items)
            //    System.out.println(item[0] + "/" + item[1] + "/" + item[2]);


            // 사원이름, 직책, 부서명 조회 : join
            //jpql = " select e.fname, e.jobid, e.deptid, d.dname from Employees e " +
            //       " inner join e.Departments d ";
            //List<Object[]> items = em.createQuery(jpql).getResultList();

            //for (Object[] item : items)
            //    System.out.println(item[0] + "/" + item[1] + "/" + item[2] + "/" + item[3]);


            // 부서번호가 60번인 사원들의 이름, 직책, 부서명 조회 : join
            //jpql = " select e.fname, e.jobid, d.dname from Employees e " +
            //       " inner join e.Departments d where e.deptid = 60";
            //List<Object[]> items = em.createQuery(jpql).getResultList();

            //for(Object[] item : items)
            //    System.out.println(item[0] + "/" + item[1] + "/" + item[2]);

            // 부서명이 IT인 사원의 사번과 입사일 조회 : 서브쿼리
            jpql = " select empid, hdate from Employees e where deptid = " +
                    " (select deptid from Departments d where dname = 'IT') ";
            List<Object[]> items = em.createQuery(jpql).getResultList();

            for(Object[] item : items)
                System.out.println(item[0] + "/" + item[1]);

            // 제공된 이름, 직책, 연봉으로 사원 조회 : 동적 쿼리
            String fname = "";
            String jobid = "";
            Integer sal = 0;  // null 체크를 위해 클래스형으로 선언

            jpql = "select e from Employees e";
            List<String> cndtns = new ArrayList<>();  // 조건절 저장 변수

            if (fname != null) {
                cndtns.add(" fname like concat('%', :fname, '%') ");
            }
            if (jobid != null) {
                cndtns.add(" jobid = :jobid ");
            }
            if (sal != null) {
                cndtns.add(" sal >= :sal ");
            }

            if (!cndtns.isEmpty()) { // 조건식이 하나라도 존재한다면
                jpql += " where " + String.join(" and ", cndtns);
            }

            TypedQuery<Employees> query =
                    em.createQuery(jpql, Employees.class);
            if (fname != null) {
                query.setParameter("fname", fname);
            }
            if (jobid != null) {
                query.setParameter("jobid", jobid);
            }
            if (sal != null) {
                query.setParameter("sal", sal);
            }

            List<Employees> emps = query.getResultList();
            for (Employees e : emps) {
                System.out.println(e);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

    }
}