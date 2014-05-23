/*
Listen to "node" messages arriving from touche pollers in the network over OSC,
and perform different actions for each state coming in from each state of 
each poller. 

NOT COMPLETE

*/

TouchePlayer {
	classvar <defaultMessage = 'node';
	var <>actions; // list of actions to access and perform per index;
	var <message = 'node';
	var <oscFunc;

	*default {
		^NameSpace(this, defaultMessage, {
			this.new;
		});
	}

	*new { | actions, name = 'node' |
		^this.newCopyArgs(actions, name).initTouchePlayer;
	}

	initTouchePlayer {
		oscFunc = OSCFunc({ | name, state |
			this.changed(name.asSymbol, state);
		});
	}

	*enable {
		this.default.enable;
	}

	enable { "I am enabled per default".postln; }

}