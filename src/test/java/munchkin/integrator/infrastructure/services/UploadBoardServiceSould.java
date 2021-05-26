package munchkin.integrator.infrastructure.services;

import munchkin.integrator.domain.asset.AssetIndex;
import munchkin.integrator.domain.asset.Image;
import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.Sizing;
import munchkin.integrator.domain.card.Card;
import munchkin.integrator.infrastructure.repositories.BoardRepository;
import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UploadBoardServiceSould {

    private final UploadBoardService uploadBoardService;

    @Mock
    private BoardRepository mockBoardRepository;

    @Mock
    private ImageService mockImageService;

    public UploadBoardServiceSould() {
        MockitoAnnotations.openMocks(this);
        this.uploadBoardService = new UploadBoardService(mockBoardRepository, mockImageService);
    }

    @Test
    public void call_repository_board_instance_to_save_new_board() {
        Board mockBoard = mock(Board.class);
        doReturn(new Image(new byte[0])).when(mockBoard).boardImage();
        doReturn(mock(Sizing.class)).when(mockBoard).sizing();
        doReturn(mock(BoardEntity.class)).when(mockBoardRepository).save(any(BoardEntity.class));

        uploadBoardService.uploadNewBoard(mockBoard);

        verify(mockBoardRepository).save(any(BoardEntity.class));
    }

    @Captor
    private ArgumentCaptor<BoardEntity> boardCaptor;

    @Test
    public void map_board_model_to_entity() {
        Board mockBoard = mock(Board.class);
        byte[] inputImage = "test".getBytes();
        Sizing size = new Sizing(1, 2);
        doReturn(new Image(inputImage)).when(mockBoard).boardImage();
        doReturn(size).when(mockBoard).sizing();
        doReturn(mock(BoardEntity.class)).when(mockBoardRepository).save(any(BoardEntity.class));

        uploadBoardService.uploadNewBoard(mockBoard);

        verify(mockBoardRepository).save(boardCaptor.capture());
        BoardEntity entity = boardCaptor.getValue();
        assertThat(entity.getImage()).isEqualTo(inputImage);
        assertThat(entity.getColumns()).isEqualTo(size.numberOfColumns());
        assertThat(entity.getLines()).isEqualTo(size.numberOfLines());
    }

    @Test
    public void return_true_when_board_succesful_persist() {
        Board mockBoard = mock(Board.class);
        byte[] inputImage = "test".getBytes();
        Sizing size = new Sizing(1, 2);
        doReturn(new Image(inputImage)).when(mockBoard).boardImage();
        doReturn(size).when(mockBoard).sizing();
        BoardEntity entityOutput = mock(BoardEntity.class);
        doReturn(entityOutput).when(mockBoardRepository).save(any(BoardEntity.class));
        doReturn(1L).when(entityOutput).getChecksum();


        boolean result = uploadBoardService.uploadNewBoard(mockBoard);

        assertThat(result).isTrue();
    }

    @Test
    public void return_false_when_board_failed_persist() {
        Board mockBoard = mock(Board.class);
        byte[] inputImage = "test".getBytes();
        Sizing size = new Sizing(1, 2);
        doReturn(new Image(inputImage)).when(mockBoard).boardImage();
        doReturn(size).when(mockBoard).sizing();
        BoardEntity entityOutput = mock(BoardEntity.class);
        doReturn(entityOutput).when(mockBoardRepository).save(any(BoardEntity.class));
        doReturn(null).when(entityOutput).getChecksum();

        boolean result = uploadBoardService.uploadNewBoard(mockBoard);

        assertThat(result).isFalse();
    }

    @Test
    public void return_mapped_output_from_find_all_boards_repository() {
        List<BoardEntity> boardEntities = Arrays.asList(mock(BoardEntity.class), mock(BoardEntity.class), mock(BoardEntity.class));
        doReturn(boardEntities).when(mockBoardRepository).findAll();

        List<Board> outputFromService = uploadBoardService.getAllBoards(false);

        assertThat(outputFromService).hasSameSizeAs(boardEntities);
        boardEntities.forEach(mock -> verify(mock).toBoard());
    }

    @Test
    public void call_resize_service_for_get_all_boards() {
        List<BoardEntity> boardEntities = Arrays.asList(mock(BoardEntity.class), mock(BoardEntity.class), mock(BoardEntity.class));
        doReturn(boardEntities).when(mockBoardRepository).findAll();

        uploadBoardService.getAllBoards(true);

        verify(mockImageService).reziseBoards(anyList(), eq(10));
    }

    @Test
    public void not_call_resize_service_for_get_all_boards_when_it_is_not_ask() {
        uploadBoardService.getAllBoards(false);

        verifyNoInteractions(mockImageService);
    }

    @Captor
    private ArgumentCaptor<List<Board>> boardListCaptor;

    @Test
    public void call_resize_service_with_mapped_board_list_from_database() {
        List<BoardEntity> boardEntities =
                Arrays.asList(
                        new BoardEntity(1L, new byte[0], 0, 0),
                        new BoardEntity(2L, new byte[0], 0, 0),
                        new BoardEntity(3L, new byte[0], 0, 0)
                );
        doReturn(boardEntities).when(mockBoardRepository).findAll();

        uploadBoardService.getAllBoards(true);

        verify(mockImageService).reziseBoards(boardListCaptor.capture(), eq(10));
        List<Board> boardListCaptured = boardListCaptor.getValue();
        assertThat(boardListCaptured).hasSameElementsAs(boardEntities.stream().map(BoardEntity::toBoard).collect(Collectors.toList()));

        verify(mockBoardRepository).findAll();
        verifyNoMoreInteractions(mockBoardRepository, mockImageService);
    }

    @Test
    public void call_resize_service_on_all_boards_should_return_new_list() {
        List<BoardEntity> boardEntities =
                Arrays.asList(
                        new BoardEntity(1L, new byte[0], 0, 0),
                        new BoardEntity(2L, new byte[0], 0, 0),
                        new BoardEntity(3L, new byte[0], 0, 0)
                );
        doReturn(boardEntities).when(mockBoardRepository).findAll();
        List<Board> boardsResized =
                Arrays.asList(
                        new Board(4L, new Sizing(0, 0), new Image(new byte[0])),
                        new Board(5L, new Sizing(0, 0), new Image(new byte[0])),
                        new Board(6L, new Sizing(0, 0), new Image(new byte[0]))
                );
        doReturn(boardsResized).when(mockImageService).reziseBoards(anyList(), eq(10));

        List<Board> outputBoardList = uploadBoardService.getAllBoards(true);

        assertThat(outputBoardList).hasSameElementsAs(boardsResized);
        assertThat(outputBoardList).doesNotContainAnyElementsOf(boardEntities.stream().map(BoardEntity::toBoard).collect(Collectors.toList()));
    }

    @Test
    public void retreive_the_board_to_croap() {
        BoardEntity board = mock(BoardEntity.class);
        doReturn(Optional.of(board)).when(mockBoardRepository).findById(eq(1L));

        uploadBoardService.cropBoard(1L);

        verify(mockBoardRepository).findById(eq(1L));
    }

    @Test
    public void throw_an_missing_data_exeption_when_trying_to_crop_no_exist_board() {
        doReturn(Optional.empty()).when(mockBoardRepository).findById(eq(0L));

        assertThrows(MissingResourceException.class, () -> uploadBoardService.cropBoard(0L));
    }

    @ParameterizedTest
    @CsvSource({"2,3", "1,5", "5,9", "0,1"})
    public void call_image_crop_service_for_each_card_on_board(int columnSize, int lineSize) {
        BoardEntity board = mock(BoardEntity.class);
        doReturn(columnSize).when(board).getColumns();
        doReturn(lineSize).when(board).getLines();
        doReturn(new byte[0]).when(board).getImage();
        doReturn(Optional.of(board)).when(mockBoardRepository).findById(anyLong());

        uploadBoardService.cropBoard(0L);

        verify(mockImageService, times(columnSize * lineSize)).cropImage(anyInt(), anyInt(), any(byte[].class));
        verifyNoMoreInteractions(mockImageService);
    }

    @ParameterizedTest
    @CsvSource({"2,3,IMAGE1", "1,5,IMAGE2", "5,9,IMAGE3", "1,1,IMAGE4"})
    public void call_image_crop_service_for_each_card_on_boar_with_good_params(int columnSize, int lineSize, String image) {
        BoardEntity board = mock(BoardEntity.class);
        doReturn(columnSize).when(board).getColumns();
        doReturn(lineSize).when(board).getLines();
        doReturn(image.getBytes()).when(board).getImage();
        doReturn(Optional.of(board)).when(mockBoardRepository).findById(anyLong());

        uploadBoardService.cropBoard(0L);

        for (int column = 0; column < columnSize; column++) {
            for (int line = 0; line < lineSize; line++) {
                verify(mockImageService).cropImage(eq(column), eq(line), eq(image.getBytes()));
            }
        }
        verifyNoMoreInteractions(mockImageService);
    }

    @ParameterizedTest
    @CsvSource({"2,3,IMAGE1", "1,5,IMAGE2", "5,9,IMAGE3", "1,1,IMAGE4"})
    public void return_board_with_croped_images_returned_from_crop_service(int columnSize, int lineSize, String image) {
        BoardEntity originalBoard = mock(BoardEntity.class);
        doReturn(columnSize).when(originalBoard).getColumns();
        doReturn(lineSize).when(originalBoard).getLines();
        doReturn(image.getBytes()).when(originalBoard).getImage();
        doReturn(new Image(image.getBytes())).when(mockImageService).cropImage(anyInt(), anyInt(), any(byte[].class));

        Board boardWithCropImages = uploadBoardService.cropBoard(0L);

        assertThat(boardWithCropImages.cards()).hasSize(columnSize * lineSize);
        for (int column = 0; column < columnSize; column++) {
            for (int line = 0; line < lineSize; line++) {
                int finalColumn = column;
                int finalLine = line;
                Optional<Card> foundCard =
                        boardWithCropImages.cards().stream()
                                .filter(card -> card.cardAsset().index().equals(new AssetIndex(finalColumn, finalLine)))
                                .findFirst();
                assertThat(foundCard).isPresent();
                assertThat(foundCard.get().cardAsset().image()).isEqualTo(new Image(image.getBytes()));
            }
        }
    }
}