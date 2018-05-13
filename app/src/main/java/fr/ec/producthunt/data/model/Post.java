package fr.ec.producthunt.data.model;

import android.content.ContentValues;
import fr.ec.producthunt.data.database.DataBaseContract;

public class Post {
  private String title;
  private String subTitle;
  private String imageUrl;
  private String postUrl;
  private String commentsCount;
  private String date;
  private long id;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getPostUrl() {
    return postUrl;
  }

  public void setPostUrl(String postUrl) {
    this.postUrl = postUrl;
  }
  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }


  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setcommentsCount(String commentsCount) {
    this.commentsCount = commentsCount;
  }

  public String getcommentsCount() {
    return commentsCount;
  }

  public ContentValues toContentValues() {

    ContentValues contentValues = new ContentValues();
    contentValues.put(DataBaseContract.PostTable.ID_COLUMN, id);
    contentValues.put(DataBaseContract.PostTable.TITLE_COLUMN, title);
    contentValues.put(DataBaseContract.PostTable.SUBTITLE_COLUMN, subTitle);
    contentValues.put(DataBaseContract.PostTable.IMAGE_URL_COLUMN, imageUrl);
    contentValues.put(DataBaseContract.PostTable.POST_URL_COLUMN, postUrl);
    contentValues.put(DataBaseContract.PostTable.POST_COMMENTSCOUNT_COLUMN, commentsCount);
    return contentValues;
  }


}
