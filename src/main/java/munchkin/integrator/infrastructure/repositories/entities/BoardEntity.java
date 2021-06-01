package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.domain.asset.Image;
import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.Sizing;
import munchkin.integrator.domain.card.Card;
import munchkin.integrator.infrastructure.repositories.generators.ChecksumId;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity(name = "Board")
public class BoardEntity implements ChecksumId {
    @Id
    @GeneratedValue(generator = "checksum-generator")
    @GenericGenerator(name = "checksum-generator", strategy = "munchkin.integrator.infrastructure.repositories.generators.ChecksumIdGenerator")
    private Long checksum;

    @Column(columnDefinition = "BLOB")
    private byte[] image;

    private int columns;

    private int lines;

    @OneToMany(mappedBy = "board")
    private final List<CardPositionEntity> cards = new ArrayList<>();

    public BoardEntity() {
    }

    public BoardEntity(Long checksum, byte[] image, int columns, int lines, List<CardPositionEntity> cards) {
        this.checksum = checksum;
        this.image = image;
        this.columns = columns;
        this.lines = lines;
        if (cards != null) {
            this.cards.addAll(cards);
        }
    }

    public BoardEntity(Board boardToSave) {
        requireNonNull(boardToSave);
        requireNonNull(boardToSave.sizing());
        this.checksum = boardToSave.boardId();
        this.image = boardToSave.boardImage().image();
        this.columns = boardToSave.sizing().numberOfColumns();
        this.lines = boardToSave.sizing().numberOfLines();
        for (Card card : boardToSave.cards()) {
            this.cards.add(new CardPositionEntity(card));
        }
    }

    public Long getChecksum() {
        return checksum;
    }

    public byte[] getImage() {
        return image;
    }

    public int getColumns() {
        return columns;
    }

    public int getLines() {
        return lines;
    }

    public List<CardPositionEntity> getCards() {
        return cards;
    }

    public Board toBoard() {
        return new Board(checksum, new Sizing(columns, lines), new Image(image), new ArrayList<Card>());
    }

    @Override
    public byte[] fileToCheckSum() {
        return this.getImage();
    }
}
