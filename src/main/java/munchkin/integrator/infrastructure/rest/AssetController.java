package munchkin.integrator.infrastructure.rest;

import munchkin.integrator.domain.Type;
import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.Sizing;
import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.infrastructure.rest.responses.BoardResponseLight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("asset")
public class AssetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetController.class);

    private final UploadBoard boardUploadingService;

    @PostMapping("board")
    @ResponseStatus(CREATED)
    public void newBoad(@RequestParam MultipartFile file, @RequestParam int numberOfColumns, @RequestParam int numberOfLines, @RequestParam Type boardType) throws IOException {
        requireNonNull(file);
        if (ImageIO.read(file.getInputStream()) == null) {
            throw new InvalidMediaTypeException("Image file", file.getOriginalFilename());
        }
        if (numberOfColumns <= 0 || numberOfLines <= 0 || boardType == null) {
            throw new IllegalArgumentException("Number of columns and lines should be positive and superior to 0, and board type must be valid");
        }
        if (!boardUploadingService.uploadNewBoard(new Board(null, new Sizing(numberOfColumns, numberOfLines), file.getBytes()))) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("board")
    public List<BoardResponseLight> getAllBoard() {
        return boardUploadingService.getAllBoards().stream().map(BoardResponseLight::new).collect(Collectors.toList());
    }

    public AssetController(UploadBoard boardUploadingService) {
        this.boardUploadingService = requireNonNull(boardUploadingService);
    }
}
