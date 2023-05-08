package com.example.getdataservice1.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataTransferModel implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String Data;
    private String sessionId;
    private String authType;
    private String method;
    private String requestURI;
}
