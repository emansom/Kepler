package org.alexdev.kepler.messages.outgoing.songs;

import org.alexdev.kepler.messages.incoming.navigator.RECOMMENDED_ROOMS;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class HAND_SOUNDSETS extends MessageComposer {
    private final List<Integer> handSoundsets;

    public HAND_SOUNDSETS(List<Integer> handSoundsets) {
        this.handSoundsets = handSoundsets;
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.handSoundsets.size());

        for (Integer songTrackId : this.handSoundsets) {
            response.writeInt(songTrackId);
        }
    }

    @Override
    public short getHeader() {
        return 302; // "Dn"
    }
}
