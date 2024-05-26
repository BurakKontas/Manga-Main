package com.aburakkontas.manga_main.domain.repositories;

import com.aburakkontas.manga_main.domain.entities.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
}

