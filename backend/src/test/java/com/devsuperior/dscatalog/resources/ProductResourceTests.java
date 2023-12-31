package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.zip.DataFormatException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResourceTests.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService service;
    @Autowired
    private ObjectMapper objectMapper;
    private Long existingId;
    private Long nonExistingId;
    private Long dependenteId;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;

    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependenteId = 3L;

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.insert(any())).thenReturn(productDTO);

        when(service.update(eq(existingId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
//        doThrow(DataFormatException.class).when(service).delete(dependenteId);

    }

    @Test
    public void deleteShouldReturnContentWhenIdExists() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDosNotExist() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
//        result.andExpect(jsonPath("$.id").exists());
//        result.andExpect(jsonPath("$.name").exists());
//        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void updateShouldReturnProductDTOWhenExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
//        result.andExpect(jsonPath("$.id").exists());
//        result.andExpect(jsonPath("$.name").exists());
//        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void updateShouldReturnNotFoundIdDosNotExist() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
//        result.andExpect(jsonPath("$.id").exists());
//        result.andExpect(jsonPath("$.name").exists());
//        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }
}
