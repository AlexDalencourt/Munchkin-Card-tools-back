package munchkin.integrator.infrastructure.services;

import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.Sizing;
import munchkin.integrator.infrastructure.repositories.BoardRepository;
import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
        doReturn(new byte[0]).when(mockBoard).boardImage();
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
        doReturn(inputImage).when(mockBoard).boardImage();
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
        doReturn(inputImage).when(mockBoard).boardImage();
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
        doReturn(inputImage).when(mockBoard).boardImage();
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

        verify(mockImageService).reziseBoards(anyList());
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

        verify(mockImageService).reziseBoards(boardListCaptor.capture());
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
                        new Board(4L, new Sizing(0, 0), new byte[0]),
                        new Board(5L, new Sizing(0, 0), new byte[0]),
                        new Board(6L, new Sizing(0, 0), new byte[0])
                );
        doReturn(boardsResized).when(mockImageService).reziseBoards(anyList());

        List<Board> outputBoardList = uploadBoardService.getAllBoards(true);

        assertThat(outputBoardList).hasSameElementsAs(boardsResized);
        assertThat(outputBoardList).doesNotContainAnyElementsOf(boardEntities.stream().map(BoardEntity::toBoard).collect(Collectors.toList()));
    }
}