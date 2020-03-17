package com.proathome;

import android.os.Bundle;
import com.proathome.utils.CommonUtils;
import com.proathome.utils.Constants;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StaticActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        String nameFragment = null;
        if(savedInstanceState == null){

            nameFragment = getIntent().getStringExtra(Constants.ARG_NAME);
            CommonUtils.setFragment(this, nameFragment, R.id.content_static, 1, "1", "1", "1", "1", "1", "1", "1", 1.0, 1.0);

        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(nameFragment);

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
            return true;

        }

        return super.onOptionsItemSelected(item);

    }

}

