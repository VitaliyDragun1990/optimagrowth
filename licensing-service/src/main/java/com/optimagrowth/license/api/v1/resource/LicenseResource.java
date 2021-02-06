package com.optimagrowth.license.api.v1.resource;

import com.optimagrowth.license.api.ApiVersion;
import com.optimagrowth.license.api.v1.dto.LicenseDto;
import com.optimagrowth.license.domain.service.license.LicenseService;
import com.optimagrowth.license.domain.service.license.License;
import com.optimagrowth.license.usercontext.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(LicenseResource.BASE_URL)
public class LicenseResource {

    static final String BASE_URL = ApiVersion.V_1 + "/organizations";

    private final LicenseService licenseService;

    private final ModelMapper mapper;

    @GetMapping("/{organizationId}/licenses")
    public List<LicenseDto> getLicenses(@PathVariable("organizationId") String organizationId) {
        LOG.debug("Received GET request to search for licenses for organization with id:[{}], Correlation Id: {}",
                organizationId, UserContextHolder.getContext().getCorrelationId());

        List<License> licenses = licenseService.getLicenses(organizationId);

        List<LicenseDto> dtos = licenses.stream().map(l -> mapper.map(l, LicenseDto.class)).collect(toList());

        dtos.forEach(this::enhanceWithLinks);

        return dtos;
    }

    @GetMapping("/{organizationId}/licenses/{licenseId}")
    public LicenseDto getLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        LOG.debug("Received GET request to search for license with id:[{}] for organization with id:[{}]", licenseId, organizationId);

        License license = licenseService.getLicense(licenseId, organizationId);

        LicenseDto dto = mapper.map(license, LicenseDto.class);

        enhanceWithLinks(dto);

        return dto;
    }

    @PutMapping("/{organizationId}/licenses/{licenseId}")
    public ResponseEntity<LicenseDto> updateLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @RequestBody LicenseDto request) {
        LOG.debug("Received PUT request to update license with id:[{}] for organization with id:[{}], data:[{}]",
                licenseId, organizationId, request);

        License result = licenseService.updateLicense(mapper.map(request, License.class));

        LicenseDto dto = mapper.map(result, LicenseDto.class);

        enhanceWithLinks(dto);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{organizationId}/licenses")
    public ResponseEntity<LicenseDto> createLicense(
            @PathVariable("organizationId") String organizationId, @RequestBody LicenseDto request) {
        LOG.debug("Received POST request to create license for organization with id:[{}], data:[{}]",
                organizationId, request);

        License result = licenseService.createLicense(mapper.map(request, License.class));

        LicenseDto dto = mapper.map(result, LicenseDto.class);

        enhanceWithLinks(dto);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{organizationId}/licenses/{licenseId}")
    public ResponseEntity<String> deleteLicense(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        LOG.debug("Received DELETE request to delete license with id:[{}] for organization with id:[{}]",
                licenseId, organizationId);

        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }

    private void enhanceWithLinks(LicenseDto dto) {
        dto.add(
                linkTo(methodOn(LicenseResource.class).getLicense(dto.getOrganizationId(), dto.getLicenseId())).withSelfRel(),
                linkTo(methodOn(LicenseResource.class).createLicense(dto.getOrganizationId(), dto)).withRel("createLicense"),
                linkTo(methodOn(LicenseResource.class).updateLicense(dto.getOrganizationId(), dto.getLicenseId(), dto)).withRel("updateLicense"),
                linkTo(methodOn(LicenseResource.class).deleteLicense(dto.getOrganizationId(), dto.getLicenseId())).withRel("deleteLicense"));
    }
}
