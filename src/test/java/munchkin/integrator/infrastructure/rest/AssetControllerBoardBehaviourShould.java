package munchkin.integrator.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import munchkin.integrator.domain.asset.Image;
import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.Sizing;
import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.domain.card.Card;
import munchkin.integrator.domain.card.Type;
import munchkin.integrator.infrastructure.rest.responses.boards.BoardResponseLight;
import munchkin.integrator.infrastructure.rest.responses.boards.BoardResponseWithResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static munchkin.integrator.domain.card.Type.DUNGEON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssetController.class)
class AssetControllerBoardBehaviourShould {

    public static final String NUMBER_OF_COLUMNS = "numberOfColumns";
    public static final String NUMBER_OF_LINES = "numberOfLines";
    public static final String BOARD_CARD_TYPE = "boardType";
    public static final String URL_UPLOAD_BOARD = "/asset/board";
    public static final String URL_UPLOAD_BOARD_FULL = "/asset/board/full";

    @MockBean
    private UploadBoard boardUploadingService;

    private final MockMvc mvc;
    private final ObjectMapper jsonMapper;

    private final MockMultipartFile mockMultipartFileCard;
    private final Resource cardImage;


    public AssetControllerBoardBehaviourShould(@Autowired MockMvc mvc, @Autowired ObjectMapper jsonMapper, @Value("classpath:card.jpg") Resource card) throws IOException {
        this.mvc = requireNonNull(mvc);
        this.jsonMapper = requireNonNull(jsonMapper);
        this.mockMultipartFileCard = new MockMultipartFile("file", "test.png", MediaType.MULTIPART_FORM_DATA_VALUE, card.getInputStream().readAllBytes());
        this.cardImage = requireNonNull(card);
    }

    @BeforeEach
    public void initEach() {
        doReturn(true).when(boardUploadingService).uploadNewBoard(any(Board.class));
    }

    @Test
    public void reject_invalid_input_format_with_415_error_code() throws Exception {
        MockMultipartFile mockInputFile = new MockMultipartFile("file", "card.jpg", MediaType.MULTIPART_FORM_DATA_VALUE, "file".getBytes());
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockInputFile)
                        .param(NUMBER_OF_COLUMNS, "0")
                        .param(NUMBER_OF_LINES, "0")
                        .param(BOARD_CARD_TYPE, DUNGEON.name())
        ).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void accept_valid_input_format() throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "1")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, DUNGEON.name())
        ).andExpect(status().isCreated());
    }

    @ParameterizedTest
    @CsvSource({"0", "-1", "-2"})
    public void reject_invalid_size_column_less_or_equals_to_0(int input) throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, input + "")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, DUNGEON.name())
        ).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({"0", "-1", "-2"})
    public void reject_invalid_size_line_less_or_equals_to_0(int input) throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, input + "")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, DUNGEON.name())
        ).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({"CARD", "au"})
    public void reject_not_exist_board_type_for_upload_bord(String cardType) throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "1")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, cardType)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void reject_not_board_type_for_upload_board() throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "1")
                        .param(NUMBER_OF_LINES, "1")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void reject_empty_board_type_upload_board() throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "1")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, "")
        ).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @EnumSource(Type.class)
    public void accept_values_of_enum_types_for_upload_board(Type boardType) throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "1")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, boardType.name())
        ).andExpect(status().isCreated());
    }

    @Test
    public void reject_board_type_not_send() throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_LINES, "1")
                        .param(NUMBER_OF_COLUMNS, "1")
        ).andExpect(status().isBadRequest());
    }

    @Captor
    private ArgumentCaptor<Board> boardCaptor;

    @Test
    public void upload_new_board() throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "2")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, DUNGEON.name())
        );

        verify(boardUploadingService).uploadNewBoard(boardCaptor.capture());
        verifyNoMoreInteractions(boardUploadingService);

        Board capturedBoard = boardCaptor.getValue();
        assertThat(capturedBoard.boardImage()).isEqualTo(new Image(mockMultipartFileCard.getBytes()));
        assertThat(capturedBoard.sizing().numberOfColumns()).isEqualTo(2);
        assertThat(capturedBoard.sizing().numberOfLines()).isEqualTo(1);
    }

    @Test
    public void upload_new_board_should_response_with_error_if_save_fail() throws Exception {
        doReturn(false).when(boardUploadingService).uploadNewBoard(any(Board.class));

        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "2")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, DUNGEON.name())
        ).andExpect(status().is5xxServerError());
    }

    @Test
    public void upload_new_board_should_response_with_created_when_success() throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "2")
                        .param(NUMBER_OF_LINES, "1")
                        .param(BOARD_CARD_TYPE, DUNGEON.name())
        ).andExpect(status().isCreated());
    }

    @Test
    public void call_service_to_get_all_boards() throws Exception {
        mvc.perform(get(URL_UPLOAD_BOARD)).andExpect(status().isOk());

        verify(boardUploadingService).getAllBoards(eq(false));
    }

    @Test
    public void call_service_to_get_all_boards_full_with_image() throws Exception {
        mvc.perform(get(URL_UPLOAD_BOARD_FULL)).andExpect(status().isOk());

        verify(boardUploadingService).getAllBoards(anyBoolean());
    }

    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3"})
    public void return_mapped_output_for_each_result_of_get_all_board_with_variable_size(int numberOfResults) throws Exception {
        List<Board> allBoardServiceResults = new ArrayList<>();
        for (long index = 0; index < numberOfResults; index++) {
            allBoardServiceResults.add(new Board(index, new Sizing(1, 2), new Image(cardImage.getInputStream().readAllBytes()), new ArrayList<Card>()));
        }
        doReturn(allBoardServiceResults).when(boardUploadingService).getAllBoards(anyBoolean());

        String outputAsJson = mvc.perform(get(URL_UPLOAD_BOARD)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        BoardResponseLight[] responseLight = jsonMapper.readValue(outputAsJson, BoardResponseLight[].class);

        assertThat(responseLight).isNotNull();
        assertThat(responseLight).hasSameSizeAs(allBoardServiceResults);
        Arrays.stream(responseLight).forEach(response -> assertThat(
                allBoardServiceResults.stream().filter(
                        board -> !(
                                board.boardId().equals(response.getBoardId())
                                        && board.sizing().numberOfLines() == response.getSizing().getNumberOfLines()
                                        && board.sizing().numberOfColumns() == response.getSizing().getNumberOfLines()
                        )
                ).collect(Collectors.toList())
        ).hasSize(numberOfResults));
    }

    @ParameterizedTest
    @CsvSource({"0", "1", "2", "3"})
    public void return_mapped_output_for_each_result_of_get_all_board_full_with_variable_size(int numberOfResults) throws Exception {
        List<Board> allBoardServiceResults = new ArrayList<>();
        for (long index = 0; index < numberOfResults; index++) {
            allBoardServiceResults.add(new Board(index, new Sizing(1, 2), new Image(cardImage.getInputStream().readAllBytes()), new ArrayList<Card>()));
        }
        doReturn(allBoardServiceResults).when(boardUploadingService).getAllBoards(anyBoolean());

        String outputAsJson = mvc.perform(get(URL_UPLOAD_BOARD_FULL)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        System.out.println(outputAsJson);
        BoardResponseWithResource[] responseLight = jsonMapper.readValue(outputAsJson, BoardResponseWithResource[].class);

        assertThat(responseLight).isNotNull();
        assertThat(responseLight).hasSameSizeAs(allBoardServiceResults);
        Arrays.stream(responseLight).forEach(response -> assertThat(
                allBoardServiceResults.stream().filter(
                        board -> !(
                                board.boardId().equals(response.getBoardId())
                                        && board.sizing().numberOfLines() == response.getSizing().getNumberOfLines()
                                        && board.sizing().numberOfColumns() == response.getSizing().getNumberOfLines()
                                        && board.boardImage().equals(new Image(response.getImage()))
                        )
                ).collect(Collectors.toList())
        ).hasSize(numberOfResults));
    }

    @Test
    public void call_service_to_get_all_boards_with_false_option_resize_image() throws Exception {
        mvc.perform(get(URL_UPLOAD_BOARD)).andExpect(status().isOk());

        verify(boardUploadingService).getAllBoards(eq(false));
    }


    @Test
    public void call_service_to_get_all_boards_with_false_option_resize_image_when_option_is_true_on_parameter() throws Exception {
        mvc.perform(get(URL_UPLOAD_BOARD).param("resizeImages", "true")).andExpect(status().isOk());

        verify(boardUploadingService).getAllBoards(eq(false));
    }

    @Test
    public void call_service_to_get_all_boards_full_with_option_resize_image_when_option_is_on_parameters() throws Exception {
        mvc.perform(get(URL_UPLOAD_BOARD_FULL).param("resizeImages", "true")).andExpect(status().isOk());

        verify(boardUploadingService).getAllBoards(eq(true));
    }

    @Test
    public void call_service_to_get_all_boards_full_with_option_resize_image_false_when_option_is_false_on_parameters() throws Exception {
        mvc.perform(get(URL_UPLOAD_BOARD_FULL).param("resizeImages", "false")).andExpect(status().isOk());

        verify(boardUploadingService).getAllBoards(eq(false));
    }

    @Test
    public void call_service_to_get_all_boards_full_with_option_resize_image_false_when_option_is_not_on_parameters() throws Exception {
        mvc.perform(get(URL_UPLOAD_BOARD_FULL).param("resizeImages", "false")).andExpect(status().isOk());

        verify(boardUploadingService).getAllBoards(eq(false));
    }
}