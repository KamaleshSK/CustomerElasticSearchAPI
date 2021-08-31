package com.customerDuplication.customerDuplicationImplementation.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;	

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customerDuplication.customerDuplicationImplementation.Models.CustomerProfileDocument;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class CustomerProfileService {
	
	@Autowired
	private RestHighLevelClient client;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public String createProfileDocument(CustomerProfileDocument document) throws Exception {
		
		UUID uuid = UUID.randomUUID();
		document.setId(uuid.toString());
		
		Map<String, Object> documentMapper = objectMapper.convertValue(document, Map.class);
        IndexRequest indexRequest = new IndexRequest("profiles").id(document.getId()).source(documentMapper);
		
		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
		
		return indexResponse.getResult().name();
	}
	
	public CustomerProfileDocument findById(String id) throws Exception {

        GetRequest getRequest = new GetRequest("profiles", id);

        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> resultMap = getResponse.getSource();

        return objectMapper.convertValue(resultMap, CustomerProfileDocument.class);
    }
	
	public String updateProfile(CustomerProfileDocument document) throws Exception {

        CustomerProfileDocument resultDocument = findById(document.getId());

        UpdateRequest updateRequest = new UpdateRequest("profiles", resultDocument.getId());

        Map<String, Object> documentMapper = 
          objectMapper.convertValue(document, Map.class);

        updateRequest.doc(documentMapper);

        UpdateResponse updateResponse = 
          client.update(updateRequest, RequestOptions.DEFAULT);

        return updateResponse.getResult().name();
    }
	
	public List<CustomerProfileDocument> findAll() throws Exception {

        SearchRequest searchRequest = new SearchRequest("profiles");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);

        return getSearchResult(searchResponse);
    }
	
	private List<CustomerProfileDocument> getSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();
        List<CustomerProfileDocument> profileDocuments = new ArrayList<>();

        if (searchHit.length > 0) {
            Arrays.stream(searchHit)
                    .forEach(hit -> profileDocuments
                            .add(objectMapper
                                    .convertValue(hit.getSourceAsMap(),
                                                    CustomerProfileDocument.class))
                    );
        }

        return profileDocuments;
    }
	
	public String deleteProfileDocument(String id) throws Exception {

        DeleteRequest deleteRequest = new DeleteRequest("profiles", id);
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);

        return response.getResult().name();
    }
	
	private Map<String, Object> convertProfileDocumentToMap(CustomerProfileDocument profileDocument) {
        return objectMapper.convertValue(profileDocument, Map.class);
    }
	
}
