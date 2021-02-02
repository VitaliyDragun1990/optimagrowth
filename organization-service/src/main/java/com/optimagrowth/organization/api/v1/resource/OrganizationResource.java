package com.optimagrowth.organization.api.v1.resource;

import com.optimagrowth.organization.api.ApiVersion;
import com.optimagrowth.organization.api.v1.dto.OrganizationDto;
import com.optimagrowth.organization.domain.service.OrganizationService;
import com.optimagrowth.organization.domain.service.model.Organization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.optimagrowth.organization.api.v1.resource.OrganizationResource.BASE_URL;
import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class OrganizationResource {

    static final String BASE_URL = ApiVersion.V_1 + "/organizations";

    private final OrganizationService service;

    private final ModelMapper mapper;

    @GetMapping("/{organizationId}")
    public OrganizationDto getOrganization(@PathVariable("organizationId") String organizationId) {
        LOG.debug("Received GET request to search for organization with id:[{}]", organizationId);

        Organization organization = service.findById(organizationId);

        OrganizationDto dto = mapper.map(organization, OrganizationDto.class);

        enhanceWithLinks(dto);

        return dto;
    }

    @GetMapping
    public List<OrganizationDto> getOrganizations() {
        LOG.debug("Received GET request to search for all organizations");

        List<Organization> organizations = service.findAll();

        List<OrganizationDto> dtos = organizations.stream().map(o -> mapper.map(o, OrganizationDto.class)).collect(toList());

        dtos.forEach(this::enhanceWithLinks);

        return dtos;
    }

    @PostMapping
    public OrganizationDto saveOrganization(@RequestBody OrganizationDto request) {
        LOG.debug("Received POST request to save organization with data:{}", request);

        Organization savedOrganization = service.create(mapper.map(request, Organization.class));

        OrganizationDto responseDto = mapper.map(savedOrganization, OrganizationDto.class);

        enhanceWithLinks(responseDto);

        return responseDto;
    }

    @PutMapping("/{organizationId}")
    public OrganizationDto updateOrganization(
            @PathVariable("organizationId") String organizationId,
            @RequestBody OrganizationDto request) {
        LOG.debug("Received PUT request to update organization with id:[{}] with data:{}", organizationId, request);

        Organization updatedOrganization = service.update(mapper.map(request, Organization.class));

        OrganizationDto responseDto = mapper.map(updatedOrganization, OrganizationDto.class);

        enhanceWithLinks(responseDto);

        return responseDto;
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable("organizationId") String organizationId) {
        service.deleteById(organizationId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void enhanceWithLinks(OrganizationDto dto) {
        dto.add(
                linkTo(methodOn(OrganizationResource.class).getOrganization(dto.getId())).withSelfRel(),
                linkTo(methodOn(OrganizationResource.class).updateOrganization(dto.getId(), dto)).withRel("updateOrganization"),
                linkTo(methodOn(OrganizationResource.class).deleteOrganization(dto.getId())).withRel("deleteOrganization"));
    }
}
