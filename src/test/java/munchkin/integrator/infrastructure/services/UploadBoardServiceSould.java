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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UploadBoardServiceSould {

    private final UploadBoardService uploadBoardService;

    @Mock
    private BoardRepository boardRepository;

    public UploadBoardServiceSould() {
        MockitoAnnotations.openMocks(this);
        this.uploadBoardService = new UploadBoardService(boardRepository);
    }

    @Test
    public void call_repository_board_instance_to_save_new_board() {
        Board mockBoard = mock(Board.class);
        doReturn(new byte[0]).when(mockBoard).boardImage();
        doReturn(mock(Sizing.class)).when(mockBoard).sizing();
        doReturn(mock(BoardEntity.class)).when(boardRepository).save(any(BoardEntity.class));

        uploadBoardService.uploadNewBoard(mockBoard);

        verify(boardRepository).save(any(BoardEntity.class));
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
        doReturn(mock(BoardEntity.class)).when(boardRepository).save(any(BoardEntity.class));

        uploadBoardService.uploadNewBoard(mockBoard);

        verify(boardRepository).save(boardCaptor.capture());
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
        doReturn(entityOutput).when(boardRepository).save(any(BoardEntity.class));
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
        doReturn(entityOutput).when(boardRepository).save(any(BoardEntity.class));
        doReturn(null).when(entityOutput).getChecksum();

        boolean result = uploadBoardService.uploadNewBoard(mockBoard);

        assertThat(result).isFalse();
    }
}