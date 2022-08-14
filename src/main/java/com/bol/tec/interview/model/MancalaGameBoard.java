package com.bol.tec.interview.model;

import com.bol.tec.interview.utils.PitType;
import com.bol.tec.interview.dto.*;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class MancalaGameBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int gameId;
    @Column
    int noOfStones;
    @Column
    int noOfPits;
    @Column
    int noOfPlayers;
    @Lob
    @Column
    Pit[] pits;
    @Column
    int playerTurn;
    @Column
    int winner;

    public MancalaGameBoard() {
    }

    public MancalaGameBoard(GameCreateRequest gameBoardRequest) {
        this.noOfPlayers = gameBoardRequest.getNoOfPlayers();
        this.noOfPits = gameBoardRequest.getNoOfPits();
        this.noOfStones = gameBoardRequest.getNoOfStones();
        int totalNoOfPits = noOfPits * noOfPlayers + noOfPlayers;
        int currentPlayer = 0;
        this.setPlayerTurn(currentPlayer);
        pits = new Pit[totalNoOfPits];
        int pitCount = 0;
        for (int i = 0; i < totalNoOfPits; i++) {
            Pit pit;
            if( pitCount == noOfPits ){
                pit = new Pit(i,0, PitType.BIG_PIT,false,currentPlayer);
                currentPlayer++;
                pitCount = 0;
            }else {
                pit = new Pit(i,this.noOfStones, PitType.SMALL_PIT, false, currentPlayer);
                pitCount++;
            }
            pits[i] = pit;
        }
        this.winner = -1;
    }

    public Pit getOppositePit(Pit pit) {
        return this.pits[this.noOfPlayers * this.noOfPits - pit.getPitId()];

    }

}
