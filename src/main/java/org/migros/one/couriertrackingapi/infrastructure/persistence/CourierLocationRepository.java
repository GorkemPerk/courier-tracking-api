package org.migros.one.couriertrackingapi.infrastructure.persistence;

import org.migros.one.couriertrackingapi.domain.entity.CourierLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {
    Optional<CourierLocation> findTopByCourierIdAndStoreIdOrderByTrackingEntryDateDesc(String courierId, Long storeId);

    List<CourierLocation> findByCourierIdOrderByTrackingEntryDateAsc(String courierId);
}
