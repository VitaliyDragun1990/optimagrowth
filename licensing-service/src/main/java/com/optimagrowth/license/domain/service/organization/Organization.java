package com.optimagrowth.license.domain.service.organization;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@RedisHash("organization")
public class Organization {

    @Id
    private String id;

    private String name;

    private String contactName;

    private String contactEmail;

    private String contactPhone;
}
