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

	var window, inputDisplay, startStopButton;
	var poller, port, broadcastAddress;
	var serialPortName = "/dev/tty.usbmodem1411";
	var serialPortNameField;
	var <>sensorName = "aSensor";

	var <>verbose = true;

	var <state;

	*initClass { StartUp add: { this.gui } }

	*default {
		^NameSpace(\TouchePoller, \default, { this.new });
	}

	*start { this.default.start; }

	*stop {	this.default.stop;	}

	*gui {
		this.default.gui;
	}

	gui {
		window = Window("serial input display", Rect(0, 0, 200, 200)).front;
		window.view.layout = VLayout(
			Button().states_([["Find Serial Input Device"]]).action_({
				SerialPort.listDevices;
				this.guessPort;
			}),
			serialPortNameField = TextField()
			.string_(serialPortName)
			.action_({ | me |
				me.string.postln;
				serialPortName = me.string;
				this.openPort;
			}),
			Button().states_([["Load host IP list"]]).action_({
				{ | path | BroadcastOSC(path.load.postln); }.doPath;
			}),
			startStopButton = Button()
			.states_([["start"], ["stop"]])
			.action_({ | me |
				[{ this.start }, { this.stop }][1 - me.value].value
			}),
			inputDisplay = StaticText().string_("not running"),
			Button().states_([["NO TOUCH"]]).action_({
				port.put(0);
			}),
			Button().states_([["TOUCH"]]).action_({
				port.put(1);
			}),
			Button().states_([["GRAB"]]).action_({
				port.put(2);
			}),
			Button().states_([["SAVE"]]).action_({
				port.put(255);
			})
		);
	}

	openPort {
		format("opening serial port: %", serialPortName).postln;
			port = SerialPort(
				serialPortName,
				baudrate: 9600,
				crtscts: true
			)
	}

	guessPort {
		var devices, thePort;
		devices = SerialPort.devices;
		thePort = devices detect: { | d |
			"/dev/tty.usb*".matchRegexp(d);
		};
		[thePort, thisMethod.name, thePort].postln;
		if (thePort.isNil) {
			"NO SERIAL PORT MATCHING /dev/tty.usb* FOUND!".postln;
			"Please connect an Arduino to the USB port.".postln;
		}{
			{ serialPortNameField.string = thePort }.defer;
			serialPortName = thePort;
			this.openPort;
		}
	}

	start {
		var inputVal;
		//		broadcastAddress = NetAddr("255.255.255.0", 57120);
		poller = {
			"starting polling loop".postln;
			loop {
				inputVal = port.read;
				{inputDisplay.string = inputVal.asString}.defer;
				inputVal = this.massageInputByte(inputVal);
				if (state != inputVal) {
					state = inputVal;
					this.changed(\node, sensorName, state);
					if (verbose) {
						[this, thisMethod.name, state].postln;
					};
				};
			}
		}.fork;
	}

	massageInputByte { | byte |
		//		^byte - 48;
		^byte;
	}

	stop {
		poller.stop;
		"closing serial port".postln;
		port.close;
	}
}