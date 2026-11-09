package com.trustvault.repository;
import com.trustvault.model.StoredDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DocumentRepository extends JpaRepository<StoredDocument,Long> {
 List<StoredDocument> findAllByOrderByUploadedAtDesc();
 List<StoredDocument> findByOwnerEmailOrderByUploadedAtDesc(String email);
}
