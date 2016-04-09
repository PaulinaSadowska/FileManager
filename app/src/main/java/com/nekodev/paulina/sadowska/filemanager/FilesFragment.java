package com.nekodev.paulina.sadowska.filemanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Paulina Sadowska on 09.04.16.
 */
public class FilesFragment extends Fragment {

    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.files_fragment, container, false);

        path = "/";
        // Use the current directory as title
        if (getActivity().getIntent().hasExtra("path")) {
            path = getActivity().getIntent().getStringExtra("path");
        }
        getActivity().setTitle(path);

        // Read all files sorted into the values-array
        ArrayList<String> values = new ArrayList<>();
        File dir = new File(path);
        if (!dir.canRead()) {
            getActivity().setTitle(getActivity().getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".")) {
                    values.add(file);
                }
            }
        }
        Collections.sort(values);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = (String) listView.getAdapter().getItem(position);
                if (path.endsWith(File.separator)) {
                    filename = path + filename;
                } else {
                    filename = path + File.separator + filename;
                }
                if (!new File(filename).canRead()) {
                    Toast.makeText(getActivity(), filename + " is not accessible", Toast.LENGTH_SHORT).show();
                }
                else if (new File(filename).isDirectory()) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("path", filename);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), filename + " is not a directory", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        return v;
    }
}

