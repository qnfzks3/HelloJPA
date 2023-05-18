package hbm;


import model.Departments;
import notmap.Empinfo;
import model.Employees;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;


public class HelloHBM04 {
    public static void main(String[] args) {
        // SessiontFactory 초기화



        Configuration cfg = new Configuration().configure();    //- 설청 파일 불러옴
        SessionFactory sf = cfg.addAnnotatedClass(Employees.class).addAnnotatedClass(Departments.class).buildSessionFactory();
        // 모델이 있는 사원, 부서 클래스를 가져와서 객체 생성
        //만약 .addAnnotatedClass 을 연속으로 계속 붙이면 계속 해서 클래스 타입 여러개를 가져와 사용가능하다.
        Session sess = sf.openSession();

        try {
            //hql을 이용한 조회

            Query query = sess.createQuery("from Employees");//데이터 전체 조회
            List <Employees> emps = query.list();

            for (Employees emp : emps)
                System.out.println(emp);

            //조회 - 부분 조회1 : 이름 , 연봉

            Query query1 =sess.createQuery("select fname,sal from Employees");  //sql문을 실행해서 데이터를 가져와서
            List<Object[]> items = query1.getResultList();  //getResultList() = 위에서 받아온 데이터를 Object에 넣어준다.

            System.out.println("부분 조회1 : 이름 , 연봉");  
            for (Object[] item : items)                        // 출력
                System.out.println(item[0]+"/"+item[1]);

            //조회 - 부분 조회 2 : 이름 , 연봉

            Query query2=sess.createQuery("select new notmap.Empinfo(fname, sal) from Employees ");

            List<Empinfo> items2 = query2.getResultList();

            System.out.println("부분 조회 2 : 이름 , 연봉");
            for (Empinfo e: items2)
                System.out.println(e);
            
            //3. 조건검색 : where , 직책이 IT_PROG인 사원 조회
            Query query3= sess.createQuery("from Employees where jobid=?1"); //조건검색은 전체 조회 해서

            query3.setParameter(1,"IT_PROG");        // 파라메터로 데이터를 가져옴

            List<Employees> emps3 = query3.getResultList();
            for (Employees e : emps3)
                System.out.println(e);

            //4.조건 검색 : 연봉이 20000이상인 사원 조회
            Query query4 = sess.createQuery("from Employees  where  sal >= 20000");
            List<Employees> emps4= query4.getResultList();

            System.out.println(emps4);

            //5. 정렬: orderby 부서 번호 기준
            Query query5 = sess.createQuery("from Employees  order by  deptid desc");
            List<Employees> emps5 = query5.getResultList();

            for (Employees e : emps5)
                System.out.println(e);

            //6.직책 수 조회

            String hql = "select count(distinct jobid) from Employees";
            Query query6 = sess.createQuery(hql);
            List cntjob=query6.getResultList();

            System.out.println(cntjob);

            //7. 그룹핑 : 직책별 최대 ,최소 , 평균 연봉 , 직책수 조회


            Query query7 = sess.createQuery("select jobid,max(sal),min(sal), count(jobid) from Employees group by jobid" );
            List<Object[]> items7 =query7.getResultList();

            for (Object[] item : items7)
                System.out.println(item[0]+"/"+item[1]+"/"+item[2]+"/"+item[3]);


            //8. 평균 연봉보다 작게 받는 사원 조회

            Query query8 = sess.createQuery("select fname,sal,avg(sal) from Employees where sal <(select avg(sal) from Employees )" );

            List<Object[]> items8 =query8.getResultList();

            for (Object[] item : items8)
                System.out.println(item[0]+"/"+item[1]+"/"+item[2]);

            //9. 서브쿼리2 : IT부서에서 근무중인 사원들의 이름 ,직책 , 부서명 조회

            Query query9 =
                    sess.createQuery("select e.fname, e.jobid, d.dname from Employees e inner join Departments d on e.deptid = d.deptid where e.deptid = 60" );


            List<Object[]> items9 =query9.getResultList();     //query8.List(); 같은 기능이긴하나 getResultList는 전체가져옴
                                                              //하지만 보통 getResultList를 사용하기로 함


            System.out.println("9. IT부서에서 근무중인 사원들의 이름 ,직책 , 부서명 조회");
            for (Object[] item : items9)
                System.out.println(item[0]+"/"+item[1]+"/"+item[2]);











        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sess.close();
            sf.close();
        }

    }
}