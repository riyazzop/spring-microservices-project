package com.employee.model.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String empName;

    @Column(unique = true)
    private String empEmail;

    private String empCode;
    private String companyName;

}
