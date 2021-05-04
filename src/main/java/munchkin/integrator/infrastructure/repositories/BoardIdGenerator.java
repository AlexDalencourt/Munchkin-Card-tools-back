package munchkin.integrator.infrastructure.repositories;

import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

public class BoardIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        BoardEntity entity = (BoardEntity) o;
        Checksum calculator = new CRC32C();
        calculator.update(entity.getImage());
        return calculator.getValue();
    }
}
