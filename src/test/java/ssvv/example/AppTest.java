package ssvv.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ssvv.example.domain.Student;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppTest 
{
    private Service service;

    @BeforeEach
    public void setUp() {
        String filenameStudent = "fisiere_test/Studenti.xml";
        String filenameTema = "fisiere_test/Teme.xml";
        String filenameNota = "fisiere_test/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);

        this.service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @AfterEach
    public void cleanup() throws IOException {
        // Cleanup files after each test run to keep validity of i/o operations
        Path file = Paths.get("fisiere_test/Studenti.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
        file = Paths.get("fisiere_test/Teme.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
        file = Paths.get("fisiere_test/Note.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
    }

    @Test
    public void test_1_addStudent_success() throws Exception {
        // Dummy test to test Jetkins
        service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com"));
    }

    // id

    @Test
    public void test_2_addStudent_success_validId() throws Exception {
        service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com"));
    }

    @Test
    public void test_3_addStudent_ExceptionThrown_invalidId(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "Real name", 1, "realemail@gmail.com")));
    }

    // name

    @Test
    public void test_2_addStudent_success_validName() throws Exception {
        service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com"));
    }

    @Test
    public void test_3_addStudent_ExceptionThrown_invalidName(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "", 1, "realemail@gmail.com")));
    }

    // mail

    @Test
    public void test_2_addStudent_success_validMail() throws Exception {
        service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com"));
    }

    @Test
    public void test_3_addStudent_ExceptionThrown_invalidMail(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "Real name", -1, "realemail@gmail.com")));
    }

    // Group

    @Test
    public void test_2_addStudent_success_validGroup() throws Exception {
        service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com"));
    }

    @Test
    public void test_3_addStudent_ExceptionThrown_invalidGroup(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "Real name", 1, null)));
    }

    // Prof

    @Test
    public void test_2_addStudent_success_validProf() throws Exception {
        service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com"));
    }

    @Test
    public void test_3_addStudent_ExceptionThrown_invalidProf(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "Real name", 1, "realemail@gmail.com")));
    }


    // Id logic

    @Test
    public void test_2_addStudent_success_IDDoesNotExist() throws Exception {
        service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com"));
    }

    @Test
    public void test_3_addStudent_ExceptionThrown_IdExists() throws Exception {
        service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com"));
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("1", "Real name", 1, "realemail@gmail.com")));

    }

}
