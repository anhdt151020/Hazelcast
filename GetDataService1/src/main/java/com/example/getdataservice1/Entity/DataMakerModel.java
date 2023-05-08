package com.example.getdataservice1.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataMakerModel {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String Data;

    private String status;
}
