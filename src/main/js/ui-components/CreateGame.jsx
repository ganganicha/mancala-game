import React from "react";
import axios from "axios";
import GameBoard from "./GameBoard";
import {Alert} from "@material-ui/lab";
import {Link} from "@material-ui/core";


class CreateGame extends React.Component {

    constructor(props) {
        super(props);
        this.handleMancalaPitClick = this.handleMancalaPitClick.bind(this);
        this.clickPit = this.clickPit.bind(this);
        this.state = {
            open: true,
            data: {
                "noOfPlayers": 2,
                "noOfPits": 6,
                "noOfStones": 6
            },
            gameBord: {},
            gameConcluded: false
        }

        const passData = (data) => {
            console.log("passedData", passData);
        };
    }


    handleMancalaPitClick(e) {
        const {data} = this.state;
        console.log("******", data)

        const url = 'mancalagame/create-game';
        console.log(url);
        let passedValue = {
            "noOfPlayers": data.noOfPlayers,
            "noOfPits": data.noOfPits,
            "noOfStones": data.noOfStones
        };
        const config = {
            headers: {
                'content-type': 'application/json'
            }
        }
        axios.post(url, passedValue, config).then(response => {
            let res = response.data;
            console.log("response : ", res);
            this.setState({gameBord: res});
        });

    }

    clickPit(pitId, playerId) {

        console.log("******", pitId, playerId)

        const url = 'mancalagame/play';
        console.log(url);
        let passedValue = {
            "pitId": pitId,
            "gameId": this.state.gameBord.gameId,
            "playerId": playerId
        };
        const config = {
            headers: {
                'content-type': 'application/json'
            }
        }
        axios.post(url, passedValue, config).then(response => {
            let res = response.data;
            console.log("response : ", res);
            this.setState({
                gameBord: res,
                gameConcluded: res.winner !== -1
            });

        });


    }


    handleMancalaPitClick2() {
        // const { data } = this.state;
        console.log("******llll", this.setState);

        // const url = 'mancalagame/api/create-game';
        // console.log(url);
        // let passedValue = {
        //     "noOfPlayers": data.noOfPlayers,
        //     "noOfPits": data.noOfPits,
        //     "noOfStones": data.noOfStones
        // };
        // const config = {
        //     headers: {
        //         'content-type': 'application/json'
        //     }
        // }
        // axios.post(url, passedValue, config).then(response => {
        //     let res = response.data;
        //     console.log("response : ", res);
        //     this.setState({ gameBord: res });
        // });

    }

    // getMancalaPits(data) {

    // }


    render() {
        const {data, gameBord} = this.state;
        console.log(gameBord);

        return (
            <React.Fragment>
                {!this.state.gameConcluded && Object.keys(gameBord).length === 0 && <div>
                    <label for="noOfPlayers">Number Of Players:</label>
                    <input type="text" id="noOfPlayers" name="noOfPlayers" value={data.noOfPlayers} onChange={(e) => {
                        let val = e.target.value;
                        this.setState({
                            data: {
                                noOfPlayers: val,
                                noOfPits: data.noOfPits,
                                noOfStones: data.noOfStones
                            }
                        });
                    }}/>
                    <label for="noOfPits">Number Of Pits:</label>
                    <input type="text" id="noOfPits" name="noOfPits" value={data.noOfPits} onChange={(e) => {
                        let val = e.target.value;
                        this.setState({
                            data: {
                                noOfStones: data.noOfStones,
                                noOfPits: val,
                                noOfPlayers: data.noOfPlayers
                            }
                        });
                    }}/>
                    <label for="noOfStones">Stones per Pit:</label>
                    <input type="text" id="noOfStones" name="noOfStones" value={data.noOfStones} onChange={(e) => {
                        let val = e.target.value;
                        this.setState({
                            data: {
                                noOfStones: val,
                                noOfPlayers: data.noOfPlayers,
                                noOfPits: data.noOfPits
                            }
                        });
                    }}/>
                    <input type="Submit" onClick={this.handleMancalaPitClick} value="Submit"/>
                </div>}
                {this.state.gameConcluded &&
                    <Alert severity="success">Player {gameBord.winner + 1} Won!
                        <Link
                            component="button"
                            variant="body2"
                            onClick={() => {
                                this.setState({
                                    open: true,
                                    data: {
                                        "noOfPlayers": 2,
                                        "noOfPits": 6,
                                        "noOfStones": 6
                                    },
                                    gameBord: {},
                                    gameConcluded: false
                                })
                            }}
                        >    Click to Start New Game ... </Link>
                    </Alert>
                }
                {gameBord && Object.keys(gameBord).length !== 0 &&
                    <GameBoard disabled={this.state.gameConcluded} gameBord={gameBord}
                               clickPit={this.clickPit}/>}
            </React.Fragment>
        )


    }

}

export default CreateGame;