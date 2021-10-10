package org.ecd3.samples.shoppingcard.monotonic.insertanddelete.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests whether the processing of shopping card actions coming out of order
 * are correctly processed..
 */
public class ShoppingCardActionsProcessingTest {

    private static final Logger logger = LogManager.getLogger(ShoppingCardActionsProcessingTest.class);

    private Customer customer;

    private List<Product> products;

    private List<ShoppingCardAction> shoppingCardActions;

    private List<ShoppingCardAction> actionProcessingOrder1;

    private List<ShoppingCardAction> actionProcessingOrder2;


    @BeforeEach
    public void setup() {
        customer = new Customer(UUID.randomUUID(), "Susanne", "Braun");

        products = new ArrayList<>(3);
        products.add(createProduct("Rose Leonardo da Vinci", 25.99));
        products.add(createProduct("Lupine - Russel's Hybrids", 15.95));
        products.add(createProduct("Foxgloves Mix", 10.95));

        shoppingCardActions = new ArrayList<>(4);
        shoppingCardActions.add(new AddItemAction(UUID.randomUUID(), products.get(0), customer.getId()));
        shoppingCardActions.add(new AddItemAction(UUID.randomUUID(), products.get(1), customer.getId()));
        shoppingCardActions.add(new AddItemAction(UUID.randomUUID(), products.get(2), customer.getId()));
        shoppingCardActions.add(new AddItemAction(UUID.randomUUID(), products.get(0), customer.getId()));
        shoppingCardActions.add(new DeleteItemAction(UUID.randomUUID(), products.get(0), customer.getId()));

        actionProcessingOrder1 = new ArrayList<>(4);
        actionProcessingOrder1.add(shoppingCardActions.get(0)); // add "Rose Leonardo da Vinci"
        actionProcessingOrder1.add(shoppingCardActions.get(1)); // add "Lupine - Russel's Hybrids"
        actionProcessingOrder1.add(shoppingCardActions.get(4)); // delete "Rose Leonardo da Vinci"
        actionProcessingOrder1.add(shoppingCardActions.get(2)); // add "Foxgloves Mix"


        actionProcessingOrder2 = new ArrayList<>(4);
        actionProcessingOrder2.add(shoppingCardActions.get(1)); // add "Lupine - Russel's Hybrids"
        actionProcessingOrder2.add(shoppingCardActions.get(4)); // delete "Rose Leonardo da Vinci"
        actionProcessingOrder2.add(shoppingCardActions.get(2)); // add "Foxgloves Mix"
        actionProcessingOrder2.add(shoppingCardActions.get(0)); // add "Rose Leonardo da Vinci"
    }

    @Test
    public void resultShouldBeTheSameIndependentOfProcessingOrder() {
        ShoppingCardService shoppingCardService = ShoppingCardService.getInstance();

        ShoppingCard shoppingCard = createNewShoppingCard();
        logger.info("\t\t-----------------------------------------------------");
        logger.info("\t\tProcessing first ordering of shopping card actions...".toUpperCase());
        for(ShoppingCardAction action: actionProcessingOrder1) {
            logger.info("\t\t"+action.getClass().getSimpleName()+" "+action.getProduct().getName());
            shoppingCardService.handleShoppingCardAction(action);
        }
        logger.info("\t\t-----------------------------------------------------");

        Set<LineItem> shoppingCardItems1 = shoppingCard.getLineItems();
        logShoppingCardLineItems("1", shoppingCardItems1);

        shoppingCard = createNewShoppingCard();
        logger.info("\t\t-----------------------------------------------------");
        logger.info("\t\tProcessing second ordering of shopping card actions...".toUpperCase());
        for(ShoppingCardAction action: actionProcessingOrder2) {
            logger.info("\t\t"+action.getClass().getSimpleName()+" "+action.getProduct().getName());
            shoppingCardService.handleShoppingCardAction(action);
        }
        logger.info("\t\t-----------------------------------------------------");

        Set<LineItem> shoppingCardItems2 = shoppingCard.getLineItems();
        logShoppingCardLineItems("2", shoppingCardItems2);

        // assert that both cards contain the same number of products
        assertEquals(shoppingCardItems1, shoppingCardItems2);

        // assert that both cards have equal amounts of products
        for(LineItem lineItem: shoppingCardItems1) {
            LineItem otherLineLineItem = shoppingCardItems2.stream().filter(lineItem::equals).findFirst()
                .orElse(null);
            assertNotNull(otherLineLineItem);
            assertEquals(lineItem.getNumber(), otherLineLineItem.getNumber());
        }
    }

    private ShoppingCard createNewShoppingCard() {
        ShoppingCard shoppingCard = new ShoppingCard(UUID.randomUUID(), customer.getId());
        ShoppingCardRepository.getInstance().insert(shoppingCard);
        return shoppingCard;
    }

    private void logShoppingCardLineItems(String shoppingCardName, Set<LineItem> shoppingCardItems) {
        logger.info("\t\t-----------------------------------------------------");
        logger.info("\t\tThe following items are in shopping card ".toUpperCase()+shoppingCardName+": ");
        for(LineItem item: shoppingCardItems) {
            logger.info("\t\t"+item.getNumber()+"x - "+item.getName());
        }
        logger.info("\t\t-----------------------------------------------------");
    }

    private Product createProduct(String name, double price) {
        Money priceInEuro = Money.of(price, "EUR");
        return new Product(UUID.randomUUID().toString(), name, priceInEuro);
    }
}
