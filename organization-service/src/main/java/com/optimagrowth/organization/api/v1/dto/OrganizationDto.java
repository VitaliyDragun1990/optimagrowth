package com.optimagrowth.organization.api.v1.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
public class OrganizationDto extends RepresentationModel<OrganizationDto> {

    private String id;

    private String name;

    private String contactName;

    private String contactEmail;

    private String contactPhone;
}
