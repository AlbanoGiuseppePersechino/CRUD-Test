package co.develhope.crudTestExercise;

import co.develhope.crudTestExercise.controllers.StudentController;
import co.develhope.crudTestExercise.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@Nested
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class CrudTestExerciseApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	StudentController studentController;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
		assertThat(studentController).isNotNull();
	}

	private Student createStudent () throws Exception {
		Student student = new Student();
		student.setWorking(true);
		student.setName("Albano");
		student.setSurname("Persechino");

		return createStudent(student);
	}

	private Student createStudent(Student student) throws Exception {
		MvcResult result = createStudentRequest(student);
		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		return studentFromResponse;
	}

	private MvcResult createStudentRequest(Student student) throws Exception{
		if(student == null) return null;

		String studentJson = objectMapper.writeValueAsString(student);

		return this.mockMvc.perform(post("/student/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	private Student getStudentFromId(Integer id) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/student/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		try {
			String userJSON = result.getResponse().getContentAsString();
			return objectMapper.readValue(userJSON, Student.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Test
	@Order(1)
	void createStudentTest() throws Exception {
		Student studentFromResponse = createStudent();
		assertThat(studentFromResponse.getId()).isNotNull();
	}


	@Test
	@Order(2)
	void getStudentListTest() throws Exception{

		MvcResult result = this.mockMvc.perform(get("/student"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		System.out.println("Student in database is: " + studentFromResponse.size());
		createStudent();
		assertThat(studentFromResponse.size()).isNotZero();
	}

	@Test
	@Order(3)
	void readSingleStudent() throws Exception {
		Student student = createStudent();

		assertThat(student.getId()).isNotNull();

		MvcResult result = this.mockMvc.perform(get("/student/" + student.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
	}

	@Test
	@Order(4)
	void updateStudent() throws Exception {
		Student student = createStudent();

		assertThat(student.getId()).isNotNull();

		String newName = "Luca";
		student.setName(newName);
		String studentJSON = objectMapper.writeValueAsString(student);

		MvcResult result = this.mockMvc.perform(put("/student/" + student.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getName()).isEqualTo(newName);
	}

	@Test
	@Order(5)
	void deleteStudent() throws Exception {
		Student student = createStudent();

		assertThat(student.getId()).isNotNull();

		this.mockMvc.perform(delete("/student/" + student.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = getStudentFromId(student.getId());
		assertThat(studentFromResponse).isNull();
	}

	@Test
	@Order(6)
	void workingStudent() throws Exception {
		Student student = createStudent();

		assertThat(student.getId()).isNotNull();

		MvcResult result = this.mockMvc.perform(put("/student/working/" + student.getId() + "/activation?activated=true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentFromResponse).isNotNull();
		assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponse.getWorking()).isEqualTo(true);

		Student studentFromResponseGet = getStudentFromId(student.getId());
		assertThat(studentFromResponseGet).isNotNull();
		assert studentFromResponseGet != null;
		assertThat(studentFromResponseGet.getId()).isEqualTo(student.getId());
		assertThat(studentFromResponseGet.getWorking()).isEqualTo(true);
	}
}
