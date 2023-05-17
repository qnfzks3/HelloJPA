package dsl;


import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import model.Employees;
import model.QDepartments;
import model.QEmployees;
import org.apache.commons.lang.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class HelloQueryDSL02 {
    public static void main(String[] args) {
        // jpa 객체 초기화 : emf -> em -> tx
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            //쿼리 객체 준비
            QEmployees qemp=QEmployees.employees;   //qemp 이거 자체가 테이블이라 생각하면됨
            QDepartments qdept= QDepartments.departments;  //qdept 이것도 그냥 테이블이다 지정
            JPAQueryFactory query = new JPAQueryFactory(em);  //jpa 쿼리 작성을 지원하는 라이브러리 사용 한다 선언 -sql과 비슷

            //조회 - 전체 사원 조회
            List<Employees> emps =query.selectFrom(qemp).fetch();


            System.out.println("사원 전체 조회");
            for (Employees e: emps)
                System.out.println(e);

            
            //조회 - 일부 사원 , 페이징 (offset)
            List<Employees> emps2 =query.selectFrom(qemp).offset(30).limit(15).fetch();  // 3page에 해당하는 것만 나옴 - 15개


            System.out.println(" 일부 사원 , 페이징 (offset)");
            for (Employees e: emps2)
                System.out.println(e);
            

            
            //사원 데이터 조회 - query : 이름 , 부서번호 , 입사일
            query.selectFrom(qemp);// <- From 은 전체 데이터를 셀렉해서 받아오는거 - selectFrom (전체)-<List> , select(부분 데이터)-<Tuple>
                        // selectFrom 은 Tuple ,   select는 list - Employees      설명: selectfrom은 전체를 나타내니깐 , *와 같다.
            List<Tuple> items = query.select(qemp.fname, qemp.deptid , qemp.hdate).from(qemp).fetch(); 
                                                                                  //select;에서 from(qemp) 하면 엔티티에 직접 접근


            for (Tuple item : items)
                System.out.println(item);


            //정렬 : orderby, 부서번호 기준


            List<Employees> deptemp=query.selectFrom(qemp).orderBy(qemp.deptid.desc()).fetch();

            System.out.println("orderby, 부서번호 기준");
            for (Employees e: deptemp)
                System.out.println(e);

            
            //조건 검색 : where, 직책인 IT_PROG인 사원 조회


            List<Employees> jobprog = query.selectFrom(qemp).where(qemp.jobid.eq("IT_PROG")).fetch();
                                  // ==> select * from emp where jobid = IT_PROG ;
            for (Employees e: jobprog)
                System.out.println(e);



            //조건 검색 : 연봉이 20000 이상인 사원 조회

            List<Employees> salemp=query.selectFrom(qemp).where(qemp.sal.goe(20000)).fetch();
            for (Employees e: salemp)
                System.out.println(e);

            //직책 수 조회 1

            List<Long> cnt = query.select(qemp.jobid.count()).from(qemp).fetch(); // 만약에 from(qemp)로 엔티티에 직접 접근 하는게 아니라면
                                                                // jobid를 칼럼명인 JOB_ID로 바꿔줘도 된다.

            System.out.println(cnt);

            //직책 수 조회 2 - 리스트로 안만들고 변수로 만듬

            long cnt2 = query.select(qemp.jobid).from(qemp).fetchCount();  //그냥 숫자이기 때문에 변수로 지정하고 타입을 long으로 함
            System.out.println(cnt2);


            //직책 수 조회 3 - 중복 허용 안함
            List<Long> cnt3=query.select(qemp.jobid.countDistinct()).from(qemp).fetch();
            System.out.println(cnt3);
            
            //직책 수 조회 4 - 중복허용 안하고 변수로 출력

            long cnt4=query.select(qemp.jobid).distinct().from(qemp).fetchCount();
            System.out.println(cnt4);


            //그룹핑 : 직책별   최대 연봉, 최소 연봉, 평균 연봉 , 직책수 조회
            List<Tuple> grouping =query.select(qemp.jobid,qemp.sal.max(), qemp.jobid,qemp.sal.min(),
                            qemp.sal.avg(),qemp.jobid.count())
                            .from(qemp).groupBy(qemp.jobid).fetch();


            System.out.println("그룹핑 : 직책별   최대 연봉, 최소 연봉, 평균 연봉 , 직책수 조회");
            for (Tuple e: grouping)
                System.out.println(e);


            //그룹핑2 : 직책별   최대 연봉, 최소 연봉, 평균 연봉 , 직책수 조회

            StringPath jbcnt = Expressions.stringPath("jbcnt"); //별칭정의

            List<Tuple> grouping2 =query.select(qemp.jobid,qemp.sal.max(), qemp.jobid,qemp.sal.min(),
                            qemp.sal.avg(),qemp.jobid.count().as("jbcnt"))
                    .from(qemp).groupBy(qemp.jobid).orderBy(jbcnt.desc()).fetch();


            System.out.println("그룹핑2 : 직책별   최대 연봉, 최소 연봉, 평균 연봉 , 직책수 조회");
            for (Tuple e: grouping2)
                System.out.println(e);


            //서브쿼리1 : 평균연봉보다 작게 받는 사원들 조회





            //서브쿼리 - 평균 연봉은?
            JPQLQuery<Double> subqry=JPAExpressions.select(qemp.sal.avg()).from(qemp);

            System.out.println("평균연봉");
            System.out.println(subqry); //이렇게 해주니 안나온다. - 그저 평균 연봉을 검색해주는 쿼리문만 나오게 되는데,이렇게 쿼리문을 아래에
                                            //넣어줘서 두개의 조건을 조회한다. 평균 연봉 , 평균 연봉 미만 사원조회




            //주쿼리 - 사원 조회 - 평균 연봉보다 적게 받는 사원 조회
            List<Employees> emps3=query.selectFrom(qemp).where(qemp.sal.lt(subqry)).fetch();

            System.out.println("평균 연봉보다 적게 받는 사원 조회");
            for (Employees e: emps3)
                System.out.println(e);



            //서브쿼리2 : IT 부서에 근무중인 사원들의 이름 , 직책 , 급여 조회 - 다른 테이블(외래키)
            JPQLQuery<Long> subqry2=JPAExpressions.select(qdept.deptid ).where(qdept.dname.eq("IT")).from(qdept);

            //주쿼리 2
            List<Tuple> items2=query.select(qemp.fname,qemp.jobid,qemp.sal).from(qemp).where(qemp.deptid.eq(subqry2)).fetch();

            System.out.println("IT 부서에 근무중인 사원들의 이름 , 직책 , 급여 조회 ");
            for (Tuple e: items2)
                System.out.println(e);


            //이제 join으로 풀어보자  - 두개의 테이블 - 부서번호가 60번인 사원들의 이름 , 직책, 부서명 조회
            query=new JPAQueryFactory(em);
            //select fname , jobid,dname from employees qemp
            //inner join department d on (qemp.DEPARTMENT_ID = d.DEPARTMENT_ID) - employees의 DEPARTMENT_ID 와 department의 DEPARTMENT_ID는 같은 칼럼이다.
            // where deptid = 60L

            List<Tuple> items3=query.select().select(qemp.fname,qemp.jobid,qdept.dname)
                    .from(qemp)
                    .join(qemp.department,qdept)                     //join(qemp.department,qdept)  이렇게 department 데이터 가져오기
                    .where(qemp.deptid.eq(60L))      //L 은 long의 약자 - long 타입이라는 뜻
                    .fetch();
            System.out.println("IT 부서에 근무중인 사원들의 이름 , 직책 , 급여 조회 2");
            for (Tuple e: items3)
                System.out.println(e);



            String fname=null;
            String jobid = "IT_PROG";
            Integer sal = null;

            query= new JPAQueryFactory(em);
            List<Employees> emps4=query.selectFrom(qemp)
                    .where(
                            (fname != null)? qemp.fname.contains(fname) : null,
                            StringUtils.isNotEmpty(jobid) ? qemp.jobid.eq(jobid): null,
                            (sal!= null) ? qemp.sal.gt(sal) : null


                    ).fetch();
            System.out.println(emps4);

            // 동적 쿼리
            // 직책이 IT_PROG 인 사원 조회
            // 연봉이 10000이상인 사원 조회
            // 직책이 IT_PROG이고 연봉이 6000 이상인 사원 조회
            String fname = "Ste";
            String jobid = null;
            Integer sal = null;

            query = new JPAQueryFactory(em);
            List<Employee> emps = query.selectFrom(qemp)
                    .where(
                            (fname != null) ?  qemp.fname.contains(fname) : null,
                            StringUtils.isNotEmpty(jobid) ? qemp.jobid.eq(jobid): null,
                            (sal != null) ? qemp.sal.gt(sal) : null
                    ).fetch();

            System.out.println(emps);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }

    }
}