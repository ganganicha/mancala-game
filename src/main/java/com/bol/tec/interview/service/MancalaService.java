package com.bol.tec.interview.service;

import com.bol.tec.interview.exception.GameNotFoundException;
import com.bol.tec.interview.exception.PitNotFoundException;
import com.bol.tec.interview.model.Pit;
import com.bol.tec.interview.dto.GameCreateRequest;
import com.bol.tec.interview.repository.MancalaGameBoardRepository;
import com.bol.tec.interview.utils.PitType;
import com.bol.tec.interview.model.MancalaGameBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MancalaService {


    @Autowired
    MancalaGameBoardRepository mancalagameBoardRepository;

    public MancalaGameBoard createGame(GameCreateRequest gameBoardRequest) {
        MancalaGameBoard mancalaGameBoard = new MancalaGameBoard(gameBoardRequest);
        return mancalagameBoardRepository.save(mancalaGameBoard);
    }

    public MancalaGameBoard playGame(Integer gameId, Integer playerId, Integer requestedPitId) {

        MancalaGameBoard gameBoard = null;
        Optional<MancalaGameBoard> mancalagameBoard = mancalagameBoardRepository.findById(gameId);
        if (!mancalagameBoard.isPresent()) {
            throw new GameNotFoundException();
        }
        gameBoard = mancalagameBoard.get();
        //check is the correct player turn
        if (gameBoard.getPlayerTurn() == playerId) {
            // get the correct pit from the array based on the player
            Pit selectedPit = null;
            if (gameBoard.getPits().length >= requestedPitId) {
                selectedPit = gameBoard.getPits()[requestedPitId];
            }
//                Optional<Pit> optionalPit = Arrays.stream(gameBoard.getPits())
//                        .filter(pit -> pit.getPitId() == requestedPitId).findFirst();

            if (selectedPit != null && selectedPit.isPlayable()) {
                int nextPitId = selectedPit.getPitId();
                int noOfStones = selectedPit.getNoOfStones();
                Pit nextPit = null;
                selectedPit.empty();
                while (noOfStones > 0) {
                    nextPitId = (nextPitId + 1) % (gameBoard.getPits().length); //get nextpitId
                    nextPit = gameBoard.getPits()[nextPitId];
                    if (nextPit.getPlayerId() != playerId && nextPit.getPitType() == PitType.BIG_PIT) {
                        continue;
                    }
                    gameBoard.getPits()[nextPitId].addStones(1);
                    noOfStones--;

                }
                handleLastStoneWithEmptyPit(playerId, gameBoard, nextPit);
                int nextPlayerId = getNextPlayerTurn(playerId, nextPitId, gameBoard.getPits(), gameBoard.getNoOfPlayers());
                gameBoard.setPlayerTurn(nextPlayerId);
                if (isGameOver(gameBoard.getPits(), playerId, gameBoard.getNoOfPits())) {
                    addPLayerRemainingStonesToBigPit(gameBoard.getPits(), nextPlayerId);
                    gameBoard.setWinner(getWinner(gameBoard.getPits()));
                }
                mancalagameBoardRepository.save(gameBoard);
                return gameBoard;
            }
        }

        return gameBoard;
    }

    private int getNextPlayerTurn(int playerId, int lastPid, Pit[] pits, int noOfPlayers) {

        Pit playerBigPit = getPlayerPitByType(playerId, pits, PitType.BIG_PIT);
        if (playerBigPit.getPitId() == lastPid) {
            return playerId;
        }
        return (playerId + 1) % noOfPlayers;
    }

    private Pit getPlayerPitByType(int playerId, Pit[] pits, PitType pitType) {
        return Arrays.stream(pits)
                .filter(pit -> pit.getPitType().equals(pitType) && pit.getPlayerId() == playerId)
                .findFirst().orElseThrow(PitNotFoundException::new);
    }

    private void handleLastStoneWithEmptyPit(int playerId, MancalaGameBoard mancalaGameBoard, Pit pit) {
        Pit[] pits = mancalaGameBoard.getPits();
        if (pit != null && pit.getNoOfStones() == 1 && pit.getPitType().equals(PitType.SMALL_PIT) && pit.getPlayerId() == playerId) {
            Pit oppositePit = mancalaGameBoard.getOppositePit(pit);
            getPlayerPitByType(playerId, pits, PitType.BIG_PIT).addStones(oppositePit.getNoOfStones() + pit.getNoOfStones());
            oppositePit.empty();
            pit.empty();

        }
    }

    public boolean isGameOver(Pit[] pits, int playerId, int noOfPits) {
        return Arrays.stream(pits).filter(pit -> pit.getPlayerId() == playerId
                && pit.getPitType().equals(PitType.SMALL_PIT) && pit.isEmpty()).count() == noOfPits;
    }

    public void addPLayerRemainingStonesToBigPit(Pit[] pits, int playerId) {
        int playerRemainingPitsStones = Arrays.stream(pits)
                .filter(pit -> pit.getPlayerId() == playerId
                        && pit.getPitType().equals(PitType.SMALL_PIT)).mapToInt(Pit::getNoOfStones).sum();
        getPlayerPitByType(playerId, pits, PitType.BIG_PIT).addStones(playerRemainingPitsStones);
        Arrays.stream(pits)
                .filter(pit -> pit.getPlayerId() == playerId && pit.getPitType().equals(PitType.SMALL_PIT)).forEach(Pit::empty);
    }

    public int getWinner(Pit[] pits) {
        Optional<Pit> winnerPit = Arrays.stream(pits)
                .filter(pit -> pit.getPitType().equals(PitType.BIG_PIT))
                .collect(Collectors.toList()).stream().max(Comparator.comparing(Pit::getNoOfStones));
        return winnerPit.map(Pit::getPlayerId).orElse(-1);


    }
}
