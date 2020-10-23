package com.liming.dynamodbtest;

import com.liming.dynamodbtest.entity.CatalogItem;
import com.liming.dynamodbtest.utils.DynamodbUtils;

import java.util.*;

public class Main {
    public static void main(String[] args){
        DynamodbUtils utils = new DynamodbUtils();
        CatalogItem newItem = newItem(601,"Book 601","11342543-324",new HashSet<String>(Arrays.asList("Author1","Author2")));
        utils.insertOne(newItem);
    }

    private static CatalogItem newItem(Integer id, String title, String isbn,Set<String> authors){
        CatalogItem catalogItem = new CatalogItem();
        catalogItem.setId(id);
        catalogItem.setTitle(title);
        catalogItem.setISBN(isbn);
        catalogItem.setBookAuthors(authors);
        return catalogItem;
    }
}
