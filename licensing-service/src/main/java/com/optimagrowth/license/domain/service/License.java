package com.optimagrowth.license.domain.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class License {

    private String licenseId;

    private String organizationId;

    private String description;

    private String productName;

    private String licenseType;

    private String comment;

    private Organization organization;

    public License withComment(String comment) {
        this.setComment(comment);
        return this;
    }
}
