package munchkin.integrator.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import munchkin.integrator.domain.asset.Asset;
import munchkin.integrator.domain.asset.AssetIndex;
import munchkin.integrator.domain.asset.Image;
import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.domain.card.Card;
import munchkin.integrator.domain.card.Type;
import munchkin.integrator.infrastructure.rest.responses.cards.CardResponseWithImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
class CardControllerShould {

    private final MockMvc mvc;
    private final ObjectMapper jsonMapper;

    @MockBean
    private UploadBoard mockBoardUploadingService;

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
    public void reject_crop_cards_when_board_param_is_not_present_with_not_found() throws Exception {
        mvc.perform(put("/cards/crop/{boardId}", "")).andExpect(status().isNotFound());
    }

    @Test
    public void reject_crop_cards_when_board_requested_not_exist() throws Exception {
        doThrow(new MissingResourceException("No board found", "Board", "1")).when(mockBoardUploadingService).cropBoard(eq(1L), eq(false));
        mvc.perform(put("/cards/crop/{boardId}", "1")).andExpect(status().isNotFound());
    }

    @Test
    public void ask_crop_on_board_when_all_params_is_good() throws Exception {
        mvc.perform(put("/cards/crop/{boardId}", "1"));

        verify(mockBoardUploadingService).cropBoard(eq(1L), eq(false));
    }

    @Test
    public void ask_crop_on_board_should_return_ok_http_code() throws Exception {
        mvc.perform(put("/cards/crop/{boardId}", "1")).andExpect(status().isOk());
    }

    @Test
    public void ask_crop_on_board_should_return_all_cropped_cards() throws Exception {
        final List<CardResponseWithImage> expectedResponse = new ArrayList<>();
        expectedResponse.add(new CardResponseWithImage("IMAGE1".getBytes()));
        expectedResponse.add(new CardResponseWithImage("IMAGE2".getBytes()));
        expectedResponse.add(new CardResponseWithImage("IMAGE3".getBytes()));
        final List<Card> serviceReturnCards = new ArrayList<>();
        serviceReturnCards.add(new Card(new Asset(new Image("IMAGE1".getBytes()), new AssetIndex(1, 0))));
        serviceReturnCards.add(new Card(new Asset(new Image("IMAGE3".getBytes()), new AssetIndex(0, 2))));
        serviceReturnCards.add(new Card(new Asset(new Image("IMAGE2".getBytes()), new AssetIndex(0, 0))));
        doReturn(serviceReturnCards).when(mockBoardUploadingService).cropBoard(anyLong(), anyBoolean());

        String restResponseAsString = mvc.perform(put("/cards/crop/{boardId}", "1")).andReturn().getResponse().getContentAsString();

        CardResponseWithImage[] returnedObject = jsonMapper.readValue(restResponseAsString, CardResponseWithImage[].class);
        assertThat(returnedObject).hasSameElementsAs(expectedResponse);
    }

    @Test
    public void ask_crop_on_board_and_save_must_call_service_with_good_param() throws Exception {
        mvc.perform(put("/cards/crop/{boardId}", "1").param("persist", "true")).andExpect(status().isOk());

        verify(mockBoardUploadingService).cropBoard(eq(1L), eq(true));
    }
}