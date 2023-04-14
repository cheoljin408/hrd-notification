package util.hrdnotification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import util.hrdnotification.entity.HrdStudent;

public interface HrdStudentRepository extends JpaRepository<HrdStudent, Long> {
}
