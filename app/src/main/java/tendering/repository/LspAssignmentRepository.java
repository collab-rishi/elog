package tendering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tendering.model.LspAssignment;

import java.util.List;
import java.util.Optional;

@Repository
public interface LspAssignmentRepository extends JpaRepository<LspAssignment, Long> {
    
    Optional<LspAssignment> findByLspResponseId(Long lspResponseId);
    
    Optional<LspAssignment> findByTenderNo(String tenderNo);
    
    List<LspAssignment> findByLspCompanyName(String lspCompanyName);
    
    List<LspAssignment> findByAssignedBy3pl(String threePlCompanyName);
    
    boolean existsByLspResponseId(Long lspResponseId);
    
    boolean existsByTenderNo(String tenderNo);
}



