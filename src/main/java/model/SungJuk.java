package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="sungjuk")
@Data
public class SungJuk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sjno;

    private String name;

    private int kor;

    private int eng;

    private int mat;

    private int tot;

    private double avg;

    private String grd;

    private Date regdate;

    // persist 호출전에 regdate 컬럼에 현재 날짜/시간 대입
    @PrePersist
    protected void onCreate() {
        regdate = new Date();
    }

}