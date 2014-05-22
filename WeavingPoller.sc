/*
Subclass of TouchePoller for input from the WeavingVoices loom.

Thu, May 22 2014, 02:15 CEST
*/

WeavingPoller : TouchePoller {
	
	*default {
		^NameSpace(\WeavingPoller, \default, { this.new });
	}	

	massageInputByte { | byte |
		^byte;
	}
}