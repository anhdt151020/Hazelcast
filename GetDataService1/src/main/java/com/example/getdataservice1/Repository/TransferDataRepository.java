package com.example.getdataservice1.Repository;

import com.example.getdataservice1.Entity.TransferModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferDataRepository extends JpaRepository<TransferModel, String> {
    TransferModel getByTransKey(String key);
}
