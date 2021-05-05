package munchkin.integrator.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import munchkin.integrator.domain.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerShould {

    private final MockMvc mvc;
    private final ObjectMapper jsonMapper;

    public CardControllerShould(@Autowired MockMvc mvc, @Autowired ObjectMapper jsonMapper) {
        this.mvc = requireNonNull(mvc);
        this.jsonMapper = requireNonNull(jsonMapper);
    }

    @Test
    public void return_all_types_of_card() throws Exception {
        String outputString = mvc.perform(get("/cards/types")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Type[] listOfTypes = jsonMapper.readValue(outputString, Type[].class);

        assertThat(listOfTypes).hasSameElementsAs(Arrays.asList(Type.values()));
    }
}