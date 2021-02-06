package com.optimagrowth.license.domain.repository.jpa;

import com.optimagrowth.license.domain.entity.LicenseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LicenseRepository extends CrudRepository<LicenseEntity, String> {

    List<LicenseEntity> findByOrganizationId(String organizationId);

    Optional<LicenseEntity> findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
