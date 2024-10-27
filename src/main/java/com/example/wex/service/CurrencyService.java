package com.example.wex.service;

import com.example.wex.entity.Transactions;
import com.example.wex.exceptions.BusinessException;
import com.example.wex.exceptions.BusinessExceptionHandler;
import com.example.wex.utils.JSONUtils;
import com.example.wex.vo.TransactionConvertedVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    @Value("${service.currency.url}")
    private String url;

    @Value("${service.currency.endpoint}")
    private String endpoint;

    @Value("${service.currency.fields}")
    private String fields;

    @Value("${service.currency.time.period.begin}")
    private String timePeriodBegin;

    @Value("${service.currency.time.period.end}")
    private String timePeriodEnd;

    @Value("${service.currency.time.period.range}")
    private int range;

    private RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Map> getCurrency(List<Transactions> transactionsList, String country, String currency) throws BusinessException {
        List<TransactionConvertedVO> responseReturn = new ArrayList<>();
        for(Transactions transaction : transactionsList) {
            ResponseEntity<Map> currencyData = this.getCurrencyData(transaction, country, currency);
            Map aux = JSONUtils.jsonToObject(String.valueOf(currencyData.getBody()), Map.class);
            List<Map<String,Object>>data = (List) aux.get("data");
            responseReturn.add((new TransactionConvertedVO(transaction, data.get(0))));
        }
        return new ResponseEntity(responseReturn, HttpStatus.OK);
    }

    private ResponseEntity<Map> getCurrencyData(Transactions transaction, String country, String currency) throws BusinessException {
        LocalDate dateBegin = LocalDate.from(transaction.getTransactionDate().truncatedTo(ChronoUnit.DAYS));
        LocalDate dateEnd = dateBegin.minusMonths(range);
        String filter = timePeriodBegin + dateBegin + "," + timePeriodEnd + dateEnd
                + ",currency:eq:" + currency + ",country:eq:" + country;
        logger.info("----Filter: " + filter + "---");

        Map params = new HashMap<String, String>();
        params.put("filter", filter);
        params.put("fields", fields);
        params.put("sort", "-record_date");

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url+endpoint)
                .queryParam("filter",params.get("filter"))
                .queryParam("fields",params.get("fields"))
                .queryParam("sort", params.get("sort")).build();

        HttpEntity<String> requestEntity = new HttpEntity<>(null,  new HttpHeaders());


        ResponseEntity<Map> result;

        try {
            result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, String.class, params);
        } catch (Exception e) {
            return BusinessExceptionHandler.throwNoDataError("Error in the api call!");
        }

        Map<String,Object> mapResult = JSONUtils.jsonToObject(String.valueOf(result.getBody()), Map.class);

        if(( (List) mapResult.get("data")).size() < 1 ) {
            return BusinessExceptionHandler.throwNoDataError("No exchange rate found for the following info!", country, currency, dateBegin, dateEnd);
        }

        logger.info("---- Result: " + result + " ---");
        return result;
    }




}
