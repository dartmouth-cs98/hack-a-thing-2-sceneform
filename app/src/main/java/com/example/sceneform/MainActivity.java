package com.example.sceneform;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ArFragment arFragment;
    private ModelRenderable bearRenderable,
                            catRenderable,
                            dogRenderable,
                            cowRenderable,
                            elephantRenderable,
                            ferretRenderable,
                            hippoRenderable,
                            hourseRenderable,
                            koalaRenderable,
                            lionRenderable,
                            reindeerRenderable,
                            wolverineRenderable;

    ImageView bear,cat,cow,dog,elephant,ferret,hippo,horse,koala,lion,reindeer,wolverine;
    View arrayView[];
    ViewRenderable name_animal;

    int selected = 1;

    ViewRenderable animal_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);

        bear = (ImageView)findViewById(R.id.bear);

        setArrayView();
        setClickListener();

        setUpModel();
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                     //add model
                    if (selected == 1) {
                        Anchor anchor = hitResult.createAnchor();
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        createModel(anchorNode, selected);
                    }
            }
        });
    }

    private void setUpModel() {
        ViewRenderable.builder()
                .setView(this, R.layout.name_animal)
                .build()
                .thenAccept(renderable -> name_animal = renderable);


        ModelRenderable.builder()
                .setSource(this, R.raw.bear)
                .build().thenAccept(renderable -> bearRenderable = renderable)
                .exceptionally(
                        throwable ->{
                            Toast.makeText(this, "Unable to load bear model", Toast.LENGTH_SHORT).show();
                            return null;
                        });
    }

    private void setArrayView() {
        arrayView = new View[] {
                bear, cat,cow,dog
        };
    }

    private void setClickListener() {
       for (int i=0; i< arrayView.length; i++) {
           arrayView[i].setOnClickListener(this::onClick);
//           arrayView[i].setOnClickListener(this);
       }
    }

    private void createModel(AnchorNode anchorNode, int selected) {
        if (selected==1) {
            TransformableNode bear = new TransformableNode(arFragment.getTransformationSystem());
            bear.setParent(anchorNode);
            bear.setRenderable(bearRenderable);
            bear.select();

            addName(anchorNode, bear, "Bear");
        }
    }

    private void addName(AnchorNode anchorNode, TransformableNode model, String name) {

        ViewRenderable.builder().setView(this, R.layout.name_animal)
                .build()
                .thenAccept(viewRenderable -> {
                    TransformableNode nameView = new TransformableNode(arFragment.getTransformationSystem());
                    nameView.setLocalPosition(new Vector3(0, model.getLocalPosition().y+0.5f,0));
                    nameView.setParent(anchorNode);
                    nameView.setRenderable(viewRenderable);
                    nameView.select();

                    // Set text
                    TextView txt_name = (TextView) name_animal.getView();
                    txt_name.setText(name);
                    txt_name.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            anchorNode.setParent(null);
                        }
                    });
                } );
    }

//    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bear) {
            selected = 1;
            setBackground(view.getId());
        }
    }

    public void setBackground(int id) {
        for (int i = 0; i<arrayView.length; i++) {
            if(arrayView[i].getId() == id) {
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"));
            } else {
                arrayView[i].setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
