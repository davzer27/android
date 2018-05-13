package fr.ec.producthunt.ui.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Post;

/**
 * Created by Cisse & David on 21/03/18.
 */

public class PostAdapterWithLargeIcon extends BaseAdapter {

  private List<Post> dataSource = Collections.emptyList();
  public static final int TYPE_LARGE = 1;
  public static final int TYPE_SMALL = 0;
  private Context context;
  private PostsFragments.Callback callback;

  public PostAdapterWithLargeIcon(PostsFragments.Callback callback) {
    this.callback = callback;
  }

  @Override
  public int getCount() {
    return dataSource.size();
  }

  @Override
  public Object getItem(int position) {
    return dataSource.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }
  @Override
  public int getItemViewType(int position) {
    return position == 0 ? TYPE_LARGE : TYPE_SMALL;
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    int type = getItemViewType(position);
    if (convertView == null) {
      int layoutID;
      if (type == TYPE_SMALL) {
        layoutID = R.layout.item;
      } else {
        layoutID = R.layout.item_big;
      }

      convertView = LayoutInflater.from(parent.getContext())
        .inflate(layoutID, parent, false);

      viewHolder = new ViewHolder();
      viewHolder.title = convertView.findViewById(R.id.title);
      viewHolder.subtitle = convertView.findViewById(R.id.sub_title);
      viewHolder.postImage = convertView.findViewById(R.id.img_product);
      viewHolder.commentsCount = convertView.findViewById(R.id.comments_count);

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    final Post post = dataSource.get(position);
    viewHolder.title.setText(post.getTitle());
    viewHolder.subtitle.setText(post.getSubTitle());
    viewHolder.commentsCount.setText(post.getcommentsCount());
    viewHolder.commentsCount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        System.out.println("On va la");
        callback.onClickComment(post);
      }
    });

    Picasso.with(parent.getContext())
      .load(post.getImageUrl())
      .centerCrop()
      .fit()
      .into(viewHolder.postImage);
    return convertView;
  }

  public void showPosts(List<Post> posts) {
    dataSource = posts;
    notifyDataSetChanged();
  }

  private static class ViewHolder {
    TextView title;
    TextView subtitle;
    ImageView postImage;
    TextView commentsCount;
  }

}
