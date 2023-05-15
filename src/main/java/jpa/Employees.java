package jpa;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;           /* util 에서 sql로 바꾸면 - 날짜만 출력된다.*/

@Entity                          //이건 엔티티다 mvc의 - vo와 같은 개념임
@Table(name = "employees")    //테이블명
@Data
public class Employees {
    //객체 매핑하는데 오류가 날 가능성을 줄이기 위해 클래스 형식의 Integer ,Long 등 을 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")  //칼럼명 지정
    private Long empid;

    @Column(name = "FALST_NAME")
    private String fname;

    @Column(name = "LAST_NAME")
    private String lname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phone;

    @Column(name = "HIRE_DATE")
    private Date hdate;

    @Column(name = "JOB_ID")
    private String jobid;

    @Column(name = "SALARY")
    private Integer sal;

    @Column(name = "COMMISSION_PCT")
    private BigDecimal comm;

    @Column(name = "MANAGER_ID")
    private Integer mgrid;

    @Column(name = "DEPARTMENT_ID")
    @JoinColumn(name="departments_id")
    private Long deptid;






}
