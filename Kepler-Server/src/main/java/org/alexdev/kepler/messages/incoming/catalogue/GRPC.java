package org.alexdev.kepler.messages.incoming.catalogue;

import org.alexdev.kepler.dao.mysql.CurrencyDao;
import org.alexdev.kepler.dao.mysql.ItemDao;
import org.alexdev.kepler.dao.mysql.PlayerDao;
import org.alexdev.kepler.dao.mysql.TeleporterDao;
import org.alexdev.kepler.game.catalogue.CatalogueItem;
import org.alexdev.kepler.game.catalogue.CatalogueManager;
import org.alexdev.kepler.game.catalogue.CataloguePackage;
import org.alexdev.kepler.game.catalogue.CataloguePage;
import org.alexdev.kepler.game.item.Item;
import org.alexdev.kepler.game.item.base.ItemBehaviour;
import org.alexdev.kepler.game.item.base.ItemDefinition;
import org.alexdev.kepler.game.player.Player;
import org.alexdev.kepler.messages.outgoing.catalogue.NO_CREDITS;
import org.alexdev.kepler.messages.outgoing.user.CREDIT_BALANCE;
import org.alexdev.kepler.messages.types.MessageEvent;
import org.alexdev.kepler.server.netty.streams.NettyRequest;
import org.alexdev.kepler.util.DateUtil;
import org.alexdev.kepler.util.StringUtil;

import java.sql.SQLException;

public class GRPC implements MessageEvent {
    @Override
    public void handle(Player player, NettyRequest reader) throws SQLException {
        String content = reader.contents();
        String[] data = content.split(Character.toString((char) 13));

        String pageIndex = data[1];
        String saleCode = data[3];

        CataloguePage page = CatalogueManager.getInstance().getCataloguePage(pageIndex);

        if (page == null || player.getDetails().getRank() < page.getMinRole()) {
            return;
        }

        CatalogueItem item = CatalogueManager.getInstance().getCatalogueItem(saleCode);

        if (item == null) {
            return;
        }

        if (item.getPrice() > player.getDetails().getCredits()) {
            player.send(new NO_CREDITS());
            return;
        }

        if (!item.isPackage()) {
            String extraData = data[4];
            purchase(player, item.getDefinition(), extraData, item.getItemSpecialId());
        } else {
            for (CataloguePackage cataloguePackage : item.getPackages()) {
                for (int i = 0; i < cataloguePackage.getAmount(); i++){
                    purchase(player, cataloguePackage.getDefinition(), null, cataloguePackage.getSpecialSpriteId());
                }
            }
        }

        CurrencyDao.decreaseCredits(player.getDetails(), item.getPrice());
        player.send(new CREDIT_BALANCE(player.getDetails()));

        player.getInventory().getView("last");
    }

    private void purchase(Player player, ItemDefinition def, String extraData, int specialSpriteId) throws SQLException {
        String customData = "";

        if (extraData != null) {
            if (def.hasBehaviour(ItemBehaviour.DECORATION)) {
                customData = extraData;
            } else {
                if (specialSpriteId > 0) {
                    customData = String.valueOf(specialSpriteId);
                }
            }

            if (def.hasBehaviour(ItemBehaviour.POST_IT)) {
                customData = "20";
            }

            if (def.hasBehaviour(ItemBehaviour.PRIZE_TROPHY)) {
                customData += player.getDetails().getName();
                customData += (char)9;

                customData += DateUtil.getShortDate();
                customData += (char)9;

                customData += StringUtil.filterInput(extraData, true);
            }
        }

        Item item = new Item();
        item.setOwnerId(player.getDetails().getId());
        item.setDefinitionId(def.getId());
        item.setCustomData(customData);

        ItemDao.newItem(item);
        player.getInventory().getItems().add(item);

        if (def.hasBehaviour(ItemBehaviour.TELEPORTER)) {
            Item linkedTeleporterItem = new Item();
            linkedTeleporterItem.setOwnerId(player.getDetails().getId());
            linkedTeleporterItem.setDefinitionId(def.getId());
            linkedTeleporterItem.setCustomData(customData);

            ItemDao.newItem(linkedTeleporterItem);
            player.getInventory().getItems().add(linkedTeleporterItem);
            
            linkedTeleporterItem.setTeleporterId(item.getId());
            item.setTeleporterId(linkedTeleporterItem.getId());

            TeleporterDao.addPair(linkedTeleporterItem.getId(), item.getId());
            TeleporterDao.addPair(item.getId(), linkedTeleporterItem.getId());
        }
    }
}
