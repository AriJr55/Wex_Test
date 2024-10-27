package com.example.wex.restful;

import com.example.wex.utils.JSONUtils;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@AutoConfigureMockMvc
@EnableWebMvc
public class TransactionResourceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransactionResouce transactionResouce;

    @Test
    void testSave() throws Exception {
        var map = new HashMap<String, Object>();
        map.put("Description", "FamilyBarbecue");
        map.put("PurchaseAmount", "78.56");
        map.put("DestinyKey", 775);
        this.mockMvc.perform(post("/v1/transactions/save")
                        .content(JSONUtils.objectToJson(map))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void testErrorDescriptionSave() {
        var map = new HashMap<String, Object>();
        map.put("Description", "cnwuifenfn ewounfu uo  FHAPCFHqeuhfpuphquPPHUHdqdwhpuoqHUCXNUQIBDUHQEWNUPOXNDWQNCUODQBPWUDBQUObdn");
        map.put("PurchaseAmount", "78.56");
        map.put("DestinyKey", 775);

        try {
            this.mockMvc.perform(post("/v1/transactions/save")
                            .content(JSONUtils.objectToJson(map))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk()).andReturn();
        }catch (Exception e) {
            Assert.isInstanceOf(Exception.class, e);
        }
    }

    @Test
    void testUpdate() throws Exception {
        var map = new HashMap<String, Object>();
        map.put("Description", "FamilyBarbecue");
        map.put("PurchaseAmount", "78.56");
        map.put("DestinyKey", 775);
        map.put("purchaseKey", "e552a75c-5763-4616-af65-ad5575a3");
        this.mockMvc.perform(put("/v1/transactions/update")
                        .content(JSONUtils.objectToJson(map))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void testUpdateWithoutKey() throws Exception {
        var map = new HashMap<String, Object>();
        map.put("Description", "FamilyBarbecue");
        map.put("PurchaseAmount", "78.56");
        map.put("DestinyKey", 775);
        MvcResult result =  this.mockMvc.perform(put("/v1/transactions/update")
                        .content(JSONUtils.objectToJson(map))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andExpect(content().string(containsString("You should inform a key to perform this action!")))
                .andReturn();

        String content = result.getResponse().getContentAsString();
       // jsonPath("Message").value("You should inform a key to perform this action!")
    }

}
