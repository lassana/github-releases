package com.github.lassana.releases.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.lassana.releases.R;
import com.github.lassana.releases.fragment.RepositoriesFragment;
import com.github.lassana.releases.fragment.TagOverviewFragment;
import com.github.lassana.releases.fragment.TagsFragment;
import com.github.lassana.releases.view.DraggablePanelLayout;

public class MainActivity extends ActionBarActivity
        implements RepositoriesFragment.RepositoriesCallback, TagsFragment.TagsCallback {

    private TagsFragment mCurrentContentFragment;
    private DraggablePanelLayout mPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPanelLayout = (DraggablePanelLayout) findViewById(android.R.id.primary);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RepositoriesFragment())
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, new TagsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == R.id.action_settings ) {
            mPanelLayout.switchState();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRepositoryTagsRequested(long repositoryId) {
        if ( mCurrentContentFragment != null ) {
            getSupportFragmentManager().beginTransaction().remove(mCurrentContentFragment).commit();
        }
        mCurrentContentFragment = TagsFragment.getInstance(repositoryId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, mCurrentContentFragment)
                .commit();
        new Runnable() {
            @Override
            public void run() {
                mPanelLayout.openBottomPanel();
            }
        }.run();
    }

    @Override
    public void onNewRepositoryRequested() {
        mPanelLayout.closeBottomPanel();
    }

    @Override
    public void onTagOverviewRequested(long tagId) {
        DialogFragment fragment = TagOverviewFragment.getInstance(tagId);
        fragment.show(getSupportFragmentManager(), Long.toString(tagId));
    }

    @Override
    public void onTagsLoadingStarted() {

    }
}
