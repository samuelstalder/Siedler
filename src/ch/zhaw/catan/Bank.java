package ch.zhaw.catan;

/**
 * The Bank is a place where you can trade resources in a 4:1 ratio.
 * You need to offer 4 of any resource to get 1 of any resource back.
 * You can trade every resource as long as the Bank has it in it's own stock.
 * The bank does not check if the player who wants to trade has enough resources
 *
 * @author Joel Plambeck
 * @version 06.12.2019
 */
public class Bank {
    public final static int OFFER_AMOUNT_RATIO = 4;
    public final static int RETURN_AMOUNT_RATIO = 1;
    private static ResourceStock resourceStock;

    /**
     * Creates a bank with the initial resources defined in {@link Config#INITIAL_RESOURCE_CARDS_BANK}.
     * The resources are stored in a {@link ResourceStock}
     */
    public Bank(){
        resourceStock = new ResourceStock(Config.INITIAL_RESOURCE_CARDS_BANK);
    }

    /**
     * Allows you to trade with the bank in a {@link #OFFER_AMOUNT_RATIO}:{@link #RETURN_AMOUNT_RATIO} ratio.
     * @param offer the resource you offer
     * @param want the resource you want to get back
     * @return whether the trade was successful
     */
    public boolean trade(Config.Resource offer, Config.Resource want){
        boolean result = resourceStock.remove(want, RETURN_AMOUNT_RATIO);
        if(result){
            resourceStock.add(offer, OFFER_AMOUNT_RATIO);
        }
        return result;
    }
    
    /**
     * Returns the resourceStock of the bank
     * @return the stock of the bank
     */
    public ResourceStock getResourceStock() {
        return resourceStock;
    }
}
