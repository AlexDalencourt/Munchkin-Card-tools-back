package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.domain.card.Card;
import munchkin.integrator.infrastructure.repositories.generators.ChecksumId;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;

import static java.util.Objects.requireNonNull;

@Entity(name = "Card")
public class CardEntity implements ChecksumId, Serializable {

    @Serial
    private static final long serialVersionUID = -7299753468531526190L;

    @Id
    @GeneratedValue(generator = "checksum-generator")
    @GenericGenerator(name = "checksum-generator", strategy = "munchkin.integrator.infrastructure.repositories.generators.ChecksumIdGenerator")
    private Long checksum;

    @Column(columnDefinition = "BLOB")
    private byte[] image;

    @Column(columnDefinition = "integer default 0")
    private Integer version;

    public CardEntity() {
    }

    public CardEntity(Card card) {
        requireNonNull(card);
        this.image = card.cardAsset().image().image();
    }

    public Long getChecksum() {
        return checksum;
    }

    public void setChecksum(Long checksum) {
        this.checksum = checksum;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public byte[] fileToCheckSum() {
        return new byte[0];
    }
}
