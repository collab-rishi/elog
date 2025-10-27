package tendering.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tendering.model.Signup;
import tendering.model.UserRole;

@Repository
public interface SignupRepository extends JpaRepository<Signup, Long> {
    Optional<Signup> findByEmail(String email);
    Optional<Signup> findByMobileNumber(String mobileNumber);
    Optional<Signup> findByCompanyName(String companyName);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByCompanyName(String companyName);
    List<Signup> findByRole(UserRole role);
}