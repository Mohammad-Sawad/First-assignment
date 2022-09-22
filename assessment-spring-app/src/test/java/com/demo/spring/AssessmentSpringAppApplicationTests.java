package com.demo.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.spring.entity.Product;
import com.demo.spring.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AssessmentSpringAppApplicationTests {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MockMvc mvc;


	
	@Test
	public void testfindOneProduct() throws Exception {
		mvc.perform(get("/products/find/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.productName").value("iPhone12"));
	}

	@Test
	public void testfindOneProductFailure() throws Exception {
		mvc.perform(get("/products/find/6")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.status").value("product not found"));
	}


	@Test
	public void testSaveProductFailure() throws Exception {
		Product product = new Product(1, "USB Cable", 1200.0);
		ObjectMapper mapper = new ObjectMapper();
		String productJson = mapper.writeValueAsString(product);
		mvc.perform(post("/products/save").content(productJson).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.status").value("product exists"));
	}

	@Test
	public void testSaveProduct() throws Exception {
		Product product = new Product(6, "Usb Cable", 1200.0);
		ObjectMapper mapper = new ObjectMapper();
		String productJson = mapper.writeValueAsString(product);
		mvc.perform(post("/products/save").content(productJson).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
	}

	@Test
	public void testupdateProduct() throws Exception {
		Product product = new Product(1, "Usb Cable", 1200.0);
		ObjectMapper mapper = new ObjectMapper();
		String productJson = mapper.writeValueAsString(product);

		mvc.perform(put("/products/update").content(productJson).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.status").value("product updated"));
	}

	@Test
	public void testupdateProductFailure() throws Exception {
		Product product = new Product(10, "Usb Cable", 1200.0);
		ObjectMapper mapper = new ObjectMapper();
		String productJson = mapper.writeValueAsString(product);

		mvc.perform(put("/products/update").content(productJson).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.status").value("product not found"));
	}

	@Test
	public void testDeleteProduct() throws Exception {
		mvc.perform(delete("/products/remove/3")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.status").value("product deleted"));
	}

	@Test
	public void testDeleteProductFailure() throws Exception {
		mvc.perform(delete("/products/remove/8")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.status").value("product not found"));
	}

	@Test
	public void testUpdatePrice() throws Exception {
		mvc.perform(patch("/products/update/3/600")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.status").value("price updated"));
	}

	@Test
	public void testUpdatePriceFailure() throws Exception {
		mvc.perform(patch("/products/update/1050/700")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.status").value("product not found"));

	}

//	@Test
//	public void testListAllProducts() {
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Accept", "application/json");
//		HttpEntity<Void> req = new HttpEntity<>(headers);
//		ResponseEntity<List<Product>> productList = template.exchange("http://localhost:" + port + "/products/",
//				HttpMethod.GET, req, new ParameterizedTypeReference<List<Product>>() {
//				});
//		Assertions.assertTrue(productList.getBody().size() > 0);
//		Assertions.assertTrue(productList.getStatusCode().is2xxSuccessful());
//		Assertions.assertTrue(productList.getHeaders().getContentType().equals(MediaType.APPLICATION_JSON));
//	}

}
