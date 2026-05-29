package model;

import java.math.BigDecimal;

public class RecipeDetail {
    private int recipeDetailId;
    private int recipeId;
    private int itemId;       // the ingredient
    private BigDecimal initialWeight;
    private BigDecimal finalWeight;
    private BigDecimal wastePercentage;
    private String unit;

    // transient (for line table display)
    private String itemCode;
    private String description;

    public RecipeDetail() {}

    public int getRecipeDetailId() { return recipeDetailId; }
    public void setRecipeDetailId(int recipeDetailId) { this.recipeDetailId = recipeDetailId; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public BigDecimal getInitialWeight() { return initialWeight; }
    public void setInitialWeight(BigDecimal initialWeight) { this.initialWeight = initialWeight; }

    public BigDecimal getFinalWeight() { return finalWeight; }
    public void setFinalWeight(BigDecimal finalWeight) { this.finalWeight = finalWeight; }

    public BigDecimal getWastePercentage() { return wastePercentage; }
    public void setWastePercentage(BigDecimal wastePercentage) { this.wastePercentage = wastePercentage; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
