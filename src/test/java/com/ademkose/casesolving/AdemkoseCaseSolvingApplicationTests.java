package com.ademkose.casesolving;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ademkose.casesolving.model.Presentation;
import com.ademkose.casesolving.repository.PresentationRepository;
import com.ademkose.casesolving.service.PresentationService;
import com.ademkose.casesolving.util.Utils;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdemkoseCaseSolvingApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private PresentationRepository presentationRepository;
	
	@Autowired
	private PresentationService presentationService;

	private MockMvc mockMvc;

	@Test
	@Order(1)  
	public void healthCheck_IS_SERVER_UP_AND_RUNNING() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/presentation")).andExpect(status().isOk());

	}
	@Test
	@Order(2)  
	public void load_test_data_NOT_INSTALLED() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult result = mockMvc.perform(get("/presentation")).andReturn();;

		String content = result.getResponse().getContentAsString();
		
		assertTrue(content.contains("There is no presentation. Please add presentation."));

	}
	@Test()
	@Order(3)  
	public void no_tracks() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MvcResult result = mockMvc.perform(get("/tracks")).andReturn();;

		String content = result.getResponse().getContentAsString();
		
		assertTrue(content.contains("There is no tracks"));

	}
	@Test
	@Order(4)  
	public void load_test_data_IS_INSTALLED() throws Exception {		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockMvc.perform(get("/load_test_data")).andReturn();
		MvcResult result = mockMvc.perform(get("/presentation")).andReturn();
		String content = result.getResponse().getContentAsString();
		
		assertTrue(content.contains("Architecting Your Codebase"));
		assertTrue(content.contains("Overdoing it in Python"));
		assertTrue(content.contains("Flavors of Concurrency in Java"));
		assertTrue(content.contains("Ruby Errors from Mismatched Gem Versions"));
		assertTrue(content.contains("JUnit 5 - Shaping the Future of Testing on the JVM"));
		assertTrue(content.contains("Cloud Native Java"));
		assertTrue(content.contains("Communicating Over Distance"));
		assertTrue(content.contains("AWS Technical Essentials"));
		assertTrue(content.contains("Continuous Delivery"));
		assertTrue(content.contains("Monitoring Reactive Applications"));
		assertTrue(content.contains("Pair Program g vs Noise"));
		assertTrue(content.contains("Rails Magic"));
		assertTrue(content.contains("Microservices Just Right"));
		assertTrue(content.contains("Clojure Ate Scala (on my project)"));
		assertTrue(content.contains("Perfect Scalability"));
		assertTrue(content.contains("Apache Spark"));
		assertTrue(content.contains("Async Testing on JVM"));
		assertTrue(content.contains("A World Without HackerNews"));
		assertTrue(content.contains("User Interface CSS in Apps"));		
		
		

	}
	
	@Test
	@Order(5)  // this method tests the all presentations in the tracks days?
	public void tracks_cover_all_presentations() throws Exception {		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		
		MvcResult result = mockMvc.perform(get("/tracks")).andReturn();
		String content = result.getResponse().getContentAsString();
		
		assertTrue(content.contains("Architecting Your Codebase"));
		assertTrue(content.contains("Overdoing it in Python"));
		assertTrue(content.contains("Flavors of Concurrency in Java"));
		assertTrue(content.contains("Ruby Errors from Mismatched Gem Versions"));
		assertTrue(content.contains("JUnit 5 - Shaping the Future of Testing on the JVM"));
		assertTrue(content.contains("Cloud Native Java"));
		assertTrue(content.contains("Communicating Over Distance"));
		assertTrue(content.contains("AWS Technical Essentials"));
		assertTrue(content.contains("Continuous Delivery"));
		assertTrue(content.contains("Monitoring Reactive Applications"));
		assertTrue(content.contains("Pair Program g vs Noise"));
		assertTrue(content.contains("Rails Magic"));
		assertTrue(content.contains("Microservices Just Right"));
		assertTrue(content.contains("Clojure Ate Scala (on my project)"));
		assertTrue(content.contains("Perfect Scalability"));
		assertTrue(content.contains("Apache Spark"));
		assertTrue(content.contains("Async Testing on JVM"));
		assertTrue(content.contains("A World Without HackerNews"));
		assertTrue(content.contains("User Interface CSS in Apps"));	
		
		assertTrue(content.contains("LUNCH"));

	}
	
	
	//REPOSITORY TESTING
	@Test
	@Order(6)  
	public void add_new_Presentation_REPOSITORYTESTING() throws Exception {
		Presentation new_test = new Presentation((long)20,"test11","11:00PM",11);
		
		presentationRepository.save(new_test);
		
		assertEquals("test11", presentationRepository.findByName("test11").getName());		

	}
	@Test
	@Order(7)  
	public void find_Presentation_REPOSITORYTESTING() throws Exception {
		Presentation op1 = presentationRepository.findByName("test11");			
		assertEquals("test11",op1.getName() );		

	}
	@Test
	@Order(8)  
	public void update_Presentation_REPOSITORYTESTING() throws Exception {
		Presentation op1 = presentationRepository.findByName("test11");	
		op1.setMinute(60);
		presentationRepository.save(op1);		
		
		Presentation op2 = presentationRepository.findByName("test11");		
		assertEquals(60,op2.getMinute());		

	}
	@Test
	@Order(9)  
	public void delete_Presentation_REPOSITORYTESTING() throws Exception { 
		
		presentationRepository.deleteById((long)20);
		Presentation op1 = presentationRepository.findByName("test11");			
		assertEquals(null,op1 );

	}
	
	@Test
	@Order(10)  
	public void Utils_method_test() throws Exception { 		
		String return_test= Utils.minuteToTimeAM_PM(540,"AM");
		assertEquals("09:00AM",return_test );		
		return_test= Utils.minuteToTimeAM_PM(600,"AM");			
		assertEquals("10:00AM",return_test );		
		return_test= Utils.minuteToTimeAM_PM(935,"PM");			
		assertEquals("15:35PM",return_test );
	}
	
	//SERVICE TESTING
	@Test
	@Order(11)  
	public void findAll_check_size_SERVICE_TESTING() throws Exception {
		List<Presentation> list = presentationService.getAllPresentation();
		
		int list_size= list.size();
		
		assertEquals(19, list_size);		

	}

}
