package com.aburakkontas.manga_main.domain.repositories;

import com.aburakkontas.manga_main.domain.entities.work.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkRepository extends JpaRepository<Work, UUID> {
}
