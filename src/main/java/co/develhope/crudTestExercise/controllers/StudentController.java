package co.develhope.crudTestExercise.controllers;

import co.develhope.crudTestExercise.entities.Student;
import co.develhope.crudTestExercise.repositories.StudentRepository;
import co.develhope.crudTestExercise.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;


    @PostMapping("/create")
    public @ResponseBody Student createStudent(@RequestBody Student student){
        return studentRepository.save(student);
    }

    @GetMapping("")
    public List<Student> studentList(){
    return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Student> searchById(@PathVariable Integer id){
        return studentRepository.findById(id);
    }

    @PutMapping("/{id}")
    public Student changeNameSurname(@PathVariable Integer id, @RequestBody Student studentDetail ){

        Optional<Student> searchStudent = studentRepository.findById(id);
        if(!searchStudent.isPresent()){
            return null;
        } else {
            Student updateStudent = studentRepository.saveAndFlush(studentDetail);
            return updateStudent;
        }
    }

    @PutMapping("/working/{id}/activation")
    public @ResponseBody Student studentWorking (@PathVariable Integer id, @RequestParam("activated") Boolean working){
        return studentService.changeWorkingStatus(id, working);
    }


    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id){
        studentRepository.deleteById(id);
    }

}

