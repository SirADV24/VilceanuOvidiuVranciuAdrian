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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class Integration {
    private Service service;

    @BeforeEach
    public void setUp() {
        String filenameStudent = "./fisiere_test/Studenti.xml";
        String filenameTema = "./fisiere_test/Teme.xml";
        String filenameNota = "./fisiere_test/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);

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

    public void addStudent() throws Exception {
        Student student = new Student("123", "name", 100, "oviAdi@gmail.com");
        assertNull(service.addStudent(student));
    }

    public void addAssignment() {
        assertNull(service.addTema(new Tema("123", "abc", 14, 1)));
    }

    public void addGrade() {
        service.addNota(new Nota("1","123","123",10.0, LocalDate.now()),"asd");
        assertEquals(10.0, service.addNota(new Nota("1","123","123",10.0, LocalDate.now()),"good"));
    }

    @Test
    public void addStudent_integration() throws Exception {
        addStudent();
    }

    @Test
    public void addAssignment_integration() throws Exception {
        addStudent();
        addAssignment();
    }

    @Test
    public void addGrade_integration() throws Exception {
        addStudent();
        addAssignment();
        addGrade();
    }
}
