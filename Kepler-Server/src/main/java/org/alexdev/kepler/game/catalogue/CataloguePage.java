package org.alexdev.kepler.game.catalogue;

public class CataloguePage {
    private int id;
    private int minRole;
    private String nameIndex;
    private String name;
    private String layout;
    private String imageHeadline;
    private String imageTeasers;
    private String body;
    private String labelPick;
    private String labelExtra_s;
    private String labelExtra_t;

    public CataloguePage(int id, int minRole, String nameIndex, String name, String layout, String imageHeadline, String imageTeasers, String body, String labelPick, String labelExtra_s, String labelExtra_t) {
        this.id = id;
        this.minRole = minRole;
        this.nameIndex = nameIndex;
        this.name = name;
        this.layout = layout;
        this.imageHeadline = imageHeadline;
        this.imageTeasers = imageTeasers;
        this.body = body;
        this.labelPick = labelPick;
        this.labelExtra_s = labelExtra_s;
        this.labelExtra_t = labelExtra_t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinRole() {
        return minRole;
    }

    public void setMinRole(int minRole) {
        this.minRole = minRole;
    }

    public String getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(String nameIndex) {
        this.nameIndex = nameIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getImageHeadline() {
        return imageHeadline;
    }

    public void setImageHeadline(String imageHeadline) {
        this.imageHeadline = imageHeadline;
    }

    public String getImageTeasers() {
        return imageTeasers;
    }

    public void setImageTeasers(String imageTeasers) {
        this.imageTeasers = imageTeasers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLabelPick() {
        return labelPick;
    }

    public void setLabelPick(String labelPick) {
        this.labelPick = labelPick;
    }

    public String getLabelExtra_s() {
        return labelExtra_s;
    }

    public void setLabelExtra_s(String labelExtra_s) {
        this.labelExtra_s = labelExtra_s;
    }

    public String getLabelExtra_t() {
        return labelExtra_t;
    }

    public void setLabelExtra_t(String labelExtra_t) {
        this.labelExtra_t = labelExtra_t;
    }
}
