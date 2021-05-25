package munchkin.integrator.infrastructure.repositories.generators;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

public class ChecksumIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        ChecksumId entity = (ChecksumId) o;
        Checksum calculator = new CRC32C();
        calculator.update(entity.fileToCheckSum());
        return calculator.getValue();
    }
}
