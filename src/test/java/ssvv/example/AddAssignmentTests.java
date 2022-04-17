package ssvv.example;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.service.Service;
import ssvv.example.validation.*;

import static org.junit.Assert.*;

public class AddAssignmentTests {

    private String validAssignmentId = "testId", validAssignmentDescription = "validDescription";
    private String invalidAssignmentId = null, invalidAssignmentDescription = null;
    private int validAssignmentStartWeek = 2, validAssignmentEndWeek = 4;
    private int invalidAssignmentStartWeek = 132465, invalidAssignmentEndWeek = 123456;

    private StudentValidator studentValidator = null;
    private TemaValidator temaValidator = null;
    private NotaValidator notaValidator = null;
    private StudentXMLRepo studentXMLRepository = null;
    private TemaXMLRepo temaXMLRepository = null;
    private NotaXMLRepo notaXMLRepository = null;

    private Service service = null;

    @Before
    public void doBeforeEach() {
        this.studentValidator = new StudentValidator();
        this.temaValidator = new TemaValidator();
        this.notaValidator = new NotaValidator(this.studentXMLRepository,this.temaXMLRepository);

        this.studentXMLRepository = new StudentXMLRepo("./fisiere_test/Studenti.xml");
        this.temaXMLRepository = new TemaXMLRepo("./fisiere_test/Teme.xml");
        this.notaXMLRepository = new NotaXMLRepo("./fisiere_test/Note.xml");

        this.service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    public void addAssignment_nullAssignmentID(){
        try {
            service.addTema(new Tema(null, validAssignmentDescription, validAssignmentStartWeek, validAssignmentEndWeek));
            fail("fail");
        }
        catch (Exception e) {
            assertEquals("Numar tema invalid!",e.getMessage());
        }
    }

    @Test
    public void addAssignment_emptyAssignmentID(){
        try {
            service.addTema(new Tema("",validAssignmentDescription,validAssignmentEndWeek,validAssignmentStartWeek));
            fail("Assignment addition with empty id should not be possible");
        }
        catch (Exception e) {
            assertEquals("Numar tema invalid!",e.getMessage());
        }
    }

    @Test
    public void addAssignment_nullDescription()
    {
        try {
            service.addTema(new Tema(validAssignmentId,null,validAssignmentEndWeek,validAssignmentStartWeek));
            fail("Assignment addition with null id should not be possible");
        }
        catch (Exception e) {
            assertEquals("Descriere invalida!",e.getMessage());
        }
    }

    @Test
    public void addAssignment_emptyDescription()
    {
        try {
            service.addTema(new Tema(validAssignmentId,"",validAssignmentEndWeek,validAssignmentStartWeek));
            fail("Descriere invalida!");
        }
        catch (Exception e) {
            assertEquals("Descriere invalida!",e.getMessage());
        }
    }

    @Test
    public void addAssignment_StartWeekNegative(){
        try {
            service.addTema(new Tema(validAssignmentId,validAssignmentDescription,validAssignmentEndWeek,-35));
            fail("fail");
        }
        catch (Exception e) {
            assertEquals("Saptamana primirii trebuie sa fie intre 1-14.",e.getMessage());
        }
    }

    @Test
    public void addAssignment_StartWeekGreatNumber(){
        try {
            service.addTema(new Tema(validAssignmentId,validAssignmentDescription,validAssignmentEndWeek,10000000));
            fail("fail");
        }
        catch (Exception e) {
            assertEquals("Saptamana primirii trebuie sa fie intre 1-14.",e.getMessage());
        }
    }

    @Test
    public void addAssignment_EndWeekNegative(){
        try {
            service.addTema(new Tema(validAssignmentId,validAssignmentDescription,-35,validAssignmentStartWeek));
            fail("fail");
        }
        catch (Exception e) {
            assertEquals("Deadlineul trebuie sa fie intre 1-14.",e.getMessage());
        }
    }

    @Test
    public void addAssignment_EndWeekGreatNumber(){
        try {
            service.addTema(new Tema(validAssignmentId,validAssignmentDescription,10000,validAssignmentStartWeek));
            fail("fail");
        }
        catch (Exception e) {
            assertEquals("Deadlineul trebuie sa fie intre 1-14.",e.getMessage());
        }
    }

    @Test
    public void addAssignment_ValidData_StartWeekGreaterThanEndWeek(){
        try {
            service.addTema(new Tema(validAssignmentId,validAssignmentDescription,2,4));
            fail("fail");
        }
        catch (Exception e) {
            assertEquals("Data de primire invalida!",e.getMessage());
        }
    }

    @Test
    public void addAssignment_ValidData(){
        Tema addedTema = new Tema(validAssignmentId,validAssignmentDescription,validAssignmentEndWeek,validAssignmentStartWeek);
        this.service.addTema(addedTema);
        Iterable<Tema> teme = this.service.getAllTeme();
        boolean temaWasAdded = true;
        for (Tema t : teme)
            if (t.equals(addedTema)) {
                temaWasAdded = true;
            }
        Assert.assertTrue(temaWasAdded);
        this.service.deleteTema(validAssignmentId);
    }

    @Test
    public void addAssignment_SameAssignmentTwice() {
        try {
            this.service.addTema(new Tema(validAssignmentId,validAssignmentDescription,validAssignmentEndWeek,validAssignmentStartWeek));
            this.service.addTema(new Tema(validAssignmentId,validAssignmentDescription,validAssignmentEndWeek,validAssignmentStartWeek));
            fail("already existent");
        } catch (ValidationException e) {
            assertEquals("Not unique assignments should not be added!", e.getMessage());
        }
        this.service.deleteTema(validAssignmentId);
    }
}
