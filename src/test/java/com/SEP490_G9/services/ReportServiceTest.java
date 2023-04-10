package com.SEP490_G9.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.ReportRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.service.ReportService;
import com.SEP490_G9.service.authService.authServiceImpl.ReportServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ReportServiceTest {

	@InjectMocks
	ReportServiceImpl reportService;
	
	@Mock
	ProductRepository productRepo;
	
	@Mock
	ProductDetailsRepository productDetailsRepo;
	
	@Mock
	UserRepository userRepo;
	
	@Mock
	ViolationTypeRepository vioTypeRepo;
	
	@Mock
	ReportRepository reportRepo;
	
	@Test
	public void testSendReport() {
		Product product = new Product();
		product.setId(1L);
		
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		
		User user = new User();
		user.setId(1L);
		
		ViolationType vt = new ViolationType();
		vt.setId(1L);
		vt.setName("violation 1");
		
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(product);
		
		List<User> allUsers = new ArrayList<>();
		allUsers.add(user);
		
		List<ViolationType> vioTypes = new ArrayList<>();
		vioTypes.add(vt);
		
		when(productRepo.findAll()).thenReturn(allProducts);
		when(userRepo.findAll()).thenReturn(allUsers);
		when(vioTypeRepo.findAll()).thenReturn(vioTypes);
		
		Report report = new Report();
		report.setProduct(product);
		report.setUser(user);
		report.setVersion("1.0.0");
		report.setDescription("      Sản phẩm     này đã     vi    phạm     ");
		report.setViolation_types(vt);
		
		
		Report actual = reportService.sendReport(report.getProduct().getId(), report.getUser().getId(), report.getVersion(),report.getDescription(), report.getViolation_types().getId());
		assertEquals(report, actual);
	}
	
	@Test
	public void testGetByProductAllVersion1() {
		Product p1 = new Product();
		p1.setId(1L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setVersion("1.0.0");
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);
		pd2.setVersion("1.0.1");
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);
		pd3.setVersion("1.0.0");
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);
		pd4.setVersion("1.0.3");
		
		Product p3 = new Product();
		p3.setId(3L);
		
		ProductDetails pd5 = new ProductDetails();
		pd5.setProduct(p3);
		pd5.setVersion("1.0.0");
		
		User u1 = new User();
		u1.setId(1L);
		
		User u2 = new User();
		u2.setId(2L);
		
		User u3 = new User();
		u3.setId(3L);
		
		ViolationType vt1 = new ViolationType();
		vt1.setId(1L);
		vt1.setName("vio1");
		
		Report r1 = new Report();
		r1.setProduct(p1);
		r1.setUser(u1);
		r1.setVersion(pd1.getVersion());
		r1.setStatus("DENIED");
		r1.setDescription("ajsidojsaid");
		r1.setViolation_types(vt1);
		
		Report r2 = new Report();
		r2.setProduct(p1);
		r2.setUser(u1);
		r2.setVersion(pd2.getVersion());
		r2.setStatus("PENDING");
		r2.setDescription("ajsidojsaid");
		r2.setViolation_types(vt1);
		
		Report r3 = new Report();
		r3.setProduct(p1);
		r3.setUser(u2);
		r3.setVersion(pd2.getVersion());
		r3.setStatus("PENDING");
		r3.setDescription("ajsidojsaid");
		r3.setViolation_types(vt1);
		
		Report r4 = new Report();
		r4.setProduct(p2);
		r4.setUser(u2);
		r4.setVersion(pd4.getVersion());
		r4.setStatus("ACCEPTED");
		r4.setDescription("ajsidojsaid");
		r4.setViolation_types(vt1);
		
		Report r5 = new Report();
		r5.setProduct(p3);
		r5.setUser(u3);
		r5.setVersion(pd4.getVersion());
		r5.setStatus("DENIED");
		r5.setDescription("ajsidojsaid");
		r5.setViolation_types(vt1);
		
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(p1);
		allProducts.add(p2);
		allProducts.add(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		allPd.add(pd5);
		
		List<User> allUsers = new ArrayList<>();
		allUsers.add(u1);
		allUsers.add(u2);
		allUsers.add(u3);
		
		List<ViolationType> vioTypes = new ArrayList<>();
		vioTypes.add(vt1);
		
		when(productRepo.findAll()).thenReturn(allProducts);
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		when(userRepo.findAll()).thenReturn(allUsers);
		when(vioTypeRepo.findAll()).thenReturn(vioTypes);
		
		
		List<Report> allReports = new ArrayList<>();
		allReports.add(r1);
		allReports.add(r2);
		allReports.add(r3);
		allReports.add(r4);
		allReports.add(r5);
		
		when(reportRepo.findAll()).thenReturn(allReports);
		
		List<Report> expected = new ArrayList<>();
		expected.add(r2);
		expected.add(r3);
		
		List<Report> actual = reportService.getByProductAllVersions(p1.getId(), "PENDING");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetByProductAllVersion2() {
		Product p1 = new Product();
		p1.setId(1L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setVersion("1.0.0");
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);
		pd2.setVersion("1.0.1");
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);
		pd3.setVersion("1.0.0");
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);
		pd4.setVersion("1.0.3");
		
		Product p3 = new Product();
		p3.setId(3L);
		
		ProductDetails pd5 = new ProductDetails();
		pd5.setProduct(p3);
		pd5.setVersion("1.0.0");
		
		User u1 = new User();
		u1.setId(1L);
		
		User u2 = new User();
		u2.setId(2L);
		
		User u3 = new User();
		u3.setId(3L);
		
		ViolationType vt1 = new ViolationType();
		vt1.setId(1L);
		vt1.setName("vio1");
		
		Report r1 = new Report();
		r1.setProduct(p1);
		r1.setUser(u1);
		r1.setVersion(pd1.getVersion());
		r1.setStatus("DENIED");
		r1.setDescription("ajsidojsaid");
		r1.setViolation_types(vt1);
		
		Report r2 = new Report();
		r2.setProduct(p1);
		r2.setUser(u1);
		r2.setVersion(pd2.getVersion());
		r2.setStatus("PENDING");
		r2.setDescription("ajsidojsaid");
		r2.setViolation_types(vt1);
		
		Report r3 = new Report();
		r3.setProduct(p1);
		r3.setUser(u2);
		r3.setVersion(pd2.getVersion());
		r3.setStatus("PENDING");
		r3.setDescription("ajsidojsaid");
		r3.setViolation_types(vt1);
		
		Report r4 = new Report();
		r4.setProduct(p2);
		r4.setUser(u2);
		r4.setVersion(pd4.getVersion());
		r4.setStatus("ACCEPTED");
		r4.setDescription("ajsidojsaid");
		r4.setViolation_types(vt1);
		
		Report r5 = new Report();
		r5.setProduct(p3);
		r5.setUser(u3);
		r5.setVersion(pd4.getVersion());
		r5.setStatus("DENIED");
		r5.setDescription("ajsidojsaid");
		r5.setViolation_types(vt1);
		
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(p1);
		allProducts.add(p2);
		allProducts.add(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		allPd.add(pd5);
		
		List<User> allUsers = new ArrayList<>();
		allUsers.add(u1);
		allUsers.add(u2);
		allUsers.add(u3);
		
		List<ViolationType> vioTypes = new ArrayList<>();
		vioTypes.add(vt1);
		
		when(productRepo.findAll()).thenReturn(allProducts);
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		when(userRepo.findAll()).thenReturn(allUsers);
		when(vioTypeRepo.findAll()).thenReturn(vioTypes);
		
		
		List<Report> allReports = new ArrayList<>();
		allReports.add(r1);
		allReports.add(r2);
		allReports.add(r3);
		allReports.add(r4);
		allReports.add(r5);
		
		when(reportRepo.findAll()).thenReturn(allReports);
		
		List<Report> expected = new ArrayList<>();
		
		List<Report> actual = reportService.getByProductAllVersions(p2.getId(), "PENDING");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetByProductAllVersion3() {
		Product p1 = new Product();
		p1.setId(1L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setVersion("1.0.0");
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);
		pd2.setVersion("1.0.1");
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);
		pd3.setVersion("1.0.0");
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);
		pd4.setVersion("1.0.3");
		
		Product p3 = new Product();
		p3.setId(3L);
		
		ProductDetails pd5 = new ProductDetails();
		pd5.setProduct(p3);
		pd5.setVersion("1.0.0");
		
		User u1 = new User();
		u1.setId(1L);
		
		User u2 = new User();
		u2.setId(2L);
		
		User u3 = new User();
		u3.setId(3L);
		
		ViolationType vt1 = new ViolationType();
		vt1.setId(1L);
		vt1.setName("vio1");
		
		Report r1 = new Report();
		r1.setProduct(p1);
		r1.setUser(u1);
		r1.setVersion(pd1.getVersion());
		r1.setStatus("DENIED");
		r1.setDescription("ajsidojsaid");
		r1.setViolation_types(vt1);
		
		Report r2 = new Report();
		r2.setProduct(p1);
		r2.setUser(u1);
		r2.setVersion(pd2.getVersion());
		r2.setStatus("PENDING");
		r2.setDescription("ajsidojsaid");
		r2.setViolation_types(vt1);
		
		Report r3 = new Report();
		r3.setProduct(p1);
		r3.setUser(u2);
		r3.setVersion(pd2.getVersion());
		r3.setStatus("PENDING");
		r3.setDescription("ajsidojsaid");
		r3.setViolation_types(vt1);
		
		Report r4 = new Report();
		r4.setProduct(p2);
		r4.setUser(u2);
		r4.setVersion(pd4.getVersion());
		r4.setStatus("ACCEPTED");
		r4.setDescription("ajsidojsaid");
		r4.setViolation_types(vt1);
		
		Report r5 = new Report();
		r5.setProduct(p3);
		r5.setUser(u3);
		r5.setVersion(pd4.getVersion());
		r5.setStatus("DENIED");
		r5.setDescription("ajsidojsaid");
		r5.setViolation_types(vt1);
		
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(p1);
		allProducts.add(p2);
		allProducts.add(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		allPd.add(pd5);
		
		List<User> allUsers = new ArrayList<>();
		allUsers.add(u1);
		allUsers.add(u2);
		allUsers.add(u3);
		
		List<ViolationType> vioTypes = new ArrayList<>();
		vioTypes.add(vt1);
		
		when(productRepo.findAll()).thenReturn(allProducts);
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		when(userRepo.findAll()).thenReturn(allUsers);
		when(vioTypeRepo.findAll()).thenReturn(vioTypes);
		
		
		List<Report> allReports = new ArrayList<>();
		allReports.add(r1);
		allReports.add(r2);
		allReports.add(r3);
		allReports.add(r4);
		allReports.add(r5);
		
		when(reportRepo.findAll()).thenReturn(allReports);
		
		List<Report> expected = new ArrayList<>();
		expected.add(r5);
		
		List<Report> actual = reportService.getByProductAllVersions(p3.getId(), "HANDLED");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetByProductAllVersion4() {
		Product p1 = new Product();
		p1.setId(1L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setVersion("1.0.0");
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setVersion("1.0.0");
		
		Product p3 = new Product();
		p3.setId(3L);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setVersion("1.0.0");
		
		User u1 = new User();
		u1.setId(1L);
		
		User u2 = new User();
		u2.setId(2L);
		
		User u3 = new User();
		u3.setId(3L);
		
		ViolationType vt1 = new ViolationType();
		vt1.setId(1L);
		vt1.setName("vio1");
		
		Report r1 = new Report();
		r1.setProduct(p1);
		r1.setUser(u1);
		r1.setVersion(pd1.getVersion());
		r1.setStatus("PENDING");
		r1.setDescription("ajsidojsaid");
		r1.setViolation_types(vt1);
		
		Report r2 = new Report();
		r2.setProduct(p2);
		r2.setUser(u2);
		r2.setVersion(pd2.getVersion());
		r2.setStatus("ACCEPTED");
		r2.setDescription("ajsidojsaid");
		r2.setViolation_types(vt1);
		
		Report r3 = new Report();
		r3.setProduct(p3);
		r3.setUser(u3);
		r3.setVersion(pd2.getVersion());
		r3.setStatus("DENIED");
		r3.setDescription("ajsidojsaid");
		r3.setViolation_types(vt1);
		
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(p1);
		allProducts.add(p2);
		allProducts.add(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		
		List<User> allUsers = new ArrayList<>();
		allUsers.add(u1);
		allUsers.add(u2);
		allUsers.add(u3);
		
		List<ViolationType> vioTypes = new ArrayList<>();
		vioTypes.add(vt1);
		
		when(productRepo.findAll()).thenReturn(allProducts);
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		when(userRepo.findAll()).thenReturn(allUsers);
		when(vioTypeRepo.findAll()).thenReturn(vioTypes);
		
		
		List<Report> allReports = new ArrayList<>();
		allReports.add(r1);
		allReports.add(r2);
		allReports.add(r3);
		
		when(reportRepo.findAll()).thenReturn(allReports);
		
		List<Report> expected = new ArrayList<>();
		expected.add(r1);
		
		List<Report> actual = reportService.getByProductAllVersions(p1.getId(), "PENDING");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetByProductAllVersion5() {
		Product p1 = new Product();
		p1.setId(1L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setVersion("1.0.0");
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setVersion("1.0.0");
		
		Product p3 = new Product();
		p3.setId(3L);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setVersion("1.0.0");
		
		User u1 = new User();
		u1.setId(1L);
		
		User u2 = new User();
		u2.setId(2L);
		
		User u3 = new User();
		u3.setId(3L);
		
		ViolationType vt1 = new ViolationType();
		vt1.setId(1L);
		vt1.setName("vio1");
		
		Report r1 = new Report();
		r1.setProduct(p1);
		r1.setUser(u1);
		r1.setVersion(pd1.getVersion());
		r1.setStatus("PENDING");
		r1.setDescription("ajsidojsaid");
		r1.setViolation_types(vt1);
		
		Report r2 = new Report();
		r2.setProduct(p2);
		r2.setUser(u2);
		r2.setVersion(pd2.getVersion());
		r2.setStatus("ACCEPTED");
		r2.setDescription("ajsidojsaid");
		r2.setViolation_types(vt1);
		
		Report r3 = new Report();
		r3.setProduct(p3);
		r3.setUser(u3);
		r3.setVersion(pd2.getVersion());
		r3.setStatus("DENIED");
		r3.setDescription("ajsidojsaid");
		r3.setViolation_types(vt1);
		
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(p1);
		allProducts.add(p2);
		allProducts.add(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		
		List<User> allUsers = new ArrayList<>();
		allUsers.add(u1);
		allUsers.add(u2);
		allUsers.add(u3);
		
		List<ViolationType> vioTypes = new ArrayList<>();
		vioTypes.add(vt1);
		
		when(productRepo.findAll()).thenReturn(allProducts);
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		when(userRepo.findAll()).thenReturn(allUsers);
		when(vioTypeRepo.findAll()).thenReturn(vioTypes);
		
		
		List<Report> allReports = new ArrayList<>();
		allReports.add(r1);
		allReports.add(r2);
		allReports.add(r3);
		
		when(reportRepo.findAll()).thenReturn(allReports);
		
		List<Report> expected = new ArrayList<>();
		expected.add(r2);
		
		List<Report> actual = reportService.getByProductAllVersions(p2.getId(), "HANDLED");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetByProductAllVersion6() {
		Product p1 = new Product();
		p1.setId(1L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setVersion("1.0.0");
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setVersion("1.0.0");
		
		Product p3 = new Product();
		p3.setId(3L);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setVersion("1.0.0");
		
		User u1 = new User();
		u1.setId(1L);
		
		User u2 = new User();
		u2.setId(2L);
		
		User u3 = new User();
		u3.setId(3L);
		
		ViolationType vt1 = new ViolationType();
		vt1.setId(1L);
		vt1.setName("vio1");
		
		Report r1 = new Report();
		r1.setProduct(p1);
		r1.setUser(u1);
		r1.setVersion(pd1.getVersion());
		r1.setStatus("PENDING");
		r1.setDescription("ajsidojsaid");
		r1.setViolation_types(vt1);
		
		Report r2 = new Report();
		r2.setProduct(p2);
		r2.setUser(u2);
		r2.setVersion(pd2.getVersion());
		r2.setStatus("ACCEPTED");
		r2.setDescription("ajsidojsaid");
		r2.setViolation_types(vt1);
		
		Report r3 = new Report();
		r3.setProduct(p3);
		r3.setUser(u3);
		r3.setVersion(pd2.getVersion());
		r3.setStatus("DENIED");
		r3.setDescription("ajsidojsaid");
		r3.setViolation_types(vt1);
		
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(p1);
		allProducts.add(p2);
		allProducts.add(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		
		List<User> allUsers = new ArrayList<>();
		allUsers.add(u1);
		allUsers.add(u2);
		allUsers.add(u3);
		
		List<ViolationType> vioTypes = new ArrayList<>();
		vioTypes.add(vt1);
		
		when(productRepo.findAll()).thenReturn(allProducts);
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		when(userRepo.findAll()).thenReturn(allUsers);
		when(vioTypeRepo.findAll()).thenReturn(vioTypes);
		
		
		List<Report> allReports = new ArrayList<>();
		allReports.add(r1);
		allReports.add(r2);
		allReports.add(r3);
		
		when(reportRepo.findAll()).thenReturn(allReports);
		
		List<Report> expected = new ArrayList<>();
		expected.add(r3);
		
		List<Report> actual = reportService.getByProductAllVersions(p3.getId(), "HANDLED");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetByProductAllVersion7() {
		Product p1 = new Product();
		p1.setId(1L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setVersion("1.0.0");
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setVersion("1.0.0");
		
		Product p3 = new Product();
		p3.setId(3L);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setVersion("1.0.0");
		
		User u1 = new User();
		u1.setId(1L);
		
		User u2 = new User();
		u2.setId(2L);
		
		User u3 = new User();
		u3.setId(3L);
		
		ViolationType vt1 = new ViolationType();
		vt1.setId(1L);
		vt1.setName("vio1");
		
		
		List<Product> allProducts = new ArrayList<>();
		allProducts.add(p1);
		allProducts.add(p2);
		allProducts.add(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		
		List<User> allUsers = new ArrayList<>();
		allUsers.add(u1);
		allUsers.add(u2);
		allUsers.add(u3);
		
		List<ViolationType> vioTypes = new ArrayList<>();
		vioTypes.add(vt1);
		
		when(productRepo.findAll()).thenReturn(allProducts);
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		when(userRepo.findAll()).thenReturn(allUsers);
		when(vioTypeRepo.findAll()).thenReturn(vioTypes);
		
		
		List<Report> allReports = new ArrayList<>();
		
		when(reportRepo.findAll()).thenReturn(allReports);
		
		List<Report> expected = new ArrayList<>();
		
		List<Report> actual = reportService.getByProductAllVersions(p1.getId(), "PENDING");
		assertEquals(expected, actual);
	}
}
