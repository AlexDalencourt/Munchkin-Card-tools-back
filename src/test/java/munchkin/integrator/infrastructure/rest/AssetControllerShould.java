package munchkin.integrator.infrastructure.rest;

import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.UploadBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssetController.class)
class AssetControllerShould {

    public static final String NUMBER_OF_COLUMNS = "numberOfColumns";
    public static final String NUMBER_OF_LINES = "numberOfLines";
    public static final String URL_UPLOAD_BOARD = "/asset/board";
    @MockBean
    private UploadBoard boardUploadingService;

    private final MockMvc mvc;

    private final MockMultipartFile mockMultipartFileCard;

    public AssetControllerShould(@Autowired MockMvc mvc, @Value("classpath:card.jpg") Resource card) throws IOException {
        requireNonNull(mvc);
        requireNonNull(card);
        this.mvc = mvc;
        this.mockMultipartFileCard = new MockMultipartFile("file", "test.png", MediaType.MULTIPART_FORM_DATA_VALUE, card.getInputStream().readAllBytes());
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
        ).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void accept_valid_input_format() throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "1")
                        .param(NUMBER_OF_LINES, "1")
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
        );

        verify(boardUploadingService).uploadNewBoard(boardCaptor.capture());
        verifyNoMoreInteractions(boardUploadingService);

        Board capturedBoard = boardCaptor.getValue();
        assertThat(capturedBoard.boardImage()).isEqualTo(mockMultipartFileCard.getBytes());
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
        ).andExpect(status().is5xxServerError());
    }

    @Test
    public void upload_new_board_should_response_with_created_when_success() throws Exception {
        mvc.perform(
                multipart(URL_UPLOAD_BOARD)
                        .file(mockMultipartFileCard)
                        .param(NUMBER_OF_COLUMNS, "2")
                        .param(NUMBER_OF_LINES, "1")
        ).andExpect(status().isCreated());
    }
}