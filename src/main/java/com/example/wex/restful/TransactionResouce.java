package com.example.wex.restful;

import com.example.wex.entity.Transactions;
import com.example.wex.exceptions.BusinessException;
import com.example.wex.repository.TransactionRepository;
import com.example.wex.service.CurrencyService;
import com.example.wex.service.TransactionService;
import com.example.wex.vo.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionResouce {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/")
    public Page<Transactions> query (@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
        return transactionRepository.findAll(PageRequest.of(page, 10));
    }

    @GetMapping("/findByDestinyKey")
    public List<TransactionVO> findByDestinyKey (@RequestParam(name = "destinyKey") Long destinyKey,
                                                 @RequestParam(name = "page", defaultValue = "0", required = false) int page) {
        List<Transactions> transactionsList = transactionRepository.findByDestinyKeyOrderByTransactionDateDesc(destinyKey, PageRequest.of(page, 1));
        List<TransactionVO> transactionsVoList = new ArrayList<>();
        for(Transactions vo : transactionsList) {
            transactionsVoList.add(new TransactionVO(vo));
        }
        return transactionsVoList;
    }

    @GetMapping("/findByDestinyKeyWithCurrency")
    public ResponseEntity<Map> findByDestinyKeyWithCurrency (@RequestParam(name = "destinyKey") Long destinyKey,
                                                             @RequestParam(name = "country") String country, @RequestParam(name = "currency") String currency) throws BusinessException {
        List<Transactions> transactionsList = transactionRepository.findByDestinyKeyOrderByTransactionDateDesc(destinyKey,PageRequest.of(0,10));
        return currencyService.getCurrency(transactionsList, country, currency);

    }

    @PostMapping("/save")
    public ResponseEntity<TransactionVO> save (@RequestBody Map<String,Object> inputInfo) throws BusinessException {
        return ResponseEntity.ok(new TransactionVO(transactionRepository.save(transactionService.createEntity(inputInfo))));
    }

    @PutMapping("/update")
    public ResponseEntity<TransactionVO> update (@RequestBody Map<String,Object> inputInfo) {
        UUID identifier = UUID.fromString((String) inputInfo.get("purchaseKey"));
        Transactions transaction = transactionRepository.findByPurchaseKey(identifier);
        transaction.setDestinyKey(Long.parseLong(String.valueOf(inputInfo.get("DestinyKey"))));
        transaction.setDescription((String) inputInfo.get("Description"));
        transaction.setPurchaseAmount(Double.parseDouble(String.valueOf(inputInfo.get("PurchaseAmount"))));

        return ResponseEntity.ok(new TransactionVO(transactionRepository.save(transaction)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map> delete (@RequestParam(name = "key") String key) {
        UUID identifier = UUID.fromString((String) key);

        transactionRepository.delete(transactionRepository.findByPurchaseKey(identifier));
        return new ResponseEntity<Map>(null, HttpStatus.NO_CONTENT);
    }

}
