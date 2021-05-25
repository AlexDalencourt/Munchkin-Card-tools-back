package munchkin.integrator.infrastructure.repositories.generators;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.Test;

import java.util.zip.CRC32C;
import java.util.zip.Checksum;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChecksumIdGeneratorShould {

    private final ChecksumIdGenerator generator = new ChecksumIdGenerator();

    @Test
    public void extract_byte_stream_from_input_entity() {
        ChecksumId mockEntity = mock(ChecksumId.class);
        doReturn(new byte[0]).when(mockEntity).fileToCheckSum();

        generator.generate(mock(SharedSessionContractImplementor.class), mockEntity);

        verify(mockEntity).fileToCheckSum();
    }

    @Test
    public void return_checksum_from_byte_stream_from_input_entity() {
        ChecksumId mockEntity = mock(ChecksumId.class);
        byte[] stream = "inputStream".getBytes();
        doReturn(stream).when(mockEntity).fileToCheckSum();

        Long checksum = (Long) generator.generate(mock(SharedSessionContractImplementor.class), mockEntity);

        Checksum checksumCalculator = new CRC32C();
        checksumCalculator.update(stream);
        assertThat(checksum).isEqualTo(checksumCalculator.getValue());
    }

}