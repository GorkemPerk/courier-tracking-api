package org.migros.one.couriertrackingapi.infrastructure.persistence;

import org.migros.one.couriertrackingapi.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
