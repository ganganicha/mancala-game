import { PieChartTwoTone, PinDropTwoTone } from "@material-ui/icons";
import React from "react";
import Grid from "@material-ui/core/Grid";


class Pit extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            simplifiedFunction: props.simplifiedFunction
        };
    }

    //lable click 
    // handleMancalaPitClick(e) {
    //     const { data } = this.state;
    //     console.log("******", this.props.id)
    
       
    //     };

    render() {

        return (
            <Grid>
                <span style={{ color: "blue", border: "2px solid black", height: "100px", width: "50%", padding: "50px" }}
                    id={this.props.id}  
                    onClick= { () =>
                        this.props.simplifiedFunction(this.props.id,this.props.playerId)
                      }
                    >{this.props.stones}  </span>
            </Grid>
        )
    }

}

export default Pit;
