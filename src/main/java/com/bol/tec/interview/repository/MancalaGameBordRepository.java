package com.bol.tec.interview.repository;


import com.bol.tec.interview.model.MancalaGameBord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MancalaGameBordRepository extends CrudRepository<MancalaGameBord, Integer> {


}
