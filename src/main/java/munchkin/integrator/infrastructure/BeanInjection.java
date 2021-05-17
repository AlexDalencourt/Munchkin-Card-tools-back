package munchkin.integrator.infrastructure;

import munchkin.integrator.domain.boards.UploadBoard;
import munchkin.integrator.infrastructure.repositories.BoardRepository;
import munchkin.integrator.infrastructure.services.ImageService;
import munchkin.integrator.infrastructure.services.UploadBoardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInjection {

    @Bean
    public ImageService imageService() {
        return new ImageService();
    }

    @Bean
    public UploadBoard boardUploadingService(BoardRepository boardRepository, ImageService imageService) {
        return new UploadBoardService(boardRepository, imageService);
    }
}
