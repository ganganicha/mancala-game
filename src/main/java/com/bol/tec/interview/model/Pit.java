package com.bol.tec.interview.model;

import com.bol.tec.interview.utils.PitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Pit implements Serializable {

    @Id
    int pitId;
    @Column
    int noOfStones;
    @Column
    PitType pitType;
    @Column
    boolean isEmpty;
    @Column
    int PlayerId;

    public Pit() {
    }

    public Pit(int pitId, int noOfStones, PitType pitType, boolean isEmpty, int playerId) {
        this.pitId = pitId;
        this.noOfStones = noOfStones;
        this.pitType = pitType;
        this.isEmpty = isEmpty;
        PlayerId = playerId;
    }

    public int addOneStone() {
        this.noOfStones = this.noOfStones +1 ;
        this.setEmpty(false);
        return noOfStones;
    }

    public int emptiedPit() {
        this.noOfStones = 0 ;
        this.setEmpty(true);
        return noOfStones;
    }

    public int addBulkStones(int noOfStones) {
        this.noOfStones = this.getNoOfStones() + noOfStones ;
        this.setEmpty(false);
        return noOfStones;
    }
}