import React from "react";
import Pit from "./Pit";
import Grid from "@material-ui/core/Grid";

class GameBoard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            simplifiedFunction: props.simplifiedFunction
        }
    }

    getPlaterOneMancalaPits(gameBord) {
        let mancalaPits = [];
        let platerOneMancalaPits = [];
        platerOneMancalaPits.push(gameBord.pits.filter(pit => {
            return pit.playerId === 0;
        }));
        for (let i = 5; i >= 0; i--) {
            mancalaPits.push(<Grid item >
                <Pit id={i} playerId={0} pitName={"Pit " + (i + 1)} simplifiedFunction={this.state.simplifiedFunction}
                    stones={platerOneMancalaPits[0] ? platerOneMancalaPits[0][i].noOfStones : 0} />
            </Grid>)
        }
        return mancalaPits;
    }

    getPlaterTwoMancalaPits(gameBord) {
        let mancalaPits = [];
        let platerTwoMancalaPits = [];
        platerTwoMancalaPits.push(gameBord.pits.filter(pit => {
            return pit.playerId === 1;
        }));
        console.log("platerTwoMancalaPits", platerTwoMancalaPits)
        for (let i = 0; i < 6; i++) {
            mancalaPits.push(<Grid item>
                <Pit id={platerTwoMancalaPits[0][i].pitId} playerId={1} pitName={"Pit " + (i + 1)} simplifiedFunction={this.state.simplifiedFunction}
                    stones={platerTwoMancalaPits[0] ? platerTwoMancalaPits[0][i].noOfStones : 0} />
            </Grid>)
        }
        return mancalaPits;
    }

    render() {
        const { gameBord } = this.props;
        return (
            <div>
                <lable id={gameBord.gameId}>Game No : {gameBord.gameId}</lable>
                <lable >Is is Player : {gameBord.playerTurn} Turn</lable>
                <div style={{ marginTop: "100px" }}>
                <lable >Player 1</lable>
                    <Grid container spacing={10}>
                        <Grid style={{ marginTop: '5%' }} item xs={2}>
                            <Pit
                                stones={gameBord.pits ? gameBord.pits[6].noOfStones : 0}
                                pitName="Player 1" />
                                <lable >Player 1</lable>
                        </Grid>
                        <Grid item xs={8}>
                            <Grid container spacing={5}>
                                {this.getPlaterOneMancalaPits(gameBord)}
                            </Grid>
                        </Grid>
                        <Grid item xs={8} style={{ paddingLeft: "370px" }}>
                        <lable >Player 2</lable>
                            <Grid container spacing={5}>
                                {this.getPlaterTwoMancalaPits(gameBord)}
                            </Grid>
                        </Grid>
                        <Grid style={{ marginTop: '-5%', marginleft: "-200px" }} item xs={2}>
                            <Pit
                                stones={gameBord.pits ? gameBord.pits[13].noOfStones : 0}
                                pitName="Player 2" />
                                 <lable >Player 2</lable>
                        </Grid>
                    </Grid>
                </div >
            </div>
        )
    }

}

export default GameBoard;