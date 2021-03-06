package com.customerDuplication.customerDuplicationImplementation.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customerDuplication.customerDuplicationImplementation.Models.CustomerSMSDocument;
import com.customerDuplication.customerDuplicationImplementation.Utils.RandomStringGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerSMSService {
	
	@Autowired
	private RestHighLevelClient client;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RandomStringGenerator randomStringGenerator;

	public String createSMSDocument(CustomerSMSDocument document) throws IOException {
		
		UUID uuid = UUID.randomUUID();
		document.setId(uuid.toString());
		
		Map<String, Object> documentMapper = objectMapper.convertValue(document, Map.class);
        IndexRequest indexRequest = new IndexRequest("smsdata").id(document.getId()).source(documentMapper);
		
		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
		
		return indexResponse.getResult().name();
	}
	
	public String createBulkSMSDocument(List<CustomerSMSDocument> documentList) throws IOException {
		
		BulkRequest request = new BulkRequest("smsdata");
		
		for (int i=0; i<documentList.size(); i++) {
			CustomerSMSDocument document = documentList.get(i);
			
			UUID uuid = UUID.randomUUID();
			document.setId(uuid.toString());
			
			document.setName(randomStringGenerator.generateRandomString());
			
			Map<String, Object> documentMapper = objectMapper.convertValue(document, Map.class);
			
			request.add(new IndexRequest("smsdata").id(document.getId()).source(documentMapper));
		}
		
		BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);

		return bulkResponse.getItems().getClass().getName();
	}

	public List<CustomerSMSDocument> findAll() throws IOException {
		
		SearchRequest searchRequest = new SearchRequest("smsdata");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(250); 
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);

        return getSearchResult(searchResponse);
	}
	
	// change SearchRequest to DeleteRequest after confirmation from rishi sir
	public List<CustomerSMSDocument> deleteJunkSMS(List<String> keywordList) throws IOException {
		
		SearchRequest searchRequest = new SearchRequest("smsdata");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .mustNot(QueryBuilders
                        .termsQuery("sms", new ArrayList<String>(List.of(
                        		"credited", "debited", "transferred", "payment", "deposit", "withdrawal", "auto-debit",
                        		 "upi", "neft", "imps", "rtgs", "emi", "withdrawn", "deposited"
                        		))));

        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(250);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response =
                client.search(searchRequest, RequestOptions.DEFAULT);

        return getSearchResult(response);
	}
	
	
	
	private List<CustomerSMSDocument> getSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();
        List<CustomerSMSDocument> smsDocuments = new ArrayList<>();

        if (searchHit.length > 0) {
            Arrays.stream(searchHit)
                    .forEach(hit -> smsDocuments
                            .add(objectMapper
                                    .convertValue(hit.getSourceAsMap(),
                                                    CustomerSMSDocument.class))
                    );
        }

        return smsDocuments;
    }

	

}
