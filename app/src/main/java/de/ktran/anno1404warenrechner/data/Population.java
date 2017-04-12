package de.ktran.anno1404warenrechner.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Population {

    private final HashMap<PopulationType, Integer> population;

    static Population newInstance() {
        final HashMap<PopulationType, Integer> newMap = new HashMap<>();
        for (PopulationType p : PopulationType.values()) {
            newMap.put(p, 0);
        }
        return new Population(newMap);
    }

    private Population(HashMap<PopulationType, Integer> population) {
        this.population = population;
    }

    /**
     * @return Total population count
     */
    public int getPopulationCount() {
        int count = 0;
        for (int v : population.values()) count += v;
        return count;
    }

    /**
     * @param civ Civilization (Occidental, oriental)
     * @return Occidental or oriental population count
     */
    public int getPopulationCount(PopulationType.Civilization civ) {
        int count = 0;
        for (final Map.Entry<PopulationType, Integer> e : population.entrySet()) {
            if (e.getKey().getCivilization() == civ) count += e.getValue();
        }
        return count;
    }

    /**
     * @param populationType Population type
     * @return Population count of given type
     */
    public int getPopulationCount(PopulationType populationType) {
        return population.get(populationType);
    }

    public List<PopulationType> getHighestCivs() {
        List<PopulationType> res = new ArrayList<>();

        if (population.get(PopulationType.NOBLEMEN) > 0) {
            res.add(PopulationType.NOBLEMEN);
        } else if (population.get(PopulationType.PATRICIANS) > 0) {
            res.add(PopulationType.PATRICIANS);
        } else if (population.get(PopulationType.CITIZENS) > 0) {
            res.add(PopulationType.CITIZENS);
        } else if (population.get(PopulationType.PEASANTS) > 0) {
            res.add(PopulationType.PEASANTS);
        }

        if (population.get(PopulationType.ENVOYS) > 0) {
            res.add(PopulationType.ENVOYS);
        } else if (population.get(PopulationType.NOMADS) > 0) {
            res.add(PopulationType.NOMADS);
        }

        return res;
    }


    boolean setPopulationCount(PopulationType type, int count) {
        if (population.get(type) == count) return false;

        population.put(type, count);
        return true;
    }

    public int getHouseCount(PopulationType.Civilization civilization) {
        int count = 0;

        for (Map.Entry<PopulationType, Integer> e : population.entrySet()) {
            if (e.getKey().getCivilization() == civilization) count += e.getKey().getHouseCountByPopSize(e.getValue());
        }

        return count;
    }

    boolean setHouseCount(PopulationType type, int count) {
        final int populationCount = count * type.houseSize;

        if (population.get(type) == populationCount) return false;

        population.put(type, populationCount);
        return true;
    }
}
