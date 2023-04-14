package util.hrdnotification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.hrdnotification.entity.HrdStudent;
import util.hrdnotification.repository.HrdStudentRepository;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final HrdStudentRepository hrdStudentRepository;

    @GetMapping("/")
    public ResponseEntity<HrdStudent> getStudent(@RequestParam Long id) {
        HrdStudent student = hrdStudentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return ResponseEntity.ok().body(student);
    }
}
