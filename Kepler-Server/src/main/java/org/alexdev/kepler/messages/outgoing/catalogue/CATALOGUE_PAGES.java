package org.alexdev.kepler.messages.outgoing.catalogue;

import org.alexdev.kepler.game.catalogue.CataloguePage;
import org.alexdev.kepler.messages.types.MessageComposer;
import org.alexdev.kepler.server.netty.streams.NettyResponse;

import java.util.List;

public class CATALOGUE_PAGES extends MessageComposer {
    private final int role;
    private final List<CataloguePage> cataloguePages;

    public CATALOGUE_PAGES(int role, List<CataloguePage> cataloguePages) {
        this.role = role;
        this.cataloguePages = cataloguePages;
    }

    @Override
    public void compose(NettyResponse response) {
        for (CataloguePage page : this.cataloguePages) {
            if (!page.isIndexVisible()) {
                continue;
            }

            if (this.role >= page.getMinRole()) {
                response.writeDelimeter(page.getNameIndex(), (char) 9);
                response.writeDelimeter(page.getName(), (char) 13);
            }
        }
    }

    @Override
    public short getHeader() {
        return 126; // "A~"
    }
}
