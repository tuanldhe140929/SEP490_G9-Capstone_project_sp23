package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.ViolationTypeRepository;
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ViolationTypeRepositoryTest {

	@Autowired
	ViolationTypeRepository vioTypeRepo;
	
	@Autowired
	List<Report> listreport = new List<Report>();
	
	@Test
	void testFindById() {
		ViolationType viotype = new ViolationType();
		viotype.setId(1L);
		viotype.setName("Spam");
		ViolationType result = vioTypeRepo.findById(1L);
		assertEquals(viotype, result);
	}
	
//	mỗi thằng excel lỗi>= 100
//	mỗi thằng logissue log cả testcase cả requirement 
//  nghĩ xem phải làm gì
//  rủi ro dự án
//  kiểm tra chính tả, câu viết, kí hiệu hình, đánh số
//  network diagram, critical part, state diagram
//	kịch bản demo
//  test data. expected output
	
//	@Test
//	void testFindAll() {
//		ViolationType vio1 = new ViolationType(1, "Horror", listreport);
//		ViolationType vio2 = new ViolationType(2, "Adventure", listreport);
//		ViolationType vio3 = new ViolationType(3, "Action", listreport);
//		vioTypeRepo.save(vio1);
//		vioTypeRepo.save(vio2);
//		vioTypeRepo.save(vio3);
//		List<ViolationType> expected = vioTypeRepo.findAll();
//		assertThat(expected.size()).isEqualTo(3);
//	}
	
}
