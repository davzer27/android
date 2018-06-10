package fr.ec.producthunt.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Collection;
import fr.ec.producthunt.data.model.Post;
import fr.ec.producthunt.ui.detail.DetailActivity;
import fr.ec.producthunt.ui.detail.DetailPostFragment;

public class MainActivity extends AppCompatActivity implements PostsFragments.Callback, CollectionsFragments.Callback {

  private boolean towPane;
  private DrawerLayout mDrawerLayout;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    FrameLayout detailContainer = findViewById(R.id.detail_container);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionbar = getSupportActionBar();
    actionbar.setDisplayHomeAsUpEnabled(true);
    actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

    NavigationView navigationView = findViewById(R.id.nav_view);
    mDrawerLayout = findViewById(R.id.drawerLayout);
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        Fragment fragment = null;
        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()) {
          case R.id.nav_coll:
            fragment = new CollectionsFragments();
            Toast.makeText(MainActivity.this, "CC COLLECTION", Toast.LENGTH_SHORT).show();
            break;
          case R.id.nav_pub:
            fragment = new PostsFragments();
            Toast.makeText(MainActivity.this, "CC PUBLICATION", Toast.LENGTH_SHORT).show();
            break;
        }
        if (fragment != null)
          fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mDrawerLayout.closeDrawers();
        return true;
      }
    });

    if (detailContainer != null) {
      towPane = true;
      getSupportFragmentManager().beginTransaction()
        .add(R.id.detail_container, DetailPostFragment.getNewInstance(null))
        .commit();
    }
  }

  @Override
  public void onClickPost(Post post) {

    if (towPane) {
      DetailPostFragment detailPostFragment =
        (DetailPostFragment) getSupportFragmentManager().findFragmentById(R.id.detail_container);

      if (detailPostFragment != null) {
        detailPostFragment.loadUrl(post.getPostUrl());
      }
    } else {

      Intent intent = new Intent(this, DetailActivity.class);
      intent.putExtra(DetailActivity.POST_URL_KEY, post.getPostUrl());

      startActivity(intent);
    }
  }

  @Override
  public void onClickCollection(Collection collection) {
    PostsFragments fragment = PostsFragments.newInstance(collection.getId());
    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
  }

  @Override
  public void onClickComment(Post post) {
    System.out.println("L'id du post est " +post.getId());
    CommentsFragments fragment = CommentsFragments.newInstance(post.getId(), post.getTitle());
    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

}
