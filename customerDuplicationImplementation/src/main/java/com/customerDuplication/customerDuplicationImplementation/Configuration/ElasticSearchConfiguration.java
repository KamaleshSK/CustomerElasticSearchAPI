package com.customerDuplication.customerDuplicationImplementation.Configuration;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.http.AWSRequestSigningApacheInterceptor;

@Configuration
public class ElasticSearchConfiguration {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;
    
    @Value("${aws.serviceName}")
    private String serviceName;

    @Value("${aws.region}")
    private String region; 
    
    @Value("${aws.accessKeyId}")
    private String accessKeyId;
    
    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;
    
    @Bean(destroyMethod = "close")
    public RestHighLevelClient esClient() {
    	BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretAccessKey);
    	
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(serviceName, signer, new AWSStaticCredentialsProvider(awsCreds));
        return new RestHighLevelClient(RestClient.builder(HttpHost.create(elasticsearchHost)).setHttpClientConfigCallback(hacb -> hacb.addInterceptorLast(interceptor)));
    }


}
