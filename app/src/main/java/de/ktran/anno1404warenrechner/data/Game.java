package de.ktran.anno1404warenrechner.data;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final int id;
    private String name;
    private Date creationDate;
    private Date lastOpened;
    private final HashMap<Population, Integer> population;
    public final HashMap<ProductionBuilding, Integer> bonus;
    private final HashMap<ProductionBuilding, Integer> builtBuildings;

    private int beggarPrince;
    private int envoysFavour;

    public static Game newGame(int id, String name) {
        final HashMap<Population, Integer> population = new HashMap<>();

        for (Population p : Population.values()) {
            population.put(p, 0);
        }

        final HashMap<ProductionBuilding, Integer> bonus = new HashMap<>();
        final HashMap<ProductionBuilding, Integer> builtBuildings = new HashMap<>();
        for (ProductionBuilding p : ProductionBuilding.values()) {
            bonus.put(p, 0);
            builtBuildings.put(p, 0);
        }

        return new Game(
                id, name, new Date(), new Date(), population, bonus, builtBuildings, 0, 0
        );
    }

    private Game(int id, String name, Date creationDate, Date last_opened, HashMap<Population, Integer> population, HashMap<ProductionBuilding, Integer> bonus, HashMap<ProductionBuilding, Integer> builtBuildings, int beggarPrince, int envoysFavour) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.lastOpened = last_opened;
        this.population = population;
        this.bonus = bonus;
        this.builtBuildings = builtBuildings;

        this.beggarPrince = beggarPrince;
        this.envoysFavour = envoysFavour;
    }

    public void setLastOpened(Date lastOpened) {
        this.lastOpened = lastOpened;
    }

    public Date getLastOpened() {
        return lastOpened;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public HashMap<ProductionBuilding, Integer> getBonus() {
        return bonus;
    }

    public HashMap<ProductionBuilding, Integer> getBuiltBuildings() {
        return builtBuildings;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return lastOpened;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Population, Integer> getPopulation() {
        return population;
    }

    public HashMap<Population, Float> getPopulation_100() {
        final HashMap<Population, Float> res = new HashMap<>();

        for (Map.Entry<Population, Integer> p : population.entrySet()) {
            res.put(
                    p.getKey(), p.getValue() / 100f
            );
        }

        return res;
    }

    public void setPopulation(Population p, int val) {
        population.put(p, val);
    }

    public int getPopCount() {
        int popCount = 0;
        for (int count : population.values()) {
            popCount += count;
        }

        return popCount;
    }

    public List<Population> getHighestCivs() {
        List<Population> res = new ArrayList<>();

        if (population.get(Population.NOBLEMEN) > 0) {
            res.add(Population.NOBLEMEN);
        } else if (population.get(Population.PATRICIANS) > 0) {
            res.add(Population.PATRICIANS);
        } else if (population.get(Population.CITIZENS) > 0) {
            res.add(Population.CITIZENS);
        } else if (population.get(Population.PEASANTS) > 0) {
            res.add(Population.PEASANTS);
        }

        if (population.get(Population.ENVOYS) > 0) {
            res.add(Population.ENVOYS);
        } else if (population.get(Population.NOMADS) > 0) {
            res.add(Population.NOMADS);
        }

        return res;
    }

    public int getPopCountOccidental() {
        int res = 0;
        for (Population p : Population.values()) {
            if (p != Population.BEGGARS && p != Population.NOMADS && p != Population.ENVOYS) {
                res += getPopulation().get(p);
            }
        }

        return res;
    }

    public int getPopCountOriental() {
        int res = 0;
        res += population.get(Population.NOMADS);
        res += population.get(Population.ENVOYS);
        return res;
    }

    public int getHouseCountOccidental() {
        int count = 0;

        for (Population p : Population.values()) {
            if (p == Population.BEGGARS || p == Population.NOMADS || p == Population.ENVOYS) continue;
             count += p.getHouseCountByPopSize(getPopulation().get(p));
        }
        return count;
    }

    public int getHouseCountOriental() {
        int count = 0;

        count += Population.NOMADS.getHouseCountByPopSize(getPopulation().get(Population.NOMADS));
        count += Population.ENVOYS.getHouseCountByPopSize(getPopulation().get(Population.ENVOYS));

        return count;
    }
    
    public int getBeggarPrinceBonus() {
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

    public int getEnvoysFavorBonus() {
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

    public int getBeggarPrince() {
        return beggarPrince;
    }

    public void setBeggarPrince(int beggarPrince) {
        this.beggarPrince = beggarPrince;
    }

    public int getEnvoysFavour() {
        return envoysFavour;
    }

    public void setEnvoysFavour(int envoysFavour) {
        this.envoysFavour = envoysFavour;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Game)) return false;
        return ((Game) obj).getId() == getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
