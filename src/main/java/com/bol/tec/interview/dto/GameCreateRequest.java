package com.bol.tec.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameCreateRequest {

    Integer noOfPlayers;
    Integer noOfPits;
    Integer noOfStones;
}
