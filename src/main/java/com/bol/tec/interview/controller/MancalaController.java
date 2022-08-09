package com.bol.tec.interview.controller;

import com.bol.tec.interview.service.MancalaService;
import com.bol.tec.interview.dto.GameCreateRequest;
import com.bol.tec.interview.dto.GamePlayRequest;
import com.bol.tec.interview.model.MancalaGameBord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "mancalagame/api")
public class MancalaController {

    @Autowired
    MancalaService mancalaService;

    @PostMapping(value = "create-game", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MancalaGameBord createGame(@Valid @RequestBody GameCreateRequest gameBoardRequest) {
        return mancalaService.createGame(gameBoardRequest);
    }

    @PostMapping(value = "play", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public MancalaGameBord playGame(@Valid @RequestBody GamePlayRequest gamePlayRequest) {
        return mancalaService.playGame(gamePlayRequest.getGameId(), gamePlayRequest.getPlayerId(),
                gamePlayRequest.getPitId());
    }

    @GetMapping(value = "get-game", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MancalaGameBord getGame() {
        return mancalaService.getGame(1);
    }
}
