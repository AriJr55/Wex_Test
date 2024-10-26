package com.example.wex.restful;

import com.example.wex.entity.Transactions;
import com.example.wex.exceptions.BusinessException;
import com.example.wex.repository.TransactionRepository;
import com.example.wex.service.TransactionService;
import com.example.wex.vo.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionResouce {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/")
    public Page<Transactions> query (@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
        return transactionRepository.findAll(PageRequest.of(page, 10));
    }

    @RequestMapping("/findByDestinyKey")
    public List<TransactionVO> findByDestinyKey (@RequestParam(name = "destinyKey") Long destinyKey,
                                                 @RequestParam(name = "page", defaultValue = "0", required = false) int page) {
        List<Transactions> transactionsList = transactionRepository.findByDestinyKeyOrderByTransactionDateDesc(destinyKey, PageRequest.of(page, 10));
        List<TransactionVO> transactionsVoList = new ArrayList<>();
        for(Transactions vo : transactionsList) {
            transactionsVoList.add(new TransactionVO(vo));
        }
        return transactionsVoList;
    }

    @PostMapping("/save")
    public ResponseEntity<TransactionVO> save (@RequestBody Map<String,Object> inputInfo) throws BusinessException {
        return ResponseEntity.ok(new TransactionVO(transactionRepository.save(transactionService.createEntity(inputInfo))));
    }
}
