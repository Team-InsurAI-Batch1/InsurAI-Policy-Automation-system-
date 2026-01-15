package com.insurai.insurai_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.insurai.insurai_backend.model.EmployeeQuery;

@Repository
public interface EmployeeQueryRepository extends JpaRepository<EmployeeQuery, Long> {

    // Get all queries submitted by a specific employee
    List<EmployeeQuery> findByEmployeeId(Long employeeId);

    // Get all queries assigned to a specific agent
    List<EmployeeQuery> findByAgentId(Long agentId);

    // Get all queries by employee and status (e.g., only pending)
    List<EmployeeQuery> findByEmployeeIdAndStatus(Long employeeId, String status);

    // Get all queries by agent and status (e.g., only pending)
    List<EmployeeQuery> findByAgentIdAndStatus(Long agentId, String status);

    // Get all queries with a specific status (e.g., pending) regardless of agent
    List<EmployeeQuery> findByStatus(String status);

    // ================= OPTIMIZED QUERIES WITH JOIN FETCH =================
    
    // Fetch query by ID with employee eagerly loaded
    @Query("SELECT q FROM EmployeeQuery q JOIN FETCH q.employee WHERE q.id = :queryId")
    Optional<EmployeeQuery> findByIdWithEmployee(@Param("queryId") Long queryId);

    // Fetch query by ID with agent eagerly loaded
    @Query("SELECT q FROM EmployeeQuery q JOIN FETCH q.agent WHERE q.id = :queryId")
    Optional<EmployeeQuery> findByIdWithAgent(@Param("queryId") Long queryId);

    // Fetch query with both employee and agent
    @Query("SELECT q FROM EmployeeQuery q JOIN FETCH q.employee JOIN FETCH q.agent WHERE q.id = :queryId")
    Optional<EmployeeQuery> findByIdWithEmployeeAndAgent(@Param("queryId") Long queryId);

    // Get pending queries for an agent with employee eagerly loaded
    @Query("SELECT q FROM EmployeeQuery q JOIN FETCH q.employee WHERE q.agent.id = :agentId AND q.status = 'pending' ORDER BY q.createdAt ASC")
    List<EmployeeQuery> findPendingQueriesForAgentWithEmployee(@Param("agentId") Long agentId);

    // Get all queries for an agent with employee eagerly loaded
    @Query("SELECT q FROM EmployeeQuery q JOIN FETCH q.employee WHERE q.agent.id = :agentId ORDER BY q.createdAt DESC")
    List<EmployeeQuery> findQueriesForAgentWithEmployee(@Param("agentId") Long agentId);
    
}

