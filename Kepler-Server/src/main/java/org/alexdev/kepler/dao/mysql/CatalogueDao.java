package org.alexdev.kepler.dao.mysql;

import org.alexdev.kepler.dao.Storage;
import org.alexdev.kepler.game.catalogue.CatalogueItem;
import org.alexdev.kepler.game.catalogue.CataloguePackage;
import org.alexdev.kepler.game.catalogue.CataloguePage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CatalogueDao {

    /**
     * Get the catalogue pages.
     *
     * @return the list of catalogue pages
     */
    public static List<CataloguePage> getPages() {
        List<CataloguePage> pages = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM catalogue_pages ORDER BY order_id ASC", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CataloguePage page = new CataloguePage(resultSet.getInt("id"), resultSet.getInt("min_role"), resultSet.getBoolean("index_visible"),
                        resultSet.getString("name_index"), resultSet.getString("link_list"), resultSet.getString("name"), resultSet.getString("layout"),
                        resultSet.getString("image_headline"), resultSet.getString("image_teasers"), resultSet.getString("body"),
                        resultSet.getString("label_pick"), resultSet.getString("label_extra_s"), resultSet.getString("label_extra_t"));

                pages.add(page);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return pages;
    }

    /**
     * Get the catalogue items.
     *
     * @return the list of catalogue items
     */
    public static List<CatalogueItem> getItems() {
        List<CatalogueItem> pages = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM catalogue_items ORDER BY order_id ASC", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CatalogueItem item = new CatalogueItem(resultSet.getString("sale_code"), resultSet.getInt("page_id"),
                        resultSet.getInt("order_id"), resultSet.getInt("price"), resultSet.getInt("definition_id"),
                        resultSet.getInt("item_specialspriteid"), resultSet.getString("package_name"),
                        resultSet.getString("package_description"), resultSet.getBoolean("is_package"));

                pages.add(item);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return pages;
    }

    /**
     * Get the catalogue packages.
     *
     * @return the list of catalogue packages
     */
    public static List<CataloguePackage> getPackages() {
        List<CataloguePackage> packages = new ArrayList<>();

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("SELECT * FROM catalogue_packages", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CataloguePackage cataloguePackage = new CataloguePackage(resultSet.getString("salecode"), resultSet.getInt("definition_id"),
                        resultSet.getInt("special_sprite_id"), resultSet.getInt("amount"));

                packages.add(cataloguePackage);
            }

        } catch (Exception e) {
            Storage.logError(e);
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return packages;
    }
}
