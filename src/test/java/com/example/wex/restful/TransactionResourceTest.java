package com.example.wex.restful;

import com.example.wex.entity.Transactions;
import com.example.wex.exceptions.BusinessException;
import com.example.wex.repository.TransactionRepository;
import com.example.wex.service.TransactionService;
import com.example.wex.utils.JSONUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionResourceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TransactionResouce transactionResouce;

    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void before() throws BusinessException {
        var map = new HashMap<String, Object>();
        map.put("Description", "FamilyBarbecue");
        map.put("PurchaseAmount", "78.56");
        map.put("DestinyKey", 777); //test one
        transactionRepository.save(transactionService.createEntity(map));
    }

    @AfterEach
    @Transactional
    void after() {
        List<Transactions> transaction = transactionRepository.findByDestinyKeyOrderByTransactionDateDesc(777L, PageRequest.of(0,1));
        transactionRepository.delete(transaction.get(0));
    }

    @Test
    void testFind() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.
                        get("/v1/transactions/findByDestinyKey")
                        .param("destinyKey", String.valueOf(777))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void testSave() throws Exception {
        var map = new HashMap<String, Object>();
        map.put("Description", "Graphic Card RTX");
        map.put("PurchaseAmount", "236.82");
        map.put("DestinyKey", 775);
        this.mockMvc.perform(post("/v1/transactions/save")
                        .content(JSONUtils.objectToJson(map))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void testSaveDescriptionError() throws Exception {
        var map = new HashMap<String, Object>();
        map.put("Description", "cnwuifenfn ewounfu uo  FHAPCFHqeuhfpuphquPPHUHdqdwhpuoqHUCXNUQIBDUHQEWNUPOXNDWQNCUODQBPWUDBQUObdn");
        map.put("PurchaseAmount", "78.56");
        map.put("DestinyKey", 775);

            this.mockMvc.perform(post("/v1/transactions/save")
                            .content(JSONUtils.objectToJson(map))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().is4xxClientError()).andReturn();

    }

    @Test
    void testUpdate() throws Exception {
        List<Transactions> transaction = transactionRepository.findByDestinyKeyOrderByTransactionDateDesc(777L, PageRequest.of(0,1));
        var map = new HashMap<String, Object>();
        map.put("Description", "More Food For FamilyBarbecue");
        map.put("PurchaseAmount", "15");
        map.put("purchaseKey", transaction.get(0).getPurchaseKey());//the test one the exists persisted
        this.mockMvc.perform(put("/v1/transactions/update")
                        .content(JSONUtils.objectToJson(map))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseAmount").value("15.0 U$$")).andReturn();

    }

    @Test
    void testUpdateDestinyKeyError() throws Exception {
        List<Transactions> transaction = transactionRepository.findByDestinyKeyOrderByTransactionDateDesc(777L, PageRequest.of(0,1));
        var map = new HashMap<String, Object>();
        map.put("Description", "More Food For FamilyBarbecue");
        map.put("PurchaseAmount", "78.56");
        map.put("DestinyKey", 775);
        map.put("purchaseKey", transaction.get(0).getPurchaseKey());//the test one the exists persisted
        this.mockMvc.perform(put("/v1/transactions/update")
                        .content(JSONUtils.objectToJson(map))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError()).andReturn();

    }

    @Test
    void testDelete() throws Exception {
        List<Transactions> transaction = transactionRepository.findByDestinyKeyOrderByTransactionDateDesc(775L, PageRequest.of(0,1));
        this.mockMvc.perform(delete("/v1/transactions/delete")
                        .param("key", String.valueOf(transaction.get(0).getPurchaseKey()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }


}
