package org.centum.techconnect.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.centum.techconnect.R;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewActivity extends AppCompatActivity {

    public static final String EXTRA_PATH = "path";
    @Bind(R.id.photoImageView)
    ImageView imageView;

    private PhotoViewAttacher attacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);

        attacher = new PhotoViewAttacher(imageView);

        if (getIntent() != null && getIntent().getStringExtra(EXTRA_PATH) != null) {
            String path = getIntent().getStringExtra(EXTRA_PATH);
            Picasso.with(this)
                    .load(new File(path))
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            attacher.update();
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }
}
