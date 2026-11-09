package com.trustvault.repository;
import com.trustvault.model.DocumentGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DocumentGrantRepository extends JpaRepository<DocumentGrant,Long> {
 boolean existsByDocumentIdAndUserId(Long documentId,Long userId);
 List<DocumentGrant> findByUserEmail(String email);
}
