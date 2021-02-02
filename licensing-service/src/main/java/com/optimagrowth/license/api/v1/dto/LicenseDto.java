package com.optimagrowth.license.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDto extends RepresentationModel<LicenseDto> {

    private String licenseId;

    private String organizationId;

    private String description;

    private String productName;

    private String licenseType;

    private OrganizationDto organization;
}
