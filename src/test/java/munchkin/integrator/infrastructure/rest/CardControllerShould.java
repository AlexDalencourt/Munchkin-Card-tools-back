package munchkin.integrator.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import munchkin.integrator.domain.Type;
import munchkin.integrator.domain.boards.UploadBoard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.MissingResourceException;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerShould {

    private final MockMvc mvc;
    private final ObjectMapper jsonMapper;

    @MockBean
    private UploadBoard boardUploadingService;

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

    @Test
    public void reject_crop_cards_when_board_param_is_not_present_with_bad_request() throws Exception {
        mvc.perform(put("/cards/crop")).andExpect(status().isBadRequest());
    }

    @Test
    public void reject_crop_cards_when_board_requested_not_exist() throws Exception {
        doThrow(new MissingResourceException("No board found", "Board", "1")).when(boardUploadingService).cropBoard(eq(1L));
        mvc.perform(put("/cards/crop").param("boardId", "1")).andExpect(status().isNotFound());
    }

    @Test
    public void ask_crop_on_board_when_all_params_is_good() throws Exception {
        mvc.perform(put("/cards/crop").param("boardId", "1"));

        verify(boardUploadingService).cropBoard(eq(1L));
    }

    @Test
    public void ask_crop_on_board_should_return_created_http_code() throws Exception {
        mvc.perform(put("/cards/crop").param("boardId", "1")).andExpect(status().isCreated());
    }
}