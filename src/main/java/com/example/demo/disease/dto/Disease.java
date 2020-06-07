package com.example.demo.disease.dto;

import com.example.demo.dog.dto.Dog;
import com.example.demo.overlap.BaseTimeEntity;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@ToString
@Entity
//public class Disease extends BaseTimeEntity {                 // 생성일자, 수정일자 BaseTimeEntity 클래스에서 상속받음
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Dog dog;

    private String name;            // 질병명
    private String type;            // 질병종류
    private String symptom;         // 질병증상

    @Builder
    public Disease(Long id, String name, String type, String symptom) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.symptom = symptom;
    }
}