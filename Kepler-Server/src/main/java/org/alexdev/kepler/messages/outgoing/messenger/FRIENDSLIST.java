package org.alexdev.kepler.messages.outgoing.messenger;

import org.alexdev.kepler.game.messenger.Messenger;
import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class FRIENDSLIST extends MessageComposer {
    private final String consoleMotto;
    private final List<MessengerUser> friends;

    public FRIENDSLIST(String consoleMotto, List<MessengerUser> friends) {
        this.consoleMotto = consoleMotto;
        this.friends = friends;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeString(this.consoleMotto);
        response.writeInt(600);
        response.writeInt(200);
        response.writeInt(600);
        response.writeInt(this.friends.size());

        for (MessengerUser friend : this.friends) {
            friend.serialise(response);
        }
    }

    @Override
    public short getHeader() {
        return 12; // "@L"
    }
}
