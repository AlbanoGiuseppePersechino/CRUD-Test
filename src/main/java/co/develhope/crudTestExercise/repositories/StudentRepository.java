package co.develhope.crudTestExercise.repositories;

import co.develhope.crudTestExercise.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}