package co.develhope.crudTestExercise.services;

import co.develhope.crudTestExercise.entities.Student;
import co.develhope.crudTestExercise.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student changeWorkingStatus(Integer id, Boolean isWorking) {
        Optional<Student> student = studentRepository.findById(id);
        if (!student.isPresent())
            return null;
        student.get().setWorking(isWorking);
        return studentRepository.save(student.get());
    }
}