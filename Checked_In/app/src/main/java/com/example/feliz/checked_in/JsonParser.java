package com.example.feliz.checked_in;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feliz on 2017/09/16.
 */

public class JsonParser {




    private static final String API_COMMENTS_ARRAY = "server_response";
    private static final String API_COMMENT = "comment";


    public static class JsonParserException extends Exception {
        public JsonParserException(String detailMessage) {
            super(detailMessage);
        }
    }


    /**
     * parses response from /eventsched/v1/login
     * response example:
     * {
     *  error: false
     *  id: 4
     *  name: "someone"
     *  email: "someone@gmail.com"
     *  gender: "M"
     *  key: "31b6e148cc7f2e7af07acdf0c12f83f7"
     * }
     * @param jsonStr response JSON string
     * @return User contains data about the user who logged in
     */


    /**
     * parses response from /eventsched/v1/events
     * response example:
     * {
     *   "error":false,
     *    "events":[
     *         {"id":"3","owner_id":"6","event":"meeting with supervisor",
     *           "details":"a detailed message for the even goes here....",
     *           "location":"somewhere","start_time":"1272509157","duration":"60",
     *           "last_update_time":null},
     *         {"id":"4","owner_id":"6",...}
     *    ]
     * }
     * @param jsonStr response JSON string
     * @return Event List contains data about the events
     *         for the user who made the request
     */


    /**
     * parses response from /eventsched/v1/priv
     * response example:
     * {
     *   "error":false,
     *    "members":[
     *         {"id":"3","name":"someone","email":"someone@gmail.com",
     *           "gender":"M"},
     *         {"id":"4","name":"someone2",...}
     *    ]
     * }
     * @param jsonStr response JSON string
     * @return Members List contains data about members whom the current user
     *          has privileges on
     */




    public static List<Comment> parseComments(String jsonStr) throws JsonParserException {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);


            JSONArray commentsJsonArray = jsonObject.getJSONArray(API_COMMENTS_ARRAY);

            List<Comment> commentsList = new ArrayList<>();
            for (int i = 0; i < commentsJsonArray.length(); i++) {
                JSONObject commentJson = commentsJsonArray.getJSONObject(i);

                Comment comment = helperParseComment(commentJson);
                commentsList.add(comment);
            }
            return commentsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Comment parseAddComment(String jsonStr) throws JsonParserException{
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);

            JSONObject commentJson = jsonObject.getJSONObject(API_COMMENT);
            return helperParseComment(commentJson);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Comment helperParseComment(JSONObject commentJson) throws JSONException {
        int id = commentJson.getInt(Comment.API_ID);
        int eventId = commentJson.getInt(Comment.API_EVENT_ID);
        int authorId = commentJson.getInt(Comment.API_AUTHOR_ID);
        String content = commentJson.getString(Comment.API_CONTENT);
        String authorName = commentJson.getString(Comment.API_AUTHOR_NAME);
        Long createdAtTimestamp = commentJson.getLong(Comment.API_CREATED_AT);

        return new Comment(id, authorId, eventId, content,
                authorName, createdAtTimestamp);
    }

}
