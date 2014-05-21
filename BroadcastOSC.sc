/*

Broadcast osc messages to a list of host IPs and ports
Helper class for broadcasting data received by TouchePoller. 
Uses Notification mechanism to listen to broadcast of "changed" data, 
and then sends the entire data received via OSC to the host IPs.

Can also be used for other purposes.

*/

BroadcastOSC {
	var <>hosts; // Host IP/Port NetAddress instances to send to
	var <message; // message to listen to, and to broadcast
	var <notifier; // sender from which the message is received;

	*new { | hosts, message = \node, notifier |
		notifier ?? { notifier = TouchePoller.default };
		^NameSpace(notifier, message, {
			this.newCopyArgs(hosts, message, notifier).add;
		}).hosts_(hosts);
	}

	*remove { | notifier, message |
		NameSpace.doIfFound(notifier, message, 
			{ | broadcaster | broadcaster.remove }
		);
	}

	add {
		//	add this broadcaster to the notifier
		this.addNotifier(notifier, message, { | ... data |
			this.broadcast(data);
		});
	}

	remove {
		// Remove this broadcaster from the notifier
		// A new one may take its place. 
		this.removeNotifier(notifier, message);
		NameSpace.remove(notifier, message);
	}

	broadcast { | data |
		// only send data up to the Notification instance 
		[this, thisMethod.name, data].postln;
		data = 	data[.. data.indexOf(data detect: _.isKindOf(Notification)) - 1];
		hosts do: { | netAddr |
			[this, thisMethod.name, netAddr, message, data].postln;
			netAddr.sendMsg(message, *data); };
	}
}