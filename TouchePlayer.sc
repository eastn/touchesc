/*
Listen to "node" messages arriving from touche pollers in the network over OSC,
and perform different actions for each state coming in from each state of 
each poller. 

*/

TouchePlayer {
	
	var <>actions; // list of actions to access and perform per index;

	*default {
		^NameSpace(this, \default, {
			
		})
	}

	*enable {
		this.default.enable;
	}

	
	
}