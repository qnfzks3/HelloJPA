package model;

//empid, fname, lname, email, phone
//hdate, jobid, sal, comm, mgrid, deptid

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "employees")
@Setter
@Getter
@NoArgsConstructor   //기본 생성자 없을 시 사용  ,   이렇게 아니면 생성자를 만듬
public class Employees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")
    private Long empid;

    @Column(name = "FIRST_NAME")
    private String fname;

    @Column(name = "LAST_NAME")
    private String lname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUNBER")
    private String phone;

    @Column(name = "HIRE_DATE")
    private Date hdate;

    @Column(name = "JOB_ID")
    private String jobid;

    @Column(name = "SALARY")
    private Integer sal;

    @Column(name = "COMMISSION_PCT", precision=5, scale=2)
    private BigDecimal comm;

    @Column(name = "MANAGER_ID")
    private Integer mgrid;

    @Column(name = "DEPARTMENT_ID")
    private Long deptid;

   /*
   @ManyToOne   // 테이블 연관 관계 = 다 : 1
    @JoinColumn(name="department_id")
    // department 테이블의 id 컬럼과 조인
    private Departments department;
    */

    public Employees(String fname, Long deptid, Date hdate) {
        this.fname = fname;
        this.hdate = hdate;
        this.deptid = deptid;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Employees{");
        sb.append("empid=").append(empid);
        sb.append(", fname='").append(fname).append('\'');
        sb.append(", lname='").append(lname).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", hdate=").append(hdate);
        sb.append(", jobid='").append(jobid).append('\'');
        sb.append(", sal=").append(sal);
        sb.append(", comm=").append(comm);
        sb.append(", mgrid=").append(mgrid);
        sb.append(", deptid=").append(deptid);
        sb.append('}');
        return sb.toString();
    }
}