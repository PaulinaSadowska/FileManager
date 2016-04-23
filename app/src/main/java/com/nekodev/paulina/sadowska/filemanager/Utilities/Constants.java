package com.nekodev.paulina.sadowska.filemanager.utilities;

import java.util.ArrayList;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class Constants {

    public static final String SORT_BY_KEY = "sort_by";

    public class SORTING_METHODS {
        public static final int BY_NAME = 1;
        public static final int BY_DATE = 2;
        public static final int BY_SIZE = 3;
    }

    public class SORTING_DIRECTION {
        public static final int ASCENDING = 1;
        public static final int DESCENDING = -1;
    }

    public static final ArrayList<String> sizeUnits;

    static {
        sizeUnits = new ArrayList<>();
        sizeUnits.add("B");
        sizeUnits.add("kB");
        sizeUnits.add("MB");
        sizeUnits.add("GB");
    }

    public class INTENT_KEYS {
        public static final String PATH = "path";
    }

    public class SELECTED_FILES {
        public static final String KEY = "selected_fIle";
        public static final String PATH = "selected_files_path";
        public static final String COUNT = "num_of_files";
        public static final String COPY_OR_CUT = "copy_or_cut"; //true - copy, false - cut
        public static final String TYPE = "selected_file_type";
    }
}
