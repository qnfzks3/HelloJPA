package jpa;

import javax.persistence.*;
import java.util.List;

public class HelloJPA03 {


    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        try{
            //사원 데이터 조회 - TypedQuery
            //createQuery(질의문, 리턴될 객체 종류)

            String jpql = "select e from Employees as e";
            //Employees 는 클래스 이름 , 해석 - Employee를 e 라고 명명한다.

            List<Employees> emps = em.createQuery(jpql,Employees.class).getResultList();
            //getResultList() 리스트로 받아온다.

            //for(Employees emp : emps)
            // System.out.println(emp);
//
            jpql = "select fname,deptid,hdate from Employees e";
            List<Object[]> items =  em.createQuery(jpql).getResultList(); //전체 조회는 Employees로 특정 칼럼은 Object[]로

            for(Object[] item : items)
                System.out.println(item[0]+"/"+item[1]+"/"+item[2]);


            //사원 직책 조회 - jobid가 IT_PROG인 사원
            //파라메터 바인딩 - : 파라메터명
            jpql = "select  e from Employees e where jobid = : jobid";   //이름 기반
            //jpql = "select  e from Employees e where jobid = ?1";   //위치 기반 ? 순번

            TypedQuery<Employees> query = em.createQuery(jpql, Employees.class);
            query.setParameter("jobid","IT_PROG");
            //query.setParameter("1","IT_PROG");

            emps= query.getResultList();

            System.out.println("**jobid가 IT_PROG인 사원**");
            for(Employees emp : emps)
                System.out.println(emp);

            //사원 직책수 조회
            //jpql = "select count( jobid) from Employees e";


            jpql = "select count(distinct jobid) from Employees e"; // 절댓값, - 총 jobid 개수
            Long cntjob= em.createQuery(jpql,Long.class).getSingleResult();

            System.out.println("****직책수 조회*****");
            System.out.println(cntjob);

            //페이징    ex)jobid으로 정렬 후 3페이지 페이징 조회 (페이징당 출력 건수 : 15)-> 0~15 , 16~30,31~45
            //setFirtResult(startpos) : 페이징 시작 위치
            //setMaxResult(cntdata)  : 조회할 데이터 수

            /*jpql = "select e from Employees e order by jobid";
            List<Employees> pemps=em.createQuery(jpql,Employees.class)
                    .setFirstResult(30).setMaxResults(15).getResultList();

            for(Employees emp : pemps)
                System.out.println(emp);*/

            //직책별 평균 연봉 사원수 조회
            jpql = "select jobid,avg(sal),count(jobid) from Employees e group by jobid";
            List<Object[]> itemps=em.createQuery(jpql).getResultList();

            for (Object[] item : itemps)
                System.out.println(item[0]+"/"+item[1]+"/"+item[2]);

            // 사원이름 , 직책, 부서명 조회: join
            jpql = "select e.fname,e.jobid,e.dname from Employees e inner join e.deptid d"; /*클래스의 별칭을 해당컬럼에 주어야함*/
            List<Object[]> items2=em.createQuery(jpql).getResultList();

            for (Object[] item : items2)
                System.out.println(item[0]+"/"+item[1]+"/"+item[2]);


        }catch (Exception ex){
            ex.printStackTrace();

        }finally {
            em.close();
            emf.close();
        }

    }
}
