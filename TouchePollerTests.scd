TouchePoller.gui;

(
\notalex.addNotifier(TouchePoller.default, \state, { | state |
	switch (state.postln,
		0, {
			(degree: 8).play;
		},
		1, {
			(degree: 1 rrand: 5).play;
		},
		2, {
			(degree: 6 rrand: 6).play;
		}
	);
});
)

(
~sound1 = \alex.asSynthTree;
\alex.addNotifier(TouchePoller.default, \state, { | state |
	switch (state.postln,
		0, {
			\alex.fadeOut(3);
		},
		1, {
			{ WhiteNoise.ar(0.1) } => \alex; 
		},
		2, {
			{ GrayNoise.ar(0.1) } => \alex; 
		}
	);
});
)

//

{ WhiteNoise.ar(0.1) } => \alex;

\alex.asSynthTree;