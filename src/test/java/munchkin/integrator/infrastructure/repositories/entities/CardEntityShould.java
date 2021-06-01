package munchkin.integrator.infrastructure.repositories.entities;

import munchkin.integrator.domain.asset.Asset;
import munchkin.integrator.domain.asset.AssetIndex;
import munchkin.integrator.domain.asset.Image;
import munchkin.integrator.domain.card.Card;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardEntityShould {

    @Test
    public void mappedEntityFromCard() {
        Card card = new Card(new Asset(new Image("IMAGE".getBytes()), new AssetIndex(0, 2)));

        CardEntity cardEntity = new CardEntity(card);

        assertThat(cardEntity.getImage()).isEqualTo(card.cardAsset().image().image());
    }
}
