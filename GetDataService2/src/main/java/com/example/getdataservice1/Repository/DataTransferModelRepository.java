package com.example.getdataservice1.Repository;

import com.example.getdataservice1.Entity.DataTransferModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTransferModelRepository extends JpaRepository<DataTransferModel, Long> {
}
