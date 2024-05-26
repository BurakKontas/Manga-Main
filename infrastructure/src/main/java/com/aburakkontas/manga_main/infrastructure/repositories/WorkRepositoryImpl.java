package com.aburakkontas.manga_main.infrastructure.repositories;

import com.aburakkontas.manga_main.domain.repositories.WorkRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface WorkRepositoryImpl extends WorkRepository {
}
