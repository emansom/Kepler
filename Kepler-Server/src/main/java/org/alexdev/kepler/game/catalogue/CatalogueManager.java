package org.alexdev.kepler.game.catalogue;

import org.alexdev.kepler.dao.mysql.CatalogueDao;

import java.util.ArrayList;
import java.util.List;

public class CatalogueManager {
    private static CatalogueManager instance;

    private List<CataloguePage> cataloguePageList;
    private List<CatalogueItem> catalogueItemList;

    public CatalogueManager() {
        this.cataloguePageList = CatalogueDao.getPages();
        this.catalogueItemList = CatalogueDao.getItems();
    }

    /**
     * Get a page by the page index
     *
     * @param pageIndex the index of the page to get for
     * @return the catalogue page
     */
    public CataloguePage getCataloguePage(String pageIndex) {
        for (CataloguePage cataloguePage : this.cataloguePageList) {
            if (cataloguePage.getNameIndex().equals(pageIndex)) {
                return cataloguePage;
            }
        }

        return null;
    }


    /**
     * Get a list of items for the catalogue page.
     *
     * @param pageId the id of the page to get the items for
     * @return the list of items
     */
    public List<CatalogueItem> getCataloguePageItems(int pageId) {
        List<CatalogueItem> items = new ArrayList<>();

        for (CatalogueItem catalogueItem : this.catalogueItemList) {
            if (catalogueItem.getPageId() == pageId) {
                items.add(catalogueItem);
            }
        }

        return items;
    }

    /**
     * Get catalogue page list.
     *
     * @return the list of catalogue pages
     */
    public List<CataloguePage> getCataloguePages() {
        return cataloguePageList;
    }

    /**
     * Get the {@link CatalogueManager} instance
     *
     * @return the catalogue manager instance
     */
    public static CatalogueManager getInstance() {
        if (instance == null) {
            instance = new CatalogueManager();
        }

        return instance;
    }



}
