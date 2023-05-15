package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="sungjuk")
@Data
public class SungJuk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sjno;

    @Column(length = 10, nullable = false)
    private String name;

    private int kor;

    private int eng;

    private int mat;

    @Column(nullable = true)
    private int tot;

    @Column(nullable = true, precision = 5, scale = 1)
    private BigDecimal avg;

    @Column(length = 1, nullable = true)
    private String grd;

    private Date regdate;

    // persist 호출전에 regdate 컬럼에 현재 날짜/시간 대입
    @PrePersist
    protected void onCreate() {
        regdate = new Date();
    }

}