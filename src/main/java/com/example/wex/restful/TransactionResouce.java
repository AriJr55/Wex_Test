package com.example.wex.restful;

import com.example.wex.entity.Transactions;
import com.example.wex.exceptions.BusinessException;
import com.example.wex.repository.TransactionRepository;
import com.example.wex.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

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
    public List<Transactions> findByDestinyKey (@RequestParam(name = "destinyKey") Long destinyKey,
                                                @RequestParam(name = "page", defaultValue = "0", required = false) int page) {
        return transactionRepository.findByDestinyKeyOrderByTransactionDateDesc(destinyKey, PageRequest.of(page, 10));
    }

    @PostMapping("/save")
    public Transactions save (@RequestBody Map<String,Object> inputInfo) throws BusinessException {
        return transactionRepository.save(transactionService.createEntity(inputInfo));
    }
}
