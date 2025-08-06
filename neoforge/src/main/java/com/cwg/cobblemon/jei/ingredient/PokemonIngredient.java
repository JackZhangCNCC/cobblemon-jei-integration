package com.cwg.cobblemon.jei.ingredient;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * 宝可梦成分类，用于在 JEI 中表示宝可梦
 */
public class PokemonIngredient {
    
    public static final Codec<PokemonIngredient> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.STRING.fieldOf("species_name").forGetter(ingredient -> ingredient.species.getName())
        ).apply(instance, speciesName -> {
            // 根据名称查找 Species
            Species species = PokemonSpecies.INSTANCE.getByName(speciesName);
            return new PokemonIngredient(species);
        })
    );
    
    private final Species species;
    
    public PokemonIngredient(Species species) {
        this.species = species;
    }
    
    public Species getSpecies() {
        return species;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PokemonIngredient that = (PokemonIngredient) obj;
        return species != null ? species.equals(that.species) : that.species == null;
    }
    
    @Override
    public int hashCode() {
        return species != null ? species.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "PokemonIngredient{" +
                "species=" + (species != null ? species.getName() : "null") +
                '}';
    }
}
