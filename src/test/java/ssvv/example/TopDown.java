package ssvv.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TopDown {
    private Service service;

    private StudentXMLRepo studentXMLRepository = mock(StudentXMLRepo.class);
    private TemaXMLRepo temaXMLRepository = mock(TemaXMLRepo.class);
    private NotaXMLRepo notaXMLRepository = mock(NotaXMLRepo.class);

    private StudentValidator studentValidator = mock(StudentValidator.class);
    private TemaValidator temaValidator = mock(TemaValidator.class);
    private NotaValidator notaValidator = mock(NotaValidator.class);

    @BeforeEach
    public void setUp() {
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }
    @AfterEach
    public void tearDown() throws IOException {
        Path file = Paths.get("./fisiere_test/Studenti.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
        file = Paths.get("./fisiere_test/Teme.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
        file = Paths.get("./fisiere_test/Note.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
    }

    @Test
    void addStudent_allStubs() {
        Student student = new Student("2", "name", 1, "email");

        when(studentXMLRepository.save(any())).thenReturn(null);

        try {
            service.addStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(studentValidator).validate(any());
        verify(studentXMLRepository).save(any());
    }

    @Test
    void addStudent_repoStub() {
        StudentValidator studentValidatorReal = new StudentValidator();
        service = new Service(studentXMLRepository, studentValidatorReal, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
        Student student = new Student("2", "name", 1, "email");
        try {
            service.addStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(studentXMLRepository).save(any());
    }

    @Test
    void addStudent_noStub() throws IOException {
        StudentValidator studentValidatorReal = new StudentValidator();
        String filenameStudent = "./fisiere_test/Studenti.xml";
        StudentXMLRepo studentXMLRepositoryReal = new StudentXMLRepo(filenameStudent);

        service = new Service(studentXMLRepositoryReal, studentValidatorReal, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
        Student student = new Student("2", "name", 1, "email");
        try {
            assertNull(service.addStudent(student));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Path file = Paths.get("./fisiere_test/Studenti.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
    }

    @Test
    void addAssignment_allStubs() {
        Student student = new Student("2", "name", 1, "email");
        Tema tema = new Tema("123", "abc", 14, 1);

        when(studentXMLRepository.save(any())).thenReturn(null);
        when(temaXMLRepository.save(any())).thenReturn(null);

        try {
            service.addStudent(student);
            service.addTema(tema);
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(studentValidator).validate(any());
        verify(studentXMLRepository).save(any());

        verify(temaValidator).validate(any());
        verify(temaXMLRepository).save(any());
    }

    @Test
    void addGrade_allStubs() {
        Student student = new Student("2", "name", 1, "email");
        Tema tema = new Tema("123", "abc", 14, 1);
        Nota nota = new Nota("1","2","123",10.0, LocalDate.now());

        when(studentXMLRepository.save(any())).thenReturn(null);
        when(temaXMLRepository.save(any())).thenReturn(null);
        when(notaXMLRepository.save(any())).thenReturn(null);

        try {
            service.addStudent(student);
            service.addTema(tema);
            service.addNota(nota, "asd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(studentValidator).validate(any());
        verify(studentXMLRepository).save(any());

        verify(temaValidator).validate(any());
        verify(temaXMLRepository).save(any());
    }
}
