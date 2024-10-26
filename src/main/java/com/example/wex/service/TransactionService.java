package com.example.wex.service;

import com.example.wex.entity.Transactions;
import com.example.wex.exceptions.BusinessException;
import com.example.wex.utils.JSONUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransactionService {


    public Transactions createEntity(Map<String, Object> inputInfo) throws BusinessException {
        String description = JSONUtils.readString(inputInfo, "Description");
        Double purchaseAmount  = Double.parseDouble((String) JSONUtils.readProperty(inputInfo, "PurchaseAmount"));
        Long destinyKey  = JSONUtils.readLong(inputInfo, "DestinyKey");

        if(description.length() > 50) {
            throw new BusinessException("Descript must must not exceed 50 characters!");
        }

        Transactions transaction = new Transactions();
        transaction.setPurchaseAmount(purchaseAmount);
        transaction.setDescription(description);
        transaction.setDestinyKey(destinyKey);

        return transaction;

    }
}
