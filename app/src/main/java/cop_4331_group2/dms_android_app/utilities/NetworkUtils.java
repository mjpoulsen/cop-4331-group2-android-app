/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cop_4331_group2.dms_android_app.utilities;

import android.util.Log;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

  private static final String TAG = "NetworkUtils";

  // These URLs must be here and not within Strings.xml because these calls are made asynchronously
  // and will cause an exception to be thrown.
  private static final String BASE_URL = "http://10.0.2.2:3000/";
  private static final String SERVER_USERS = "users/";
  private static final String USER_LOGIN = "users/login";
  private static final String SUBMIT_USER = "users/submituser";

  public static String getUsers() throws IOException {
      final OkHttpClient client = new OkHttpClient();

      final URL base = new URL(BASE_URL);
      final URL url = new URL(base, SERVER_USERS);

      Log.d(TAG, "Connecting to " + url);

      Request request = new Request.Builder()
          .url(url)
          .build();

      Response response = client.newCall(request).execute();
      String body = response.body().string();

      return body;
  }

  public static int postSignin(String user, String password) throws IOException {
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final OkHttpClient client = new OkHttpClient();

    final URL base = new URL(BASE_URL);
    final URL url = new URL(base, USER_LOGIN);

//    JSONObject student1 = new JSONObject();
//    try {
//      student1.put("id", "3");
//      student1.put("name", "NAME OF STUDENT");
//      student1.put("year", "3rd");
//      student1.put("curriculum", "Arts");
//      student1.put("birthday", "5/5/1993");
//
//    } catch (JSONException e) {
//      e.printStackTrace();
//    }
//
//    JSONObject student2 = new JSONObject();
//    try {
//      student2.put("id", "2");
//      student2.put("name", "NAME OF STUDENT2");
//      student2.put("year", "4rd");
//      student2.put("curriculum", "scicence");
//      student2.put("birthday", "5/5/1993");
//
//    } catch (JSONException e) {
//      e.printStackTrace();
//    }
//
//
//    JSONArray jsonArray = new JSONArray();
//
//    jsonArray.put(student1);
//    jsonArray.put(student2);
//
//    JSONObject studentsObj = new JSONObject();
//    studentsObj.put("Students", jsonArray);

    final JSONObject credentials = new JSONObject();

    try {
      credentials.put("user_name", user);
      credentials.put("password", password);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Log.d(TAG, "Connecting to " + url);

    RequestBody body = RequestBody.create(JSON, credentials.toString());
    Request request = new Request.Builder()
        .url(url)
        .post(body)
        .build();

    Response response = client.newCall(request).execute();

    // Get message for debug.
    String res_body = response.body().string();
    Log.d(TAG, "Post Signin:" + res_body);

    // Obtain status code. If status is 204, user does not exist. 500 is a server issue.
    // Want to return 200.
    int statusCode = response.code();

    return statusCode;
  }

  public static String postSubmitUser(HashMap<String, String> user_details) throws IOException {
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final OkHttpClient client = new OkHttpClient();

    final URL base = new URL(BASE_URL);
    final URL url = new URL(base, SUBMIT_USER);

    final JSONObject newUser = new JSONObject();

    // TODO make values final strings.
    try {
      newUser.put("user_name", "admin");
      newUser.put("password", "5f4dcc3b5aa765d61d8327deb882cf99");
      newUser.put("first_name", "john");
      newUser.put("last_name", "doe");
      newUser.put("email", "jdoe@aol.com");
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    RequestBody body = RequestBody.create(JSON, newUser.toString());
    Request request = new Request.Builder()
        .url(url)
        .post(body)
        .build();

    Response response = client.newCall(request).execute();
    String res_body = response.body().string();

    return res_body;
  }
}