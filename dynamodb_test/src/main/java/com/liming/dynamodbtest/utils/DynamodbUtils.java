package com.liming.dynamodbtest.utils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.S3Link;
import com.amazonaws.services.s3.model.Region;

import java.util.List;

public class DynamodbUtils {

    private AmazonDynamoDB dynamoDB;

    private DynamoDBMapper mapper;

    public AmazonDynamoDB getDynamoDB() {
        return dynamoDB;
    }

    public void setDynamoDB(AmazonDynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public DynamodbUtils(){

        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try{
            credentialsProvider.getCredentials();
        }catch(Exception e){
            throw new AmazonClientException(
                    "cannot load the credentials",e
            );
        }
        dynamoDB = AmazonDynamoDBClientBuilder.standard().
                withCredentials(credentialsProvider)
                .withRegion("us-west-1")
                .build();
        mapper = new DynamoDBMapper(dynamoDB,credentialsProvider);
    }

    public <T> T getOne(Class<T> clazz,int id){
        T result = mapper.load(clazz,id);
        return result;
    }

    public <T> void insertOne(T object){
        mapper.save(object);
    }

    public <T> void batchInsert(List<T> objects){
        mapper.batchSave(objects);
    }

    public <T> void update(T update){
        mapper.save(update);
    }

    public <T> T update(Class<T> clazz,int id,T update){
        mapper.save(update);
        // Retrieve the updated item.
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
                .build();
        T itemRetrieved = mapper.load(clazz,id,config);
        return itemRetrieved;
    }

    public <T> void delete(T object){
        mapper.delete(object);
    }

    public S3Link createS3Link(String bucketName,String key){
        return mapper.createS3Link(Region.US_West,bucketName, key);
    }
}
