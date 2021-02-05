package com.optimagrowth.license.domain.service.impl;

import com.optimagrowth.license.config.Constants.Messages;
import com.optimagrowth.license.domain.entity.LicenseEntity;
import com.optimagrowth.license.domain.repository.LicenseRepository;
import com.optimagrowth.license.domain.service.License;
import com.optimagrowth.license.domain.service.LicenseService;
import com.optimagrowth.license.domain.service.Organization;
import com.optimagrowth.license.exception.ResourceNotFoundException;
import com.optimagrowth.license.provider.message.MessageProvider;
import com.optimagrowth.license.usercontext.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

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

    @CircuitBreaker(name = "licenseService", fallbackMethod = "getLicensesFallback")
    @RateLimiter(name = "licenseService", fallbackMethod = "getLicensesFallback")
    @Retry(name = "retryLicenseService", fallbackMethod = "getLicensesFallback")
    @Bulkhead(name = "bulkheadLicenseService", type = Type.SEMAPHORE, fallbackMethod = "getLicensesFallback")
    @Override
    public List<License> getLicenses(String organizationId) {
        LOG.debug("Searching for licenses for organization with id:[{}]", organizationId);

        randomlyRunLong();

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
        LOG.debug("Creating license using data:{}, Correlation Id:{}", license, UserContextHolder.getContext().getCorrelationId());

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

    @SuppressWarnings("unused")
    private List<License> getLicensesFallback(String organizationId, Throwable t) {
        LOG.warn("Fail to get licenses - using fallback method", t);

        License fallbackLicense = new License();
        fallbackLicense.setLicenseId("00000000-00-00000");
        fallbackLicense.setOrganizationId(organizationId);
        fallbackLicense.setProductName("Sorry no licensing information currently available");

        return List.of(fallbackLicense);
    }

    private void randomlyRunLong() {
        LOG.warn("Entering long waiting process");
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) {
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
            throw new TimeoutException("Database call dummy timeout");
        } catch (InterruptedException | TimeoutException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
