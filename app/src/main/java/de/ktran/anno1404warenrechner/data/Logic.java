package de.ktran.anno1404warenrechner.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ktran.anno1404warenrechner.helpers.Pair;

class Logic {
    private final Game game;

    Logic(Game game) {
        this.game = game;
    }

    Map<PopulationType, Integer> calculateAscensionRightsOccidental(final int totalBuildingCount, final int maxPop) {
        final Map<PopulationType, Integer> res = new HashMap<>();

        final int beggars = game.population().getPopulationCount(PopulationType.Civilization.BEGGARS);
        final int envoys = game.population().getPopulationCount(PopulationType.ENVOYS);

        int peasants = totalBuildingCount;
        int citizenships = 0;

        if (maxPop >= 1 && totalBuildingCount >= 12) { // 12* 8 = 96 > 90 (needed for chapel)
            citizenships = (int) Math.floor(0.8 * totalBuildingCount + (beggars / (40 - game.getBeggarPrinceBonus())));
            citizenships = Math.min(citizenships, totalBuildingCount);
            peasants = Math.max(totalBuildingCount - citizenships, 0);
        }

        int patrician_rights = 0;
        if (maxPop >= 2 && citizenships >= 24) { // 24 * 15 = 360 > 355 (needed for tavern)
            patrician_rights = (int) Math.floor(0.6 * citizenships + envoys / (110 - game.getEnvoysFavorBonus()));
            patrician_rights = Math.min(patrician_rights, citizenships);
            citizenships = Math.max(citizenships - patrician_rights, 0);
        }

        int noble_rights = 0;

        if (maxPop >= 3 &&patrician_rights >= 48) { // 48 * 25 = 1200 > 1190 (needed for debtor's prison)
            noble_rights = (int) Math.floor(0.4 * patrician_rights);
            noble_rights = Math.min(noble_rights, patrician_rights);
            patrician_rights = Math.max(patrician_rights - noble_rights, 0);
        }

        res.put(PopulationType.PEASANTS, peasants);
        res.put(PopulationType.CITIZENS, citizenships);
        res.put(PopulationType.PATRICIANS, patrician_rights);
        res.put(PopulationType.NOBLEMEN, noble_rights);

        return res;
    }

    Map<PopulationType, Integer> calculateAscensionRightsOriental(final int totalBuildingCount, final int maxPop) {
        final Map<PopulationType, Integer> res = new HashMap<>();
        int envoys = 0;
        if (maxPop >= 1 && totalBuildingCount >= 30) { // 30*15 = 450 > 440 (required for mosque)
            envoys = (int) Math.floor(totalBuildingCount * 0.7);
        }

        res.put(PopulationType.NOMADS, totalBuildingCount - envoys);
        res.put(PopulationType.ENVOYS, envoys);

        return res;
    }


    private float calculateConsumption(Goods goods) {
        if (goods.getType() != Goods.Type.NEEDS) throw new IllegalArgumentException("Given argument is not needed by the population.");

        float tpm = 0;

        for (final PopulationType p : PopulationType.values()) {
            if (p.needsPerMinute.keySet().contains(goods)) {
                float pop = (float) game.population().getPopulationCount(p) / 100f;
                tpm += p.needsPerMinute.get(goods) * pop;
            }
        }

        return tpm;
    }

    private ProductionChain calculateNeedsChain(Goods goods) {
        if (goods.getType() != Goods.Type.NEEDS) throw new IllegalArgumentException("Given argument is not needed by the population.");

        float consumptionTPM = calculateConsumption(goods);
        final ProductionBuilding building = goods.getProductionBuilding();
        final int chainCount = (int) Math.ceil(consumptionTPM / building.getTonsPerMin(game.getBonus(building)));
        final float efficiency = consumptionTPM / (chainCount * building.getTonsPerMin(game.getBonus(building)));

        return ProductionChain.with(building, chainCount, efficiency);
    }

    List<ProductionChain> calculateAllNeedsChains() {
        final List<ProductionChain> results = new ArrayList<>();

        for (final Goods g : Goods.getNeeds()) {
            results.add(calculateNeedsChain(g));
        }

        return results;
    }

    List<BuildingAlternative> calculateChainWithDependencies(Goods goods) {
        final List<BuildingAlternative> res = new ArrayList<>();

        final ProductionChain rootChain;

        if (goods.getType() == Goods.Type.NEEDS) {
            rootChain = calculateNeedsChain(goods);
        } else if (goods.getType() == Goods.Type.MATERIAL) {
            rootChain = new ProductionChain(goods.getProductionBuilding(), game.getMaterialProductionCount(goods.getProductionBuilding()), 1f);
        } else throw new IllegalArgumentException("Given root chain would be intermediary.");

        res.add(BuildingAlternative.of(rootChain));

        if (rootChain.getBuilding().getRequires() == null) {
            return res;
        }

        res.addAll(calculateDependenciesOf(rootChain));
        return res;
    }

    private Pair<Integer, Float> calculateBuildingsWithTPM(ProductionBuilding building, float tpm) {
        final float prodTPM = building.getTonsPerMin(game.getBonus(building));

        final int chains = (int) Math.ceil(tpm / prodTPM);
        final float efficiency = tpm / (chains * prodTPM);

        return Pair.of(chains, efficiency);
    }

    private List<BuildingAlternative> calculateDependenciesOf(ProductionChain chain) {
        final List<BuildingAlternative> result = new ArrayList<>();

        if (chain.getBuilding().getRequires() == null) return result;

        for (Map.Entry<Goods, Float> entry : chain.getBuilding().getRequires().entrySet()) {
            if (entry.getKey() == Goods.INTERMEDIARY_COAL) {
                final BuildingAlternative ba = new BuildingAlternative(Goods.INTERMEDIARY_COAL);

                // Charcoal burner
                final Pair<Integer, Float> charcoalValues = calculateBuildingsWithTPM(ProductionBuilding.CHARCOAL_BURNER, entry.getValue() * chain.getChains());
                ba.addBuildingAlternative(new ProductionChain(ProductionBuilding.CHARCOAL_BURNER, charcoalValues.getLeft(), charcoalValues.getRight()));

                // Coal mine
                final Pair<Integer, Float> coalMineValues = calculateBuildingsWithTPM(ProductionBuilding.COAL_MINE, entry.getValue() * chain.getChains());
                ba.addBuildingAlternative(new ProductionChain(ProductionBuilding.COAL_MINE, coalMineValues.getLeft(), coalMineValues.getRight()));
            }

            //@WORKAROUND: performance tweak, we assume that only coal has two requirements
            final ProductionBuilding building = entry.getKey().getProductionBuilding();
            final Pair<Integer, Float> values = calculateBuildingsWithTPM(building, chain.getChains() * entry.getValue());
            final ProductionChain reqChain = ProductionChain.with(building, values.getLeft(), values.getRight());

            result.add(BuildingAlternative.of(reqChain));

            result.addAll(calculateDependenciesOf(reqChain));
        }

        return result;
    }
}
