package com.optimagrowth.license.domain.service.impl;

import com.optimagrowth.license.config.Constants.Messages;
import com.optimagrowth.license.domain.entity.LicenseEntity;
import com.optimagrowth.license.domain.repository.LicenseRepository;
import com.optimagrowth.license.domain.service.LicenseService;
import com.optimagrowth.license.domain.service.Organization;
import com.optimagrowth.license.exception.ResourceNotFoundException;
import com.optimagrowth.license.domain.service.License;
import com.optimagrowth.license.provider.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseServiceImpl implements LicenseService {

    private final MessageProvider messageProvider;

    private final LicenseRepository licenseRepository;

    private final OrganizationClient organizationClient;

    private final ExampleProperties exampleProps;

    private final ModelMapper mapper;

    @Override
    public License getLicense(String licenseId, String organizationId) {
        LOG.debug("Searching for license with id:[{}] for organization with id:[{}]", licenseId, organizationId);

        LicenseEntity licenseEntity = licenseRepository
                .findByOrganizationIdAndLicenseId(organizationId, licenseId)
                .orElseThrow(() -> new ResourceNotFoundException(messageProvider.message(Messages.SEARCH_ERROR, licenseId, organizationId)));

        Organization organization = organizationClient.findById(organizationId);

        License license = mapper.map(licenseEntity, License.class);
        license.setOrganization(organization);

        LOG.debug("Found license with id:[{}] for organization with id:[{}]", licenseId, organizationId);

        return license.withComment(exampleProps.property());
    }

    @Override
    public List<License> getLicenses(String organizationId) {
        LOG.debug("Searching for licenses for organization with id:[{}]", organizationId);

        Organization organization = organizationClient.findById(organizationId);

        List<LicenseEntity> licenseEntities = licenseRepository.findByOrganizationId(organizationId);

        List<License> licenses = licenseEntities.stream()
                .map(l -> mapper.map(l, License.class))
                .peek(l -> l.setOrganization(organization))
                .collect(toList());

        LOG.debug("Found {} license(s) for organization with id:[{}]", licenses.size(), organizationId);

        return licenses;
    }

    @Transactional
    @Override
    public License createLicense(License license) {
        LOG.debug("Creating license using data:{}", license);

        LicenseEntity licenseEntity = mapper.map(license, LicenseEntity.class);
        licenseEntity.setLicenseId(UUID.randomUUID().toString());

        licenseRepository.save(licenseEntity);

        LOG.debug("Created license with data:{}", licenseEntity);

        return mapper.map(licenseEntity, License.class).withComment(exampleProps.property());
    }

    @Override
    public License updateLicense(License license) {
        LOG.debug("Updating license using data:{}", license);

        LicenseEntity licenseEntity = mapper.map(license, LicenseEntity.class);
        licenseRepository.save(licenseEntity);

        LOG.debug("Updated license with data:{}", licenseEntity);

        return mapper.map(licenseEntity, License.class).withComment(exampleProps.property());
    }

    @Override
    public String deleteLicense(String licenseId) {
        LOG.debug("Deleting license with id:{}", licenseId);

        licenseRepository.deleteById(licenseId);

        return messageProvider.message(Messages.LICENSE_DELETED, licenseId);
    }
}
