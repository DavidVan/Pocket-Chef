package davidchan.pocketchef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Ryan on 9/30/2016.
 */

public class CookingStartActivity extends Activity{
    Bundle recipe;
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_cooking_start);

        Intent received = getIntent();
        recipe= received.getExtras();
        String recipeName = recipe.getString("recipeName");
        ArrayList<String> ingredientList =recipe.getStringArrayList("ingredients");
        ArrayAdapter<String> addToView = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView recipeList = (ListView) findViewById(R.id.itemList);
        addToView.add(recipeName);
        addToView.addAll(recipe.getStringArrayList("ingredients"));
        recipeList.setAdapter(addToView);
        ImageView food = (ImageView) findViewById(R.id.foodImage);
        int recipeImage = recipe.getInt("recipeImage");
        food.setImageResource(recipeImage);
        Button button = (Button) findViewById(R.id.startcooking);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startcooking= new Intent(CookingStartActivity.this, CookingProgressActivity.class);
                startcooking.putExtras(recipe);
                startActivity(startcooking);
            }
        });
    }
}
