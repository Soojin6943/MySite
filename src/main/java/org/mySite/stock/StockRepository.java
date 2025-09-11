package org.mySite.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // todo 이게 뭐야?
    default Stock getById(Long id){
        return findById(id).orElseThrow(NoSuchElementException::new);
    }
}
