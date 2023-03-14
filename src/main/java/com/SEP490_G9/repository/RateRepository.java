package com.SEP490_G9.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.Rate;
import com.SEP490_G9.entities.embeddable.RateItemKey;
;

public interface RateRepository extends JpaRepository<Rate, RateItemKey> {

}