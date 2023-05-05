package com.example.getdataservice1.Controller;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PojoExample {
    private String key;
    private String value;
    private String username;
    private String SessionId;
}
