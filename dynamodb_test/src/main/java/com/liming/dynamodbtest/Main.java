package com.liming.dynamodbtest;

import com.amazonaws.services.dynamodbv2.datamodeling.S3Link;
import com.liming.dynamodbtest.entity.CatalogItem;
import com.liming.dynamodbtest.utils.DynamodbUtils;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args){
        DynamodbUtils utils = new DynamodbUtils();
        S3Link image = utils.createS3Link("limingstore","images/1.jpg");
        CatalogItem newItem = newItem(601,"Book 601","11342543-324",new HashSet<String>(Arrays.asList("Author1","Author2")),image);
        utils.insertOne(newItem);
        newItem.getImage().uploadFrom(new File("C:\\Users\\HP\\Downloads\\1.jpg"));

//        CatalogItem item = utils.getOne(CatalogItem.class,601);
//        item.getImage().downloadTo(new File("E:\\1.jpg"));
    }

    private static CatalogItem newItem(Integer id, String title, String isbn, Set<String> authors, S3Link image){
        CatalogItem catalogItem = new CatalogItem();
        catalogItem.setId(id);
        catalogItem.setTitle(title);
        catalogItem.setISBN(isbn);
        catalogItem.setBookAuthors(authors);
        catalogItem.setImage(image);
        return catalogItem;
    }
}
