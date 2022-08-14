package com.bol.tec.interview.repository;


import com.bol.tec.interview.model.MancalaGameBoard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MancalaGameBoardRepository extends CrudRepository<MancalaGameBoard, Integer> {


}
