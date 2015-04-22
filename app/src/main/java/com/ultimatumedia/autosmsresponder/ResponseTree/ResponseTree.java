package com.ultimatumedia.autosmsresponder.ResponseTree;

import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ultimatumedia.autosmsresponder.R;

public class ResponseTree extends Fragment {
    private FrameLayout frame;
    private Button addNode;
    private Button editNode;
    private Button deleteNode;
    private ResponseTreeCanvas rst;

    public ResponseTree() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_response_tree, container, false);

        frame = (FrameLayout) view.findViewById(R.id.response_tree_frame);

        rst = new ResponseTreeCanvas(view.getContext());
        rst.rt = this;
        frame.addView(rst);

        FrameLayout.LayoutParams addNodeLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        addNodeLayoutParam.gravity = Gravity.BOTTOM;
        addNode = new Button(view.getContext());
        addNode.setText("Add Node");
        addNode.setLayoutParams(addNodeLayoutParam);
        addNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        frame.addView(addNode);

        FrameLayout.LayoutParams editNodeLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        editNodeLayoutParam.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        editNode = new Button(view.getContext());
        editNode.setText("Edit Node");
        editNode.setLayoutParams(editNodeLayoutParam);
        editNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        FrameLayout.LayoutParams deleteNodeLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        deleteNodeLayoutParam.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        deleteNode = new Button(view.getContext());
        deleteNode.setText("Delete Node");
        deleteNode.setLayoutParams(deleteNodeLayoutParam);
        deleteNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    public void updateView() {
        String textAddButton = "Add Node";
        frame.removeView(editNode);
        frame.removeView(deleteNode);
        for(int i = 0; i < rst.nodes.size(); i++) {
            if(rst.nodes.get(i).selected) {
                textAddButton = "Add Sub Node";
                frame.addView(deleteNode);
                frame.addView(editNode);
            }
        }
        addNode.setText(textAddButton);
    }
}
