package com.bol.tec.interview;

import com.bol.tec.interview.dto.GameCreateRequest;
import com.bol.tec.interview.model.MancalaGameBord;
import com.bol.tec.interview.repository.MancalaGameBordRepository;
import com.bol.tec.interview.service.MancalaService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class MancalaServiceTest {

    @InjectMocks
    MancalaService mancalaService;

    @Mock
    MancalaGameBordRepository mancalaGameBordRepository;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    @Test
    public void createGame(){
        GameCreateRequest gameCreateRequest = new GameCreateRequest();
        gameCreateRequest.setNoOfPits(6);
        gameCreateRequest.setNoOfPlayers(2);
        gameCreateRequest.setNoOfStones(6);
        when(mancalaGameBordRepository.save(any())).thenReturn(new MancalaGameBord(gameCreateRequest));
        MancalaGameBord gameBord = mancalaService.createGame(gameCreateRequest);
        assertEquals(gameBord.getPlayerTurn(), 0);
        assertEquals(gameBord.getPits().length, 14);
    }


    @Test
    public void successGame1() throws IOException {

        MancalaGameBord initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBord.class);
        when(mancalaGameBordRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBord gameBord = mancalaService.playGame(1,0,4);
        assertEquals(gameBord.getPlayerTurn(), 1);
        assertEquals(gameBord.getPits()[4].getNoOfStones(), 0);
        assertEquals(gameBord.getPits()[5].getNoOfStones(), 7);
        assertEquals(gameBord.getPits()[6].getNoOfStones(), 1);
        assertEquals(gameBord.getPits()[7].getNoOfStones(), 7);
        assertEquals(gameBord.getPits()[8].getNoOfStones(), 7);
        assertEquals(gameBord.getPits()[9].getNoOfStones(), 7);
        assertEquals(gameBord.getPits()[10].getNoOfStones(), 7);
    }

    @Test
    public void tryWithIncorrectPlayer() throws IOException {

        MancalaGameBord initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBord.class);
        when(mancalaGameBordRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBord gameBord = mancalaService.playGame(1,1,4);
        assertEquals(gameBord.getPlayerTurn(), 0);
    }

    @Test
    public void skipOpponentBigPit() throws IOException {

        MancalaGameBord initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBord.class);
        initialGameBord.getPits()[12].setNoOfStones(8);
        initialGameBord.setPlayerTurn(1);
        when(mancalaGameBordRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBord gameBord = mancalaService.playGame(1,1,12);
        assertEquals(gameBord.getPlayerTurn(), 0);
        assertEquals(gameBord.getPits()[6].getNoOfStones(), 0);
        assertEquals(gameBord.getPits()[7].getNoOfStones(), 7);
        assertEquals(gameBord.getPits()[12].getNoOfStones(), 0);
    }

    @Test
    public void addingToOwnBigPit() throws IOException {

        MancalaGameBord initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBord.class);
        initialGameBord.getPits()[5].setNoOfStones(1);
        initialGameBord.setPlayerTurn(0);
        when(mancalaGameBordRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBord gameBord = mancalaService.playGame(1,0,5);
        assertEquals(gameBord.getPlayerTurn(), 0);
        assertEquals(gameBord.getPits()[6].getNoOfStones(), 1);
        assertEquals(gameBord.getPits()[5].getNoOfStones(), 0);

    }

    @Test
    public void addingToOwnEmptyPit() throws IOException {

        MancalaGameBord initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBord.class);
        initialGameBord.getPits()[4].setNoOfStones(1);
        initialGameBord.getPits()[5].setNoOfStones(0);
        initialGameBord.setPlayerTurn(0);
        when(mancalaGameBordRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBord gameBord = mancalaService.playGame(1,0,4);
        assertEquals(gameBord.getPlayerTurn(), 1);
        assertEquals(gameBord.getPits()[4].getNoOfStones(), 0);
        assertEquals(gameBord.getPits()[5].getNoOfStones(), 0);
        assertEquals(gameBord.getPits()[6].getNoOfStones(), 7);
        assertEquals(gameBord.getPits()[7].getNoOfStones(), 0);

    }

    @Test
    public void handleWinningmoment() throws IOException {

        MancalaGameBord initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBord.class);
        initialGameBord.getPits()[0].setNoOfStones(0);
        initialGameBord.getPits()[0].setEmpty(true);
        initialGameBord.getPits()[1].setNoOfStones(0);
        initialGameBord.getPits()[1].setEmpty(true);
        initialGameBord.getPits()[2].setNoOfStones(0);
        initialGameBord.getPits()[2].setEmpty(true);
        initialGameBord.getPits()[3].setNoOfStones(0);
        initialGameBord.getPits()[3].setEmpty(true);
        initialGameBord.getPits()[4].setNoOfStones(0);
        initialGameBord.getPits()[4].setEmpty(true);
        initialGameBord.getPits()[5].setNoOfStones(2);
        initialGameBord.setPlayerTurn(0);
        when(mancalaGameBordRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBord gameBord = mancalaService.playGame(1,0,5);
        assertEquals(gameBord.getPlayerTurn(), 1);
        assertEquals(gameBord.getPits()[13].getNoOfStones(), 37);
        assertEquals(gameBord.getWinner(), 1);


    }

}