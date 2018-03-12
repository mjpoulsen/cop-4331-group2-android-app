package cop_4331_group2.dms_android_app;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cop_4331_group2.dms_android_app.utilities.NetworkUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

  private static final String TAG = "HomeActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    Log.d(TAG, "onCreate: Calling init().");
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemThatWasClickedId = item.getItemId();
    if (itemThatWasClickedId == R.id.action_search) {
      Context context = HomeActivity.this;
      String textToShow = "Search clicked";
      Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // This will prevent users from returning to login screen. Therefore...
  // TODO implement logout button to justify this decision.
  @Override
  public void onBackPressed() {
    // disable going back to the LoginActivity.
    moveTaskToBack(true);
  }
}