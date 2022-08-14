package com.bol.tec.interview;

import com.bol.tec.interview.dto.GameCreateRequest;
import com.bol.tec.interview.model.MancalaGameBoard;
import com.bol.tec.interview.repository.MancalaGameBoardRepository;
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
    MancalaGameBoardRepository mancalaGameBoardRepository;

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
        when(mancalaGameBoardRepository.save(any())).thenReturn(new MancalaGameBoard(gameCreateRequest));
        MancalaGameBoard gameBord = mancalaService.createGame(gameCreateRequest);
        assertEquals(gameBord.getPlayerTurn(), 0);
        assertEquals(gameBord.getPits().length, 14);
    }


    @Test
    public void successGame1() throws IOException {

        MancalaGameBoard initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBoard.class);
        when(mancalaGameBoardRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBoard gameBord = mancalaService.playGame(1,0,4);
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

        MancalaGameBoard initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBoard.class);
        when(mancalaGameBoardRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBoard gameBord = mancalaService.playGame(1,1,4);
        assertEquals(gameBord.getPlayerTurn(), 0);
    }

    @Test
    public void skipOpponentBigPit() throws IOException {

        MancalaGameBoard initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBoard.class);
        initialGameBord.getPits()[12].setNoOfStones(8);
        initialGameBord.setPlayerTurn(1);
        when(mancalaGameBoardRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBoard gameBord = mancalaService.playGame(1,1,12);
        assertEquals(gameBord.getPlayerTurn(), 0);
        assertEquals(gameBord.getPits()[6].getNoOfStones(), 0);
        assertEquals(gameBord.getPits()[7].getNoOfStones(), 7);
        assertEquals(gameBord.getPits()[12].getNoOfStones(), 0);
    }

    @Test
    public void addingToOwnBigPit() throws IOException {

        MancalaGameBoard initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBoard.class);
        initialGameBord.getPits()[5].setNoOfStones(1);
        initialGameBord.setPlayerTurn(0);
        when(mancalaGameBoardRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBoard gameBord = mancalaService.playGame(1,0,5);
        assertEquals(gameBord.getPlayerTurn(), 0);
        assertEquals(gameBord.getPits()[6].getNoOfStones(), 1);
        assertEquals(gameBord.getPits()[5].getNoOfStones(), 0);

    }

    @Test
    public void addingToOwnEmptyPit() throws IOException {

        MancalaGameBoard initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBoard.class);
        initialGameBord.getPits()[4].setNoOfStones(1);
        initialGameBord.getPits()[5].setNoOfStones(0);
        initialGameBord.setPlayerTurn(0);
        when(mancalaGameBoardRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBoard gameBord = mancalaService.playGame(1,0,4);
        assertEquals(gameBord.getPlayerTurn(), 1);
        assertEquals(gameBord.getPits()[4].getNoOfStones(), 0);
        assertEquals(gameBord.getPits()[5].getNoOfStones(), 0);
        assertEquals(gameBord.getPits()[6].getNoOfStones(), 7);
        assertEquals(gameBord.getPits()[7].getNoOfStones(), 0);

    }

    @Test
    public void handleWinningmoment() throws IOException {

        MancalaGameBoard initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBoard.class);
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
        when(mancalaGameBoardRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBoard gameBord = mancalaService.playGame(1,0,5);
        assertEquals(gameBord.getPlayerTurn(), 1);
        assertEquals(gameBord.getPits()[13].getNoOfStones(), 37);
        assertEquals(gameBord.getWinner(), 1);


    }

    @Test
    public void handleAddingStoneToSelectedPit() throws IOException {

        MancalaGameBoard initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBoard.class);
        initialGameBord.getPits()[4].setNoOfStones(14);
        when(mancalaGameBoardRepository.findById(anyInt())).thenReturn(Optional.of(initialGameBord));
        MancalaGameBoard gameBord = mancalaService.playGame(1,0,4);
        assertEquals(gameBord.getPlayerTurn(), 1);
        assertEquals(gameBord.getPits()[4].getNoOfStones(), 1);
    }


    @Test
    public void handleSameAmountInBigPit() throws IOException {

        MancalaGameBoard initialGameBord = objectMapper.readValue(new File("src/test/resources/InitialGameBord.json"), MancalaGameBoard.class);
        initialGameBord.getPits()[6].setNoOfStones(14);
        initialGameBord.getPits()[13].setNoOfStones(14);
        int winner = mancalaService.getWinner(initialGameBord.getPits());
        assertEquals(winner, 0);
    }

}