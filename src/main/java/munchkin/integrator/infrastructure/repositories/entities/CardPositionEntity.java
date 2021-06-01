package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.domain.card.Card;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

import static java.util.Objects.requireNonNull;

@Entity(name = "CardPositionInBoard")
public class CardPositionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -8966850552082564611L;
    //    @Id
//    private Long cardId;
    @Id
    @MapsId
    @OneToOne
    private CardEntity card;

    private int column;

    private int line;


    @ManyToOne
    private BoardEntity board;

    public CardPositionEntity() {
    }

    public CardPositionEntity(Card card) {
        requireNonNull(card);
        this.column = card.cardAsset().index().column();
        this.line = card.cardAsset().index().line();
        this.card = new CardEntity(card);
    }

//    public Long getCardId() {
//        return cardId;
//    }
//
//    public void setCardId(Long cardId) {
//        this.cardId = cardId;
//    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public BoardEntity getBoard() {
        return board;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    public CardEntity getCard() {
        return card;
    }

    public void setCard(CardEntity card) {
        this.card = card;
    }
}
