package com.telus.shop;

import com.telus.shop.model.Item;
import com.telus.shop.model.Stock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShopApplicationTests {

    final Logger logger = LoggerFactory.getLogger(ShopApplicationTests.class);


    @BeforeAll
    static void setup() {
        Stock.getInstance().getItems().add(new Item("waiter", "fdf75685-e8c3-479e-b8a0-bfbfd7a11db3"));
        Stock.getInstance().getItems().add(new Item("window", "2a6d7fe4-5244-40ef-bb77-5b1fa5b327b5"));
        Stock.getInstance().getItems().add(new Item("winter", "f3e2a894-4bc8-4d1f-a225-a76bf4be12b7"));
        Stock.getInstance().getItems().add(new Item("wisdom", "84cfc599-4ae7-4b07-bdc8-150c00577898"));
        Stock.getInstance().getItems().add(new Item("wizard", "ae6109c1-1606-44f9-85e9-1a5cb985fbc5"));
        Stock.getInstance().getItems().add(new Item("wonder", "3b0112d0-9bf3-4c2c-af98-4945ab560271"));
        Stock.getInstance().getItems().add(new Item("yankee", "09b46074-2516-4171-8528-54373b1c2a0f"));
        Stock.getInstance().getItems().add(new Item("yellow", "f9eb406d-aa01-446d-aadb-aab6c1b9d391"));
        Stock.getInstance().getItems().add(new Item("yogurt", "f496364d-ee2a-4a54-9a71-d7679f7b3fbb"));
        Stock.getInstance().getItems().add(new Item("zigzag", "daa6613f-9d8e-43d3-a8c0-f9d5a63fa9e7"));
        Stock.getInstance().getItems().add(new Item("zipper", "6cda8cce-ba0f-4c53-8d8b-292bb8b99f40"));
        Stock.getInstance().getItems().add(new Item("zodiac", "d90317dd-0f56-4781-8719-529beb8fc76c"));
    }

    @Test
    void contextLoads() {
    }

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void test() {
        final int stockItemsSize = Stock.getInstance().getItems().size();
        final ResponseEntity<List> entity = testRestTemplate.exchange("/items?reserved=false", HttpMethod.GET, new HttpEntity<>(new ItemService()), List.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).asList().size().isEqualTo(stockItemsSize);
        logger.info("entity.getBody().toString(): {}", entity.getBody().toString());
    }

}
