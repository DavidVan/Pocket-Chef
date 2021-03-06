package davidchan.pocketchef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by David on 9/30/2016.
 */

public class RecipeListActivity extends AppCompatActivity {

    private static final String RATING_CHANGE = "davidchan.pocketchef.rating_change";
    private static final int REQUEST_CODE_RATECHANGE = 0;

    List<Recipe> recipes;
    ListView recipeListView;
    RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_recipe_list);

        Bundle recipeData = getIntent().getExtras().getBundle("recipeData");
        recipes = (List<Recipe>) recipeData.get("recipes");

        final EditText searchBar = (EditText) findViewById(R.id.list_searchbar);
        Button searchButton = (Button) findViewById(R.id.list_search_go);
        recipeListView = (ListView) findViewById(R.id.custom_list_view);
        adapter = new RecipeAdapter(this, R.layout.recipe_row, recipes);
        recipeListView.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(searchBar.getText());
            }
        });

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView search = (TextView) findViewById(R.id.list_searchbar);
                Intent intent = new Intent(RecipeListActivity.this , CookingStartActivity.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("instructions", (Serializable) recipes.get(position).getInstructionObject());
                bundle.putString("recipeName", recipes.get(position).getRecipeName());
                bundle.putInt("recipeImage", recipes.get(position).getFilename());
                bundle.putSerializable("ingredients", (Serializable) recipes.get(position).getIngredientObject());
                bundle.putInt( "position", position );
                bundle.putFloat( "rating", recipes.get( position ).getRating() );
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_RATECHANGE);
            }

        });
        recipeListView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick( AdapterView < ? > parent, final View view, final int position, long id )
            {
                AlertDialog alertbox = new AlertDialog.Builder(RecipeListActivity.this)
                        .setMessage("Would you like to delete this Recipe?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                view.setVisibility( View.INVISIBLE );
                                recipes.remove( recipes.get( position ) );
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        })
                        .show();
                return false;
            }
        } );
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_RATECHANGE) {
            if (data == null) {
                return;
            }
            int position = data.getIntExtra( "position", 0);
            adapter.updateRating();
            recipes.get( position ).setRating( CookingStartActivity.ratingChange( data ) );

        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        Bundle recipeData = new Bundle();
        recipeData.putSerializable("recipes", (Serializable) recipes);
        data.putExtra("recipeData", recipeData);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

}
