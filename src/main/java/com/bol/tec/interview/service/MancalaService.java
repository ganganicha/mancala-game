package com.bol.tec.interview.service;

import com.bol.tec.interview.exception.GameNotFoundException;
import com.bol.tec.interview.exception.PitNotFoundException;
import com.bol.tec.interview.model.Pit;
import com.bol.tec.interview.dto.GameCreateRequest;
import com.bol.tec.interview.repository.MancalaGameBordRepository;
import com.bol.tec.interview.utils.PitType;
import com.bol.tec.interview.model.MancalaGameBord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MancalaService {


    @Autowired
    MancalaGameBordRepository mancalaGameBordRepository;

    public MancalaGameBord createGame(GameCreateRequest gameBoardRequest) {
        MancalaGameBord mancalaGameBord = new MancalaGameBord(gameBoardRequest);
        return mancalaGameBordRepository.save(mancalaGameBord);
    }

    public MancalaGameBord playGame(Integer gameId, Integer playerId, Integer requestedPitId) {

        MancalaGameBord gameBord = null;
        Optional<MancalaGameBord> mancalaGameBord = mancalaGameBordRepository.findById(gameId);
        if (mancalaGameBord.isPresent()) {
            gameBord = mancalaGameBord.get();
            //check is the correct player turn
            if (gameBord.getPlayerTurn() == playerId) {
                // get the correct pit from the array based on the player
                Pit selectedPit = null;
                if (gameBord.getPits().length >= requestedPitId) {
                    selectedPit = gameBord.getPits()[requestedPitId];
                }
//                Optional<Pit> optionalPit = Arrays.stream(gameBord.getPits())
//                        .filter(pit -> pit.getPitId() == requestedPitId).findFirst();

                if (selectedPit != null && selectedPit.isPlayable()) {
                    int nextPitId = selectedPit.getPitId();
                    int noOfStones = selectedPit.getNoOfStones();
                    Pit nextPit = null;
                    selectedPit.empty();
                    while (noOfStones > 0) {
                        nextPitId = (nextPitId + 1) % (gameBord.getPits().length); //get nextpitId
                        nextPit = gameBord.getPits()[nextPitId];
                        if (nextPit.getPlayerId() != playerId && nextPit.getPitType() == PitType.BIG_PIT) {
                            continue;
                        }
                        gameBord.getPits()[nextPitId].addStones(1);
                        noOfStones--;

                    }
                    handleLastStoneWithEmptyPit(playerId, gameBord, nextPit);
                    int nextPlayerId = getNextPlayerTurn(playerId, nextPitId, gameBord.getPits(), gameBord.getNoOfPlayers());
                    gameBord.setPlayerTurn(nextPlayerId);
                    if (isGameOver(gameBord.getPits(), playerId, gameBord.getNoOfPits())) {
                        addPLayerRemainingStonesToBigPit(gameBord.getPits(), nextPlayerId);
                        gameBord.setWinner(getWinner(gameBord.getPits()));
                    }
                    mancalaGameBordRepository.save(gameBord);
                    return gameBord;
                }
            }
        }else {
            throw new GameNotFoundException();
        }
        return gameBord;
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

    private int getOppositePit(int pitId, int noOfPits) {
        return 2 * noOfPits - pitId;
    }

    private void handleLastStoneWithEmptyPit(int playerId, MancalaGameBord mancalaGameBord, Pit pit) {
        Pit[] pits = mancalaGameBord.getPits();
        if (pit != null && pit.getNoOfStones() == 1 && pit.getPitType().equals(PitType.SMALL_PIT) && pit.getPlayerId() == playerId) {
            Pit oppositePit = mancalaGameBord.getOppositePit(pit);
            getPlayerPitByType(playerId, pits, PitType.BIG_PIT).addStones(oppositePit.getNoOfStones() + pit.getNoOfStones());
            oppositePit.empty();
            pit.empty();

        }
    }

    public boolean isGameOver(Pit[] pits, int playerId, int noOfPits) {
        long emptyPits = Arrays.stream(pits).filter(pit -> pit.getPlayerId() == playerId
                && pit.getPitType().equals(PitType.SMALL_PIT) && pit.isEmpty()).count();
        return emptyPits == noOfPits;
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
