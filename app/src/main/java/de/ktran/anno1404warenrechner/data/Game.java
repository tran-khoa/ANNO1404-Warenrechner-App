package de.ktran.anno1404warenrechner.data;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Stores all data related to a game
 */
public class Game {

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd. MMMM yyyy - HH:mm", Locale.getDefault());

    /**
     * Unique game id
     */
    private final int id;

    /**
     * Game title
     */
    private String name;

    /**
     * Date and time when the game data was last opened (read + write)
     */
    private Date lastOpened;

    /**
     * Population object
     */

    private final Population population;

    /**
     * Production productivity increase through items (0-3)
     */
    private final HashMap<ProductionBuilding, Integer> bonus;

    /**
     * Number of production buildings of goods which are not consumed by the population.
     */
    private HashMap<ProductionBuilding, Integer> materialProduction = new HashMap<>();

    /**
     * Attainment rank for "Beggar Prince", influences ascension rights
     */
    private int beggarPrince;

    /**
     * Attainment rank for "The Envoys' Favour", influences ascension rights
     */
    private int envoysFavour;

    /**
     * Preferred constructor for creation of new games
     *
     * @param id unique id
     * @param name game name
     * @return game instance
     */
    static Game newGame(int id, String name) {
        final HashMap<ProductionBuilding, Integer> bonus = new HashMap<>();
        for (ProductionBuilding p : ProductionBuilding.values()) {
            bonus.put(p, 0);
        }

        final HashMap<ProductionBuilding, Integer> otherGoods = new HashMap<>();
        for (Goods g : Goods.getMaterials()) {
            otherGoods.put(g.getProductionBuilding(), 0);
        }

        return new Game(
                id, name, new Date(), Population.newInstance(), bonus, 0, 0, otherGoods
        );
    }

    private Game(int id, String name, Date last_opened, Population population,
                 HashMap<ProductionBuilding, Integer> bonus, int beggarPrince, int envoysFavour, HashMap<ProductionBuilding, Integer> materialProduction) {
        this.id = id;
        this.name = name;
        this.lastOpened = last_opened;
        this.population = population;
        this.bonus = bonus;

        this.beggarPrince = beggarPrince;
        this.envoysFavour = envoysFavour;
        this.materialProduction = materialProduction;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    Date getLastOpened() {
        return lastOpened;
    }

    public String getLastOpenedFormatted() {
        return DATE_FORMAT.format(lastOpened);
    }

    /**
     * Updates last_opened attribute with the current date and time
     */
    void gameTouched() {
        lastOpened = new Date();
    }

    public Population population() {
        return population;
    }

    public int getBonus(ProductionBuilding building) {
        return bonus.get(building);
    }

    boolean setBonus(ProductionBuilding building, int value) {
        if (getBonus(building) == value) return false;

        this.bonus.put(building, value);
        return true;
    }

    public int getMaterialProductionCount(ProductionBuilding building) {
        return materialProduction.get(building);
    }

    boolean setOtherGoods(ProductionBuilding building, int amount) {
        if (materialProduction.get(building) == amount) return false;

        materialProduction.put(building, amount);
        return true;
    }

    public int getBeggarPrince() {
        return beggarPrince;
    }

    void setBeggarPrince(int beggarPrince) {
        this.beggarPrince = beggarPrince;
    }

    public int getEnvoysFavour() {
        return envoysFavour;
    }

    void setEnvoysFavour(int envoysFavour) {
        this.envoysFavour = envoysFavour;
    }

    int getBeggarPrinceBonus() {
        switch (getBeggarPrince()) {
            case 0:
                return 0;
            case 1:
                return 3;
            case 2:
                return 6;
            case 3:
                return 10;
            default:
                throw new IllegalStateException("Invalid beggar prince entry");
        }
    }

    int getEnvoysFavorBonus() {
        switch (getEnvoysFavour()) {
            case 0:
                return 0;
            case 1:
                return 5;
            case 2:
                return 10;
            case 3:
                return 20;
            default:
                throw new IllegalArgumentException("Invalid envoys favour bonus entry");
        }
    }



    @Override
    public boolean equals(Object obj) {
        return obj instanceof Game && ((Game) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
