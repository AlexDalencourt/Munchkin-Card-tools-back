package munchkin.integrator.infrastructure;

import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.infrastructure.repositories.BoardRepository;
import munchkin.integrator.infrastructure.services.UploadBoardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInjection {

    @Bean
    public UploadBoard boardUploadingService(BoardRepository boardRepository) {
        return new UploadBoardService(boardRepository, null);
    }
}
