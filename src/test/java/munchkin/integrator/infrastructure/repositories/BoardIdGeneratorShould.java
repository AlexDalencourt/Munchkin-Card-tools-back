package munchkin.integrator.infrastructure.repositories;

import munchkin.integrator.infrastructure.repositories.entities.BoardEntity;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.Test;

import java.util.zip.CRC32C;
import java.util.zip.Checksum;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BoardIdGeneratorShould {

    private BoardIdGenerator generator = new BoardIdGenerator();

    @Test
    public void extract_byte_stream_from_input_entity() {
        BoardEntity mockEntity = mock(BoardEntity.class);
        doReturn(new byte[0]).when(mockEntity).getImage();

        generator.generate(mock(SharedSessionContractImplementor.class), mockEntity);

        verify(mockEntity).getImage();
    }

    @Test
    public void return_checksum_from_byte_stream_from_input_entity() {
        BoardEntity mockEntity = mock(BoardEntity.class);
        byte[] stream = "inputStream".getBytes();
        doReturn(stream).when(mockEntity).getImage();

        Long checksum = (Long) generator.generate(mock(SharedSessionContractImplementor.class), mockEntity);

        Checksum checksumCalculator = new CRC32C();
        checksumCalculator.update(stream);
        assertThat(checksum).isEqualTo(checksumCalculator.getValue());
    }

}