package me.lucasfelix.beerdelivery.graphql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import me.lucasfelix.beerdelivery.util.GraphQLTestUtils;
import me.lucasfelix.beerdelivery.util.RestTemplateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql("classpath:data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FindByIdQueryTest {

    @Autowired
    private String findPDVByIdPayload;

    @Autowired
    private GraphQLTestUtils graphQLTestUtils;

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    @Test
    public void givenAQuery_whenFindPDVById_thenReturnResponse() {

        var jsonPayload = graphQLTestUtils.createJsonQuery(findPDVByIdPayload);

        var response = restTemplateUtils.doRequest(jsonPayload);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        JsonNode parsedResponse = graphQLTestUtils.parse(response.getBody());

        assertThat(parsedResponse)
                .isNotNull();

        assertThat(parsedResponse.get("data"))
                .isNotNull();

        assertThat(parsedResponse.get("data").get("findPDVById"))
                .isNotNull();

        assertThat(parsedResponse.get("data").get("findPDVById").get("document"))
                .isNotNull()
                .isEqualTo(new TextNode("02.453.716/000170"));

        assertThat(parsedResponse.get("errors")).isNull();
    }
}