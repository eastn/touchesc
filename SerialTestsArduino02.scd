SerialPort.listDevices;

(
p = SerialPort(
    "/dev/tty.usbmodem1421",    //edit to match your port.
    baudrate: 9600,    //check that baudrate is the same as in arduino sketch
    crtscts: true);
)

//read 10bit serial data sent from Arduino's Serial.println
(
r = Routine({
    var byte, str, res;
    99999.do{|i|
        if(p.read==10, {
            str = "";
            while({byte = p.read; byte != 13 }, {
                str= str++byte.asAscii;
            });
            res= str.asInteger;
            ("read value:"+res).postln;
        });
    };
}).play;
)

r.stop;
p.close;
///////////:
(
var window, inputDisplay, startStopButton;
var startFunc, stopFunc, poller, port;

startFunc = {
	poller = {
		"opening serial port".postln;
		port = SerialPort(
		"/dev/tty.usbmodem1421",
		baudrate: 9600,
		crtscts: true
		);
		1.wait;
		"starting polling loop".postln;
		loop {
			port.read.postln;
		}
	}.fork;
};

stopFunc = {
	poller.stop;
	"closing serial port".postln;
	port.close;
};

window = Window("serial input display").front;
window.view.layout = HLayout(
	startStopButton = Button()
	.states_([["start"], ["stop"]])
	.action_({ | me |
		[startFunc, stopFunc][1 - me.value].value
	}),
	inputDisplay = StaticText().string_("not initialized");
);
)



Server.default.boot;

{ SinOsc.ar(440, 0, 0.2) }.play;
{ SinOsc.ar(540, 0, 0.2) }.play;
{ SinOsc.ar(640, 0, 0.2) }.play;

(
{ var ampOsc;
    ampOsc = SinOsc.kr(0.5, 1.5pi, 0.5, 0.5);
    SinOsc.ar(440, 0, ampOsc);
}.play;
)