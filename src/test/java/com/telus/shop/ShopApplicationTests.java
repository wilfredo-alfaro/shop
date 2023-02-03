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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShopApplicationTests {

    public static final String serialNumber1 = "fdf75685-e8c3-479e-b8a0-bfbfd7a11db3";
    private static final int initialAvailableSize = 12;
    private static final int initialReservedSize = 0;
    final Logger logger = LoggerFactory.getLogger(ShopApplicationTests.class);
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    static void setup() {
        Stock.getInstance().getItems().add(new Item("waiter", serialNumber1));
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

    // Since I have initialized my stock with 12 items, getting
    // the list of available items should return a list of
    // very same size and OK (200) status.
    @Test
    void test001() {
        final ResponseEntity<List> entity = testRestTemplate.exchange("/items?reserved=false", HttpMethod.GET, new HttpEntity<>(new ItemService()), List.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(initialAvailableSize).isEqualTo(entity.getBody().size());
    }

    // Getting the list of reserved items should return a list of
    // size 0 and OK (200) status.
    @Test
    void test002() {
        final ResponseEntity<List> entity = testRestTemplate.exchange("/items?reserved=true", HttpMethod.GET, new HttpEntity<>(new ItemService()), List.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(initialReservedSize).isEqualTo(entity.getBody().size());
    }

    // Trying to reserve an item without specifying a
    // serial number should result in Bad Request (400) status.
    @Test
    void test003() {
        final ResponseEntity<String> entity = testRestTemplate.exchange("/items", HttpMethod.PUT, new HttpEntity<>(new ItemService()), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Similarly, trying to reserve an item while specifying a serial number
    // that doesn't comply to the defined pattern should result in Bad Request (400) status.
    @Test
    void test004() {
        final ResponseEntity<String> entity = testRestTemplate.exchange("/items?serialNumber=wontwork", HttpMethod.PUT, new HttpEntity<>(new ItemService()), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Now, trying to reserve an item while specifying a serial number
    // that does not exist should result in Not Found (404) status.
    @Test
    void test005() {
        final ResponseEntity<String> entity = testRestTemplate.exchange("/items?serialNumber=6b71b58c-320b-4c7e-a20b-b2c98fb5f161", HttpMethod.PUT, new HttpEntity<>(new ItemService()), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // Trying to reserv an item that is available should result in OK (200) status.
    @Test
    void test006() {
        final ResponseEntity<Item> entity = testRestTemplate.exchange("/items?serialNumber=" + serialNumber1, HttpMethod.PUT, new HttpEntity<>(new ItemService()), Item.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // Getting the list of available items should return a list of
    // size 12 and OK (200) status.
    @Test
    void test007() {
        final ResponseEntity<List> entity = testRestTemplate.exchange("/items?reserved=false", HttpMethod.GET, new HttpEntity<>(new ItemService()), List.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(initialAvailableSize - 1).isEqualTo(entity.getBody().size());
    }

    // Getting the list of reserved items should return a list of
    // size 1 and OK (200) status.
    @Test
    void test008() {
        final ResponseEntity<List> entity = testRestTemplate.exchange("/items?reserved=true", HttpMethod.GET, new HttpEntity<>(new ItemService()), List.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(initialReservedSize + 1).isEqualTo(entity.getBody().size());
    }


    // Trying to purchase an item without specifying a
    // serial number should result in Bad Request (400) status.
    @Test
    void test009() {
        final ResponseEntity<String> entity = testRestTemplate.exchange("/items", HttpMethod.POST, new HttpEntity<>(new ItemService()), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Similarly, trying to purchase an item while specifying a serial number
    // that doesn't comply to the defined pattern should result in Bad Request (400) status.
    @Test
    void test010() {
        final ResponseEntity<String> entity = testRestTemplate.exchange("/items?serialNumber=wontwork", HttpMethod.POST, new HttpEntity<>(new ItemService()), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Now, trying to purchase an item while specifying a serial number
    // that does not exist should result in Not Found (404) status.
    @Test
    void test011() {
        final ResponseEntity<String> entity = testRestTemplate.exchange("/items?serialNumber=6b71b58c-320b-4c7e-a20b-b2c98fb5f161", HttpMethod.POST, new HttpEntity<>(new ItemService()), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // Trying to purchase an item that is available or reserved should result in OK (200) status.
    @Test
    void test012() {
        final ResponseEntity<Item> entity = testRestTemplate.exchange("/items?serialNumber=" + serialNumber1, HttpMethod.POST, new HttpEntity<>(new ItemService()), Item.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // Trying to purchase the same item again should result in Not Found (404) status.
    @Test
    void test013() {
        final ResponseEntity<String> entity = testRestTemplate.exchange("/items?serialNumber=" + serialNumber1, HttpMethod.POST, new HttpEntity<>(new ItemService()), String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // Getting the list of available items should return a list of
    // size 11 and OK (200) status.
    @Test
    void test014() {
        final ResponseEntity<List> entity = testRestTemplate.exchange("/items?reserved=false", HttpMethod.GET, new HttpEntity<>(new ItemService()), List.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(initialAvailableSize - 1).isEqualTo(entity.getBody().size());
    }

    // Getting the list of reserved items should return a list of
    // size 0 and OK (200) status.
    @Test
    void test015() {
        final ResponseEntity<List> entity = testRestTemplate.exchange("/items?reserved=true", HttpMethod.GET, new HttpEntity<>(new ItemService()), List.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(initialReservedSize).isEqualTo(entity.getBody().size());
    }

}
