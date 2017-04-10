package de.ktran.anno1404warenrechner.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class Logic {
    private final Game game;

    Logic(Game game) {
        this.game = game;
    }

    Map<Population, Integer> calculateAscensionRightsOccidental(final int totalBuildingCount) {
        Map<Population, Integer> res = new HashMap<>();

        final int beggars = game.getPopulation().get(Population.BEGGARS);
        final int envoys = game.getPopulation().get(Population.ENVOYS);

        int peasants = totalBuildingCount;
        int citizenships = 0;

        if (totalBuildingCount >= 12) { // 12* 8 = 96 > 90 (needed for chapel)
            citizenships = (int) Math.floor(0.8 * totalBuildingCount + (beggars / (40 - game.getBeggarPrinceBonus())));
            citizenships = Math.min(citizenships, totalBuildingCount);
            peasants = Math.max(totalBuildingCount - citizenships, 0);
        }

        int patrician_rights = 0;
        if (citizenships >= 24) { // 24 * 15 = 360 > 355 (needed for tavern)
            patrician_rights = (int) Math.floor(0.6 * citizenships + envoys / (110 - game.getEnvoysFavorBonus()));
            patrician_rights = Math.min(patrician_rights, citizenships);
            citizenships = Math.max(citizenships - patrician_rights, 0);
        }

        int noble_rights = 0;

        if (patrician_rights >= 48) { // 48 * 25 = 1200 > 1190 (needed for debtor's prison)
            noble_rights = (int) Math.floor(0.4 * patrician_rights);
            noble_rights = Math.min(noble_rights, patrician_rights);
            patrician_rights = Math.max(patrician_rights - noble_rights, 0);
        }

        res.put(Population.BEGGARS, beggars);
        res.put(Population.PEASANTS, peasants);
        res.put(Population.CITIZENS, citizenships);
        res.put(Population.PATRICIANS, patrician_rights);
        res.put(Population.NOBLEMEN, noble_rights);

        return res;
    }

    Map<Population, Integer> calculateAscensionRightsOriental(int totalBuildingCount) {
        final Map<Population, Integer> res = new HashMap<>();
        int envoys = 0;
        if (totalBuildingCount >= 30) { // 30*15 = 450 > 440 (required for mosque)
            envoys = (int) Math.floor(totalBuildingCount * 0.7);
        }

        res.put(Population.NOMADS, totalBuildingCount - envoys);
        res.put(Population.ENVOYS, envoys);

        return res;
    }

    private LinkedHashMap<Goods, Float> calculateNeeds() {
        final LinkedHashMap<Goods, Float> res = new LinkedHashMap<>();

        for (Population p : Population.values()) {
            for (Map.Entry<Goods, Float> e : p.needsPerMinute.entrySet()) {
                if (!res.containsKey(e.getKey())) {
                    res.put(e.getKey(), e.getValue() * game.getPopulation_100().get(p));
                } else {
                    float newVal = res.get(e.getKey()) + e.getValue() * game.getPopulation_100().get(p);
                    res.put(e.getKey(), newVal);
                }
            }
        }

        return res;
    }

    List<ProductionChain> calculateChains() {
        final List<ProductionChain> res = new ArrayList<>();

        final LinkedHashMap<Goods, Float> needs = calculateNeeds();

        for (Map.Entry<Goods, Float> e : needs.entrySet()) {
            final ProductionBuilding building = e.getKey().getProductionBuilding();
            final int chainAmount = (int) Math.ceil(e.getValue()/ building.getTonsPerMin(game.getBonus().get(building)));
            final float efficiency = e.getValue() / (chainAmount * building.getTonsPerMin(game.getBonus().get(building)));

            res.add(new ProductionChain(building, chainAmount, efficiency));
        }

        return res;
    }

    List<BuildingAlternative> calculateChainDependencies(ProductionChain chain) {
        final List<BuildingAlternative> res = new ArrayList<>();
        res.add(BuildingAlternative.of(chain));

        if (chain.getBuilding().getRequires() == null) {
            return res;
        }

        for (Map.Entry<Goods, Float> goods : chain.getBuilding().getRequires().entrySet()) {
            float neededTPM = goods.getValue() * chain.getChains();

            for (ProductionBuilding b : ProductionBuilding.values()) {
                if (!b.getProduces().equals(goods.getKey())) continue;
                final float prodTPM = b.getTonsPerMin(game.getBonus().get(b));

                final int requiredChains = (int) Math.ceil(
                        neededTPM / prodTPM
                );
                final float calcEfficiency = neededTPM / (requiredChains * prodTPM);

                res.addAll(calculateChainDependencies(ProductionChain.with(b, requiredChains, calcEfficiency)));
            }
        }

        return res;
    }
}
