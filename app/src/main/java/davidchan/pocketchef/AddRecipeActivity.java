package davidchan.pocketchef;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    public static final int ADD_INGREDIENTS = 1;
    public static final int ADD_INSTRUCTIONS = 2;

    List<Ingredient> ingredients;
    List<Instruction> instructions;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        ingredients = new ArrayList<>();
        instructions = new ArrayList<>();

        final Bundle recipeData = getIntent().getExtras().getBundle("recipeData");
        recipes = (List<Recipe>) recipeData.get("recipes");

        final EditText recipeName = (EditText) findViewById(R.id.recipe_name);
        Button addIngredients = (Button) findViewById(R.id.add_ingredients_go);
        Button addInstructions = (Button) findViewById(R.id.add_instructions_go);
        Button done = (Button) findViewById(R.id.done_adding);

        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIngredientsActivity = new Intent(v.getContext(), AddIngredientsActivity.class);
                Bundle ingredientData = new Bundle();
                ingredientData.putSerializable("ingredients", (Serializable) ingredients);
                addIngredientsActivity.putExtra("ingredientData", ingredientData);
                startActivityForResult(addIngredientsActivity, ADD_INGREDIENTS);
            }
        });

        addInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addInstructionActivity = new Intent(v.getContext(), AddInstructionsActivity.class);
                Bundle instructionData = new Bundle();
                instructionData.putSerializable("instructions", (Serializable) instructions);
                addInstructionActivity.putExtra("instructionData", instructionData);
                startActivityForResult(addInstructionActivity, ADD_INSTRUCTIONS);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredients.size() == 0 || instructions.size() == 0) {
                    if (ingredients.size() == 0 && instructions.size() == 0) {
                        Toast.makeText(v.getContext(), "Please enter some ingredients and instructions!", Toast.LENGTH_LONG).show();
                    }
                    else if (ingredients.size() == 0 && instructions.size() != 0) {
                        Toast.makeText(v.getContext(), "Please enter some ingredients!", Toast.LENGTH_LONG).show();
                    }
                    else if (ingredients.size() != 0 && instructions.size() == 0) {
                        Toast.makeText(v.getContext(), "Please enter some instructions!", Toast.LENGTH_LONG).show();
                    }
                }
                else if (recipeName.getText().toString().length() == 0) {
                    Toast.makeText(v.getContext(), "Please enter a name for your recipe!", Toast.LENGTH_LONG).show();
                }
                else {
                    Recipe newRecipe = new Recipe(recipeName.getText().toString(), ingredients, instructions, 0);
                    recipes.add(newRecipe);
                    Intent data = new Intent();
                    Bundle recipeData = new Bundle();
                    recipeData.putSerializable("recipes", (Serializable) recipes);
                    data.putExtra("recipeData", recipeData);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ADD_INGREDIENTS:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle temp = data.getExtras().getBundle("ingredientsData");
                    ingredients = (List<Ingredient>) temp.get("ingredients");
                }
                break;
            case ADD_INSTRUCTIONS:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle temp = data.getExtras().getBundle("instructionsData");
                    instructions = (List<Instruction>) temp.get("instructions");
                }
                break;
            default: break;
        }
    }
}
