package com.bol.tec.interview.service;

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
//                if(gameBord.getPits().length >= requestedPitId) {
//                    Pit pit = gameBord.getPits()[requestedPitId];
//                }
                Optional<Pit> optionalPit = Arrays.stream(gameBord.getPits())
                        .filter(pit -> pit.getPitId() == requestedPitId).findFirst();

                if (optionalPit.isPresent() && optionalPit.get().getNoOfStones() > 0) {
                    Pit pit = optionalPit.get();
                    int nextPitId = pit.getPitId();
                    int noOfStones = pit.getNoOfStones();
                    gameBord.getPits()[requestedPitId].emptiedPit();
                    while (noOfStones > 0) {
                        nextPitId = (nextPitId + 1) % (gameBord.getPits().length); //get nextpitId
                        Pit rightPit = gameBord.getPits()[nextPitId];
                        if (rightPit.getPlayerId() != playerId && rightPit.getPitType() == PitType.BIG_PIT) {
                            continue;
                        }
                        gameBord.getPits()[nextPitId].addOneStone();
                        noOfStones--;

                    }
                    handleLastStoneWithEmptyPit(playerId, gameBord, nextPitId);
                    int nextPlayerId = getNextPlayerTurn(playerId, nextPitId, gameBord.getPits(), gameBord.getNoOfPlayers());
                    gameBord.setPlayerTurn(nextPlayerId);
                    if(isGameOver(gameBord.getPits(),playerId,gameBord.getNoOfPits())){
                        addPLayerRemainingStonesToBigPit(gameBord.getPits(),nextPlayerId);
                        gameBord.setWinner(getWinner(gameBord.getPits()));
                    }
                    mancalaGameBordRepository.save(gameBord);
                    return gameBord;
                }
            }
        }
        return gameBord;
    }

    private int getNextPlayerTurn(int playerId, int lastPid, Pit[] pits, int noOfPlayers) {

        Optional<Pit> playerBigPit = getPlayerBigPitByType(playerId, pits, PitType.BIG_PIT);
        if (playerBigPit.isPresent()) {
            if (playerBigPit.get().getPitId() == lastPid) {
                return playerId;
            }
        }
        return (playerId + 1) % noOfPlayers;
    }

    private Optional<Pit> getPlayerBigPitByType(int playerId, Pit[] pits, PitType pitType) {
        return Arrays.stream(pits)
                .filter(pit -> pit.getPitType().equals(pitType) && pit.getPlayerId() == playerId)
                .findFirst();
    }

    private int getOppositePit(int pitId, int noOfPits) {
       return 2 * noOfPits - pitId;
    }

    private void handleLastStoneWithEmptyPit(int playerId, MancalaGameBord mancalaGameBord, int lastPit) {
        Pit[] pits = mancalaGameBord.getPits();
        Pit pit = pits[lastPit];
        if (pit.getNoOfStones() == 1 && pit.getPitType().equals(PitType.SMALL_PIT) && pit.getPlayerId() == playerId) {
            int oppositePit = getOppositePit(pit.getPitId(), mancalaGameBord.getNoOfPits());
            int oppositePitNoStones = pits[oppositePit].getNoOfStones();
            pits[oppositePit].emptiedPit();
            pit.emptiedPit();
            Optional<Pit> optionalPit = getPlayerBigPitByType(playerId, pits, PitType.BIG_PIT);
            if (optionalPit.isPresent()) {
                int existingStoneCount = optionalPit.get().getNoOfStones();
                Arrays.stream(pits)
                        .filter(p -> p.getPitType().equals(PitType.BIG_PIT) &&
                                p.getPlayerId() == playerId).findFirst().get().setNoOfStones(existingStoneCount + oppositePitNoStones + 1);
            }
        }
    }

    public MancalaGameBord getGame(int i) {
        Optional<MancalaGameBord> mancalaGameBord = mancalaGameBordRepository.findById(i);
        return mancalaGameBord.get();
    }

    public boolean isGameOver(Pit[] pits, int playerId, int noOfPits){
        long emptyPits =  Arrays.stream(pits).filter(pit -> pit.getPlayerId()==playerId
                && pit.getPitType().equals(PitType.SMALL_PIT) && pit.isEmpty()).count();
        return emptyPits == noOfPits;
    }

    public void addPLayerRemainingStonesToBigPit(Pit[] pits, int playerId){
        int playerRemainingPitsStones = Arrays.stream(pits)
                .filter(pit -> pit.getPlayerId() == playerId
                && pit.getPitType().equals(PitType.SMALL_PIT)).mapToInt(Pit::getNoOfStones).sum();
        Optional<Pit> optionalPit = getPlayerBigPitByType(playerId, pits, PitType.BIG_PIT);
        optionalPit.ifPresent(pit -> getPlayerBigPitByType(playerId, pits, PitType.BIG_PIT)
                .get().addBulkStones(playerRemainingPitsStones));
        Arrays.stream(pits)
                .filter(pit -> pit.getPlayerId() == playerId
                        && pit.getPitType().equals(PitType.SMALL_PIT)).forEach(pit -> pit.emptiedPit());
    }

    public int getWinner(Pit[] pits){
        Optional<Pit> winnerPit =Arrays.stream(pits)
                .filter(pit -> pit.getPitType().equals(PitType.BIG_PIT))
                .collect(Collectors.toList()).stream().max(Comparator.comparing(Pit::getNoOfStones));
        return winnerPit.map(Pit::getPlayerId).orElse(-1);


    }
}
