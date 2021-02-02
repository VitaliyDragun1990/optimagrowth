package com.optimagrowth.organization.domain.repository;

import com.optimagrowth.organization.domain.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, String> {

}
