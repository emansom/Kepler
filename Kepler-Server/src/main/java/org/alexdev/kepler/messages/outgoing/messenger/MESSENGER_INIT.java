package org.alexdev.kepler.messages.outgoing.messenger;

import org.alexdev.kepler.game.messenger.Messenger;
import org.alexdev.kepler.game.messenger.MessengerUser;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;
import org.alexdev.kepler.util.config.GameConfiguration;

import java.util.List;

public class FRIENDSLIST extends MessageComposer {
    private final String consoleMotto;
    private final List<MessengerUser> friends;
    private final boolean isClubMember;

    public FRIENDSLIST(String consoleMotto, List<MessengerUser> friends, boolean isClubMember) {
        this.consoleMotto = consoleMotto;
        this.friends = friends;
        this.isClubMember = isClubMember;
    }

    @Override
    public void compose(NettyResponse response) {
        int normalFriendsLimit = GameConfiguration.getInstance().getInteger("messenger.max.friends.nonclub");
        int clubFriendsLimit = GameConfiguration.getInstance().getInteger("messenger.max.friends.club");

        response.writeString(this.consoleMotto);

        if (this.isClubMember) {
            response.writeInt(clubFriendsLimit);
        } else {
            response.writeInt(normalFriendsLimit);
        }

        response.writeInt(normalFriendsLimit);
        response.writeInt(clubFriendsLimit);
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
