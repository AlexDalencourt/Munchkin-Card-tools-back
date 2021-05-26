package munchkin.integrator.infrastructure.rest;

import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.domain.card.Type;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("cards")
public class CardController {

    private final UploadBoard boardUploadingService;

    @GetMapping("types")
    public Type[] getAllCardTypes() {
        return Type.values();
    }

    @PutMapping("/crop")
    @ResponseStatus(CREATED)
    public void cropBoard(@RequestParam Long boardId) {
        if (boardId == null) {
            throw new IllegalArgumentException("boardId null");
        }
        boardUploadingService.cropBoard(boardId);
    }

    public CardController(UploadBoard uploadBoard) {
        this.boardUploadingService = requireNonNull(uploadBoard);
    }
}
