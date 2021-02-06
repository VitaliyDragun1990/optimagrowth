package com.optimagrowth.license.domain.service.license;

import com.optimagrowth.license.domain.service.license.License;

import java.util.List;

public interface LicenseService {

    License getLicense(String licenseId, String organizationId);

    License createLicense(License license);

    License updateLicense(License license);

    String deleteLicense(String licenseId);

    List<License> getLicenses(String organizationId);
}
