package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.domain.asset.Asset;
import munchkin.integrator.domain.asset.AssetIndex;
import munchkin.integrator.domain.asset.Image;
import munchkin.integrator.domain.card.Card;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardPositionEntityShould {

    @Test
    public void createAnEntityFromCard() {
        Card card = new Card(new Asset(new Image("Image".getBytes()), new AssetIndex(0, 1)));

        CardPositionEntity cardEntity = new CardPositionEntity(card);

        assertThat(cardEntity.getCard().getImage()).isEqualTo(card.cardAsset().image().image());
        assertThat(cardEntity.getColumn()).isEqualTo(card.cardAsset().index().column());
        assertThat(cardEntity.getLine()).isEqualTo(card.cardAsset().index().line());
    }
}