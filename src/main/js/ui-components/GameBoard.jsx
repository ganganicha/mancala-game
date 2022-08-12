import React from "react";
import Pit from "./Pit";
import Grid from "@material-ui/core/Grid";
import {Box, Badge} from "@material-ui/core";

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
            mancalaPits.push(<Grid item>
                <Pit id={i} playerId={0} pitName={"Pit " + (i + 1)} simplifiedFunction={this.state.simplifiedFunction}
                     stones={platerOneMancalaPits[0] ? platerOneMancalaPits[0][i].noOfStones : 0}/>
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
                <Pit id={platerTwoMancalaPits[0][i].pitId} playerId={1} pitName={"Pit " + (i + 1)}
                     simplifiedFunction={this.state.simplifiedFunction}
                     stones={platerTwoMancalaPits[0] ? platerTwoMancalaPits[0][i].noOfStones : 0}/>
            </Grid>)
        }
        return mancalaPits;
    }

    render() {
        const {gameBord} = this.props;
        return (
            <Box sx={{flexGrow: 1}}>
                <Grid container spacing={2}>
                    <Grid item xs={2}></Grid>
                    <Grid container item xs={8} spacing={2}>
                        <Grid item xs={12} style={{display: "flex", justifyContent: "space-around"}}>
                            <lable id={gameBord.gameId}>Game No : {gameBord.gameId}</lable>
                            <lable> Turn : Player {gameBord.playerTurn + 1}</lable>
                        </Grid>
                        <Grid item xs={12} style={{display: "flex", justifyContent: "space-around"}}>
                            <label>Player 1</label>
                        </Grid>
                        <Grid item xs={12} spacing={2}>
                            <Grid container spacing={1}>
                                <Badge style={{paddingRight: "10px"}} color="primary" badgeContent={"P1"} anchorOrigin={{
                                    vertical: 'top',
                                    horizontal: 'left',
                                }}>
                                    <Grid item xs={2}>
                                        <Pit stones={gameBord.pits ? gameBord.pits[6].noOfStones : 0}
                                             pitName="Player 1"/>
                                    </Grid>
                                </Badge>
                                <Grid container item xs={8} spacing={2}>
                                    <Grid item xs={12} style={{display: "flex", justifyContent: "space-around"}}>
                                        {this.getPlaterOneMancalaPits(gameBord)}
                                    </Grid>
                                    <Grid item xs={12} style={{display: "flex", justifyContent: "space-around"}}>
                                        {this.getPlaterTwoMancalaPits(gameBord)}
                                    </Grid>
                                </Grid>
                                <Badge style={{paddingLeft: "10px"}} color="secondary" badgeContent={"P2"} anchorOrigin={{
                                    vertical: 'bottom',
                                    horizontal: 'right',
                                }}>
                                <Grid item xs={2}>
                                    <Pit stones={gameBord.pits ? gameBord.pits[13].noOfStones : 0}
                                         pitName="Player 2"/>
                                </Grid>
                            </Badge>
                        </Grid>
                    </Grid>
                    <Grid item xs={12} style={{display: "flex", justifyContent: "space-around"}}>
                        <label>Player 2</label>
                    </Grid>
                </Grid>
                <Grid item xs={2}></Grid>
            </Grid>
    </Box>
    )
    }

    renderEx() {
        const {gameBord} = this.props;
        return (
            <div>
                <lable id={gameBord.gameId}>Game No : {gameBord.gameId}</lable>
                <br></br>
                <lable>Is is Player : {gameBord.playerTurn} Turn</lable>
                <div style={{marginTop: "100px", flexGrow: "1"}}>
                    <lable>Player 1</lable>
                    <Grid container spacing={10}>
                        <Grid style={{marginTop: '5%'}} item xs={2}>
                            <Pit
                                stones={gameBord.pits ? gameBord.pits[6].noOfStones : 0}
                                pitName="Player 1"/>
                            <lable>Player 1</lable>
                        </Grid>
                        <Grid item xs={8}>
                            <Grid container spacing={5}>
                                {this.getPlaterOneMancalaPits(gameBord)}
                            </Grid>
                        </Grid>
                        <Grid item xs={8} style={{paddingLeft: "370px"}}>
                            <lable>Player 2</lable>
                            <Grid container spacing={5}>
                                {this.getPlaterTwoMancalaPits(gameBord)}
                            </Grid>
                        </Grid>
                        <Grid style={{marginTop: '-5%', marginleft: "-200px"}} item xs={2}>
                            <Pit
                                stones={gameBord.pits ? gameBord.pits[13].noOfStones : 0}
                                pitName="Player 2"/>
                            <lable>Player 2</lable>
                        </Grid>
                    </Grid>
                </div>
            </div>
        )
    }

}

export default GameBoard;