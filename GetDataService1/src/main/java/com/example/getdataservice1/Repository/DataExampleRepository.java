package com.example.getdataservice1.Repository;

import com.example.getdataservice1.Entity.DataExample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataExampleRepository extends JpaRepository<DataExample, Long> {
    List<DataExample> findAllByUsername(String username);
}
