import React from "react";
import axios from "axios";
import GameBoard from "./GameBoard";


class CreateGame extends React.Component {

    constructor(props) {
        super(props);
        this.handleMancalaPitClick = this.handleMancalaPitClick.bind(this);
        this.simplifiedFunction = this.simplifiedFunction.bind(this);
        this.state = {
            open: true,
            data: {
                "noOfPlayers": 2,
                "noOfPits": 6,
                "noOfStones": 6
            },
            gameBord: {}
        }

        const passData = (data) => {
            console.log("passedData", passData);
        };
    }




    handleMancalaPitClick(e) {
        const { data } = this.state;
        console.log("******", data)

        const url = 'mancalagame/api/create-game';
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
            this.setState({ gameBord: res });
        });

    }

    simplifiedFunction(pitId, playerId) {

        console.log("******", pitId, playerId)

        const url = 'mancalagame/api/play';
        console.log(url);
        let passedValue = {
            "pitId": pitId,
            "gameId": 1,
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
            this.setState({ gameBord: res });
            
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
        const { data, gameBord } = this.state;
        console.log(gameBord);

        return (
            <React.Fragment>
                <div>
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
                    }} />
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
                    }} />
                    <label for="noOfStones">Number Of Stones per Pit:</label>
                    <input type="text" id="noOfStones" name="noOfStones" value={data.noOfStones} onChange={(e) => {
                        let val = e.target.value;
                        this.setState({
                            data: {
                                noOfStones: val,
                                noOfPlayers: data.noOfPlayers,
                                noOfPits: data.noOfPits
                            }
                        });
                    }} />
                    <input type="Submit" onClick={this.handleMancalaPitClick} value="Submit" />
                </div>
                {gameBord && Object.keys(gameBord).length !== 0 && <GameBoard gameBord={gameBord} simplifiedFunction={this.simplifiedFunction} />}
            </React.Fragment>
        )


    }

}

export default CreateGame;