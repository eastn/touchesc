/* 
Get data sent from Arduino via serial port.
Poll byte-by-byte.  Listen for byte values "0", "1", "2", sent as ASCII chars. 

Broadcast these through this.changed("/node", <nodename>, <state>).
Also broadcast to the network over UDP at: 255.255.255.0.

Other classes can listen to the broadcasts to do various things such as: 
- play sound
- compute behavior

For Pavilion project, at IAAC FabLab.
Done at Green Lab Valldaura.

Wed, May 21 2014, 19:37 CEST
*/
TouchePoller {
	classvar default;

	var window, inputDisplay, startStopButton;
	var poller, port, broadcastAddress;
	var serialPortName = "/dev/tty.usbmodem1411";
	var <>sensorName = "aSensor";

	var <>verbose = true;

	var <state;

	*default {
		default ?? {
			default = this.new;
		};
		^default;
	}

	*start { this.default.start; }

	*stop {	this.default.stop;	}

	*gui {
		this.default.gui;
	}

	gui {
		window = Window("serial input display", Rect(0, 0, 200, 400)).front;
		window.view.layout = VLayout(
			Button().states_([["Post input devices"]]).action_({
				SerialPort.listDevices;
			}),
			TextField().string_(serialPortName).action_({ | me |
				me.string.postln;
				serialPortName = me.string;
			}),
			Button().states_([["Load host IP list"]]).action_({
				{ | path | BroadcastOSC(path.load.postln); }.doPath;
			}),
			startStopButton = Button()
			.states_([["start"], ["stop"]])
			.action_({ | me |
				[{ this.start }, { this.stop }][1 - me.value].value
			}),
			inputDisplay = StaticText().string_("not running")
		);
	}

	start {
		var inputVal;
		//		broadcastAddress = NetAddr("255.255.255.0", 57120);
		poller = {
			"opening serial port".postln;
			port = SerialPort(
				"/dev/tty.usbmodem1411",
				baudrate: 9600,
				crtscts: true
			);
			1.wait;
			"starting polling loop".postln;
			loop {
				inputVal = port.read;
				{inputDisplay.string = inputVal.asAscii.asString}.defer;
				inputVal = inputVal - 48;
				if (state != inputVal) {
					state = inputVal;
					this.changed(\node, sensorName, state);
					// broadcastAddress.sendMsg("/node", sensorName, state);
					if (verbose) {
						[this, thisMethod.name, state].postln;
					};
				};
			}
		}.fork;
	}

	stop {
		poller.stop;
		"closing serial port".postln;
		port.close;
	}
}