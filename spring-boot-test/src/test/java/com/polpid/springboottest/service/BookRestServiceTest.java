package com.polpid.springboottest.service;

import com.polpid.springboottest.domain.Book;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(BookRestService.class)
public class BookRestServiceTest {

    @Autowired
    private BookRestService bookRestService;

    @Autowired
    private MockRestServiceServer server;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void rest_test(){
        this.server.expect(requestTo("/rest/test"))
                .andRespond(withSuccess("{\"idx\":null, \"title\":\"test\", \"publishedAt\": null}" , MediaType.APPLICATION_JSON));

        Book book = this.bookRestService.getRestBook();
        assertThat(book.getTitle()).isEqualTo("test");
    }

    @Test
    public void rest_error_test(){
        this.server.expect(requestTo("/rest/test"))
                .andRespond(withServerError());
        this.thrown.expect(HttpServerErrorException.class);

        this.bookRestService.getRestBook();
    }
}
