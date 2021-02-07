package com.romanboehm.wichtelnng.repository;

import com.romanboehm.wichtelnng.model.entity.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @EntityGraph(attributePaths = "participants")
    List<Event> findAllByDeadline(LocalDate now);
}
