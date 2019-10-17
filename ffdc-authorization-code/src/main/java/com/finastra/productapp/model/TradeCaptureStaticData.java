package com.finastra.productapp.model;

public class TradeCaptureStaticData {
    private String id;
    private String description;
    private String [] applicableEntities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getApplicableEntities() {
        return applicableEntities;
    }

    public void setApplicableEntities(String[] applicableEntities) {
        this.applicableEntities = applicableEntities;
    }
}
