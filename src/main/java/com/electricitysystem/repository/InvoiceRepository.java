package com.electricitysystem.repository;

import com.electricitysystem.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
    List<InvoiceEntity> findAllByUsername(String username);
    List<InvoiceEntity> getAllByStatus(String status);

    @Query(value = "select * from invoice where id= :id", nativeQuery = true)
    InvoiceEntity getById(@Param("id") Integer id);
    InvoiceEntity findByToken(String token);

    InvoiceEntity findByUsernameAndStatus(String username, String status);
}
