// --- Repository: LspResponseRepository.java ---
package tendering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tendering.model.LspResponse;
import tendering.model.LspResponse.SelectionStatus;
import tendering.model.LspResponse.Status;

import java.util.List;
import java.util.Optional;

//@Repository
//public interface LspResponseRepository extends JpaRepository<LspResponse, Long> {
//    
//    List<LspResponse> findByLspCompanyName(String companyName);
//    
//    Optional<LspResponse> findByTenderNoAndLspCompanyName(String tenderNo, String companyName);
//    
//    List<LspResponse> findByLspCompanyNameAndStatus(String companyName, Status status);
//    
//    List<LspResponse> findByCreatedByCompanyNameAndTenderNo(String createdByCompanyName, String tenderNo);
//    
//    // New methods for selection status
//    List<LspResponse> findByTenderNo(String tenderNo);
//    
//    List<LspResponse> findByTenderNoAndSelectionStatus(String tenderNo, SelectionStatus selectionStatus);
//
//    Optional<LspResponse> findFirstByTenderNoAndLspCompanyName(String tenderNo, String lspCompanyName);
//    
//    @Modifying
//    @Query("UPDATE LspResponse l SET l.selectionStatus = :status WHERE l.tenderNo = :tenderNo")
//    void updateSelectionStatusByTenderNo(@Param("tenderNo") String tenderNo, @Param("status") SelectionStatus status);
//    
//    @Modifying
//    @Query("UPDATE LspResponse l SET l.selectionStatus = :status WHERE l.id = :id")
//    void updateSelectionStatusById(@Param("id") Long id, @Param("status") SelectionStatus status);
//}
@Repository
public interface LspResponseRepository extends JpaRepository<LspResponse, Long> {
    
    List<LspResponse> findByLspCompanyName(String companyName);
    
    Optional<LspResponse> findByTenderNoAndLspCompanyName(String tenderNo, String companyName);
    
    List<LspResponse> findByLspCompanyNameAndStatus(String companyName, Status status);
    
    List<LspResponse> findByCreatedByCompanyNameAndTenderNo(String createdByCompanyName, String tenderNo);
    
    List<LspResponse> findByTenderNo(String tenderNo);
    
    List<LspResponse> findByTenderNoAndSelectionStatus(String tenderNo, SelectionStatus selectionStatus);

    Optional<LspResponse> findFirstByTenderNoAndLspCompanyName(String tenderNo, String lspCompanyName);

    @Modifying
    @Query("UPDATE LspResponse l SET l.selectionStatus = :status WHERE l.tenderNo = :tenderNo AND l.lspCompanyName <> :lspCompanyName")
    void updateSelectionStatusByTenderNo(@Param("tenderNo") String tenderNo, @Param("status") SelectionStatus status, @Param("lspCompanyName") String lspCompanyName);

    @Modifying
    @Query("UPDATE LspResponse l SET l.selectionStatus = :status WHERE l.id = :id")
    void updateSelectionStatusById(@Param("id") Long id, @Param("status") SelectionStatus status);
}
