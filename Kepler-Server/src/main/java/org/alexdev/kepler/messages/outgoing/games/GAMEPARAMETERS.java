package org.alexdev.kepler.messages.outgoing.games;

import org.alexdev.kepler.game.GameParameter;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

public class GAMEPARAMETERS extends MessageComposer {
    private GameParameter[] parameters;

    public GAMEPARAMETERS() {
        this.parameters = new GameParameter[] {
                new GameParameter("fieldType", true, "1", 1, 5),
                new GameParameter("numTeams", true, "2", 2, 4),
                new GameParameter("allowedPowerups", true,"1,2,3,4,5,6,7,8"),
                new GameParameter("name", true, "")
        };
    }

    @Override
    public void compose(NettyResponse response) {
        response.writeInt(this.parameters.length);

        for (GameParameter parameter : this.parameters) {
            response.writeString(parameter.getName());
            response.writeBool(!parameter.hasMinMax());
            response.writeInt(parameter.isEditable() ? 2 : 0);

            if (parameter.hasMinMax()) {
                response.writeInt(Integer.parseInt(parameter.getDefaultValue()));

                if (parameter.getMin() != -1) {
                    response.writeBool(true);
                    response.writeInt(parameter.getMin());
                }

                if (parameter.getMax() != -1) {
                    response.writeBool(true);
                    response.writeInt(parameter.getMax());
                }

            } else {
                response.writeString(parameter.getDefaultValue());
                response.writeInt(0);
            }
        }
    }

    @Override
    public short getHeader() {
        return 235; // "Ck"
    }
}
