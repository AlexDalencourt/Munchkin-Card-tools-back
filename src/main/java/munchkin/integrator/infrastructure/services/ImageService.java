package munchkin.integrator.infrastructure.services;

import munchkin.integrator.domain.boards.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class ImageService {

    public List<Board> reziseBoards(List<Board> boards, final int reduction) {
        return boards.stream().map(board -> {
            try {
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(board.boardImage()));
                BufferedImage resizedImage =
                        new BufferedImage(
                                getReductionSizeBy4InPixels(originalImage.getWidth(), reduction),
                                getReductionSizeBy4InPixels(originalImage.getHeight(), reduction),
                                BufferedImage.TYPE_INT_RGB
                        );
                resizeImage(originalImage, resizedImage, reduction);

                return generateResizedBoard(board, resizedImage);
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }).collect(Collectors.toList());

    }

    private void resizeImage(BufferedImage originalImage, BufferedImage resizedImage, int reduction) {
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(
                originalImage,
                0, 0,
                getReductionSizeBy4InPixels(originalImage.getWidth(), reduction),
                getReductionSizeBy4InPixels(originalImage.getHeight(), reduction),
                null
        );
        graphics2D.dispose();
    }

    private Board generateResizedBoard(Board originalBoard, BufferedImage resizedImage) throws IOException {
        ByteArrayOutputStream rezisedImageByteStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", rezisedImageByteStream);
        return new Board(originalBoard, rezisedImageByteStream.toByteArray());
    }

    private int getReductionSizeBy4InPixels(int originalSize, int reduction) {
        return BigDecimal.valueOf(originalSize).divide(BigDecimal.valueOf(reduction), RoundingMode.DOWN).intValue();
    }

}
