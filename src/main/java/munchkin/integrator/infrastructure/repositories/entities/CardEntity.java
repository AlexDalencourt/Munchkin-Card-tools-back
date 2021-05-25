package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.infrastructure.repositories.generators.ChecksumId;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Card")
public class CardEntity implements ChecksumId {

    @Id
    @GeneratedValue(generator = "checksum-generator")
    @GenericGenerator(name = "checksum-generator", strategy = "munchkin.integrator.infrastructure.repositories.generators.ChecksumIdGenerator")
    private Long checksum;

    @Column(columnDefinition = "BLOB")
    private byte[] image;

    @Column(columnDefinition = "integer default 0")
    private int version;

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
