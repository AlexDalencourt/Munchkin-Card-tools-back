package munchkin.integrator.infrastructure.services;

import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.Sizing;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageServiceShould {

    private final ImageService imageService;

    private final Resource card;

    private final Resource card25Percent;

    public ImageServiceShould(@Value("classpath:card.jpg") Resource card, @Value("classpath:card-25%.jpg") Resource card25Percent) {
        MockitoAnnotations.openMocks(this);
        this.imageService = new ImageService();
        this.card = card;
        this.card25Percent = card25Percent;
    }

    @Test
    public void resize_boards_iterate_on_all_images() throws IOException {
        Board mockImage = mock(Board.class);
        List<Board> images = Arrays.asList(mockImage, mockImage, mockImage);
        doReturn(card.getInputStream().readAllBytes()).when(mockImage).boardImage();

        imageService.reziseBoards(images);

        verify(mockImage, times(3)).boardImage();
    }

    @Test
    public void resize_boards_return_new_byte_image_from_original_image() throws IOException {
        Board mockImage = mock(Board.class);
        byte[] image = card.getInputStream().readAllBytes();
        doReturn(image).when(mockImage).boardImage();
        List<Board> images = Arrays.asList(mockImage, mockImage, mockImage);

        List<Board> resultBoards = imageService.reziseBoards(images);

        assertThat(resultBoards).hasSameSizeAs(images);
        resultBoards.forEach(board -> {
            assertThat(board.boardImage()).isNotEqualTo(image);
        });
    }

    @Test
    public void resize_boards_should_return_same_image_at_25_percent_size() throws IOException {
        List<Board> images = Collections.singletonList(new Board(0L, new Sizing(1, 1), card.getInputStream().readAllBytes()));
        BufferedImage expectedImage = ImageIO.read(card25Percent.getInputStream());

        List<Board> resultBoards = imageService.reziseBoards(images);

        BufferedImage generatedImage = ImageIO.read(new ByteArrayInputStream(resultBoards.get(0).boardImage()));
        assertThat(generatedImage.getWidth()).isEqualTo(expectedImage.getWidth());
        assertThat(generatedImage.getHeight()).isEqualTo(expectedImage.getHeight());
    }
}