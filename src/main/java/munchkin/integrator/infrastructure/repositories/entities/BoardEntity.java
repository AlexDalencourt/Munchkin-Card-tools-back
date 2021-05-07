package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.domain.boards.Board;
import munchkin.integrator.domain.boards.Sizing;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static java.util.Objects.requireNonNull;

@Entity(name = "Board")
public class BoardEntity {
    @Id
    @GeneratedValue(generator = "checksum-generator")
    @GenericGenerator(name = "checksum-generator", strategy = "munchkin.integrator.infrastructure.repositories.BoardIdGenerator")
    private Long checksum;

    @Column(columnDefinition = "BLOB")
    private byte[] image;

    private int columns;

    private int lines;

    public BoardEntity() {
    }

    public BoardEntity(Long checksum, byte[] image, int columns, int lines) {
        this.checksum = checksum;
        this.image = image;
        this.columns = columns;
        this.lines = lines;
    }

    public BoardEntity(Board boardToSave) {
        requireNonNull(boardToSave);
        requireNonNull(boardToSave.sizing());
        this.image = boardToSave.boardImage();
        this.columns = boardToSave.sizing().numberOfColumns();
        this.lines = boardToSave.sizing().numberOfLines();
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

    public Board toBoard() {
        return new Board(checksum, new Sizing(columns, lines), image);
    }
}
