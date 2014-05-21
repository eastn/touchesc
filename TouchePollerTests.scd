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
			if (~sound1.notNil) {
				{
			};
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

//

{ WhiteNoise.ar(0.1) } => \alex;

\alex.asSynthTree;