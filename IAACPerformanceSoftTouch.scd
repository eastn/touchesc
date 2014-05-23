/*
For performance / demo with EASTN / Soft Touch Pavilion group at IAAC

Thu, May 22 2014, 19:59 CEST

*/
//:

OSCFunc.trace(true);

//:
OSCFunc.trace(false);

//:
Server.default.quit;

//:

Server.default.boot;
//:
~receiver = OSCFunc({ | msg |
	msg.postln;
	\sender.changed(msg[1].asSymbol.postln, msg[2]);
}, 'node');


//:
NetAddr("127.0.0.1", 57120).sendMsg('/node');

//:

\sound1.addNotifier(\sender, 'guillem', { | state |
	state.class.postln;
	(state + 123).postln;	

	(degree: [2, 4 + 5.rand, 8 + 10.rand][state]).play;
	//	(degree: state).play;
 });

//:
\sound1.addNotifier(\sender, 'nat', { | state |
	switch (state, 
		0, { \cicada.fadeOut(0.5) },
		1, { \cicada.start },
		2, { \cicada.set },
	)
 })

//:
\sound1.addNotifier(\sender, 'fabric', { | state |
	switch (state, 
		0, { \boombah.fadeOut(0.5) },
		1, { \boombah.start },
		2, { \boombah.set(\pfreq, 40.rrand(400)) },
	)
 })

//:
SynthTree.faders;
//:
\sound1.asSynthTree;
\sound2.asSynthTree;
\sound3.asSynthTree;
\sound4.asSynthTree;
\cicada.asSynthTree;
\cricket1.asSynthTree;
\cricket2.asSynthTree;
\gardellino.asSynthTree;
\boombah.asSynthTree;


//:

\sound1.addNotifier(\sender, 'nat', { | state |
	state.class.postln;
	(state + 123).postln;
	
	(degree: state * 3).play;

	switch (state,
		0, { \sound1.fadeOut(0.1) },
		1, { \sound1.start },
		2, { \sound1.start }
	);
	//	(degree: state).play;
 });

//:
\sound2.addNotifier(\sender, 'nat', { | state |
	state.class.postln;
	(state + 123).postln;
	
	(degree: state * 3).play;
	//	(degree: state).play;
 })