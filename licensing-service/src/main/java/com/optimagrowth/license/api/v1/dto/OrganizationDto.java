package com.optimagrowth.license.api.v1.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OrganizationDto {

    private String id;

    private String name;

    private String contactName;

    private String contactEmail;

    private String contactPhone;
}
