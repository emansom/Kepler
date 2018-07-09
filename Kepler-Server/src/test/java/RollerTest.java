import junit.framework.TestCase;
import org.alexdev.kepler.Kepler;
import org.alexdev.kepler.game.entity.Entity;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.ItemManager;
import org.alexdev.kepler.game.pathfinder.Position;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.game.player.PlayerManager;
import org.alexdev.kepler.game.room.Room;
import org.alexdev.kepler.game.room.RoomManager;
import org.alexdev.kepler.server.netty.NettyPlayerNetwork;
import org.junit.Assert;

import java.util.ArrayList;

public class RollerTest extends TestCase {
    /*private final Player player;
    private final Room room;

    public RollerTest() {
        Kepler.main(null);

        this.player = new Player(new NettyPlayerNetwork(null, 0));
        this.player.getDetails().fill(1, "Alex", "", "", 1000, "I'm a bot lol!",
                "LOL!", "M", 100, 100, 1, -1, -1, -1,
                "ADM", false, new ArrayList<>(), true, true);

        this.player.login();

        this.room = new Room();
        this.room.getData().fill(1, this.player.getDetails().getId(), 3, "Alex's Room", "", "model_a",
                "", 100, 100, true, false, 0, "", 0, 25);

        PlayerManager.getInstance().addPlayer(this.player);
        RoomManager.getInstance().addRoom(this.room);

        this.room.getEntityManager().enterRoom(this.player, null);
    }

    public void testRoomPopulation() {
        assertEquals(1, this.room.getEntityManager().getPlayers().size());
    }

    public void testRollerPlacement() {
        Item item = new Item();
        item.setRoomId(this.room.getId());
        item.setDefinitionId(ItemManager.getInstance().getDefinitionBySprite("queue_tile1*6").getId());
        item.setPosition(new Position(6, 3, 0));

        this.room.getMapping().addItem(item);
        this.player.getRoomUser().walkTo(6, 3);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(3, this.player.getRoomUser().getPosition().getX());
        assertEquals(5, this.player.getRoomUser().getPosition().getY());
    }*/
}