package com.example.wex.restful;

import com.example.wex.entity.Transactions;
import com.example.wex.exceptions.BusinessException;
import com.example.wex.exceptions.BusinessExceptionHandler;
import com.example.wex.repository.TransactionRepository;
import com.example.wex.service.CurrencyService;
import com.example.wex.service.TransactionService;
import com.example.wex.utils.JSONUtils;
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
    public ResponseEntity<Object> save (@RequestBody Map<String,Object> inputInfo) {
        TransactionVO vo;
        try {
           vo  = new TransactionVO(transactionRepository.save(transactionService.createEntity(inputInfo)));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("Message", e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<TransactionVO> update (@RequestBody Map<String,Object> inputInfo) {
        if((String) inputInfo.get("purchaseKey") == null || ((String) inputInfo.get("purchaseKey")).isEmpty()) {
            return new ResponseEntity(Map.of("Message", "You should inform a key to perform this action!"), HttpStatus.BAD_REQUEST);
        }
        UUID identifier = UUID.fromString((String) inputInfo.get("purchaseKey"));

        Transactions transaction = transactionRepository.findByPurchaseKey(identifier);
        if(transaction == null ) {
            return new ResponseEntity(Map.of("Message", "No transaction found with this key: " + identifier ), HttpStatus.BAD_REQUEST);
        }
        if(inputInfo.get("DestinyKey") != null) {
            return new ResponseEntity(Map.of("Message", "Destiny Key is not allowed to be changed!"), HttpStatus.BAD_REQUEST);
        }
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
