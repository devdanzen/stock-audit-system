package model;

public class RecipeHeader {
    private int recipeId;
    private String recipeCode;
    private int itemId;
    private String itemClass;

    public RecipeHeader() {}

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public String getRecipeCode() { return recipeCode; }
    public void setRecipeCode(String recipeCode) { this.recipeCode = recipeCode; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemClass() { return itemClass; }
    public void setItemClass(String itemClass) { this.itemClass = itemClass; }
}
