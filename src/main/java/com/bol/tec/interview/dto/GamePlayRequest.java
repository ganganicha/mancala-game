package com.bol.tec.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public
class GamePlayRequest {

    @NotNull
    Integer pitId;

    @NotNull
    Integer gameId;

    @NotNull
    Integer playerId;
}
