package com.romanboehm.wichtelnng.repository;

import com.romanboehm.wichtelnng.model.entity.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Override
    @EntityGraph(attributePaths = "participants", type = FETCH)
    List<Event> findAll();
}
